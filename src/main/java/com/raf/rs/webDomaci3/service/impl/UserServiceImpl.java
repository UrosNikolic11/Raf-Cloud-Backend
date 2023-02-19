package com.raf.rs.webDomaci3.service.impl;

import com.raf.rs.webDomaci3.domain.Role;
import com.raf.rs.webDomaci3.domain.User;
import com.raf.rs.webDomaci3.dto.request.CreateUserDto;
import com.raf.rs.webDomaci3.dto.request.UpdateUserDto;
import com.raf.rs.webDomaci3.dto.response.UserDto;
import com.raf.rs.webDomaci3.exception.BadRequestException;
import com.raf.rs.webDomaci3.exception.NotFoundException;
import com.raf.rs.webDomaci3.mappers.UserMapper;
import com.raf.rs.webDomaci3.repository.UserRepository;
import com.raf.rs.webDomaci3.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto create(CreateUserDto createUserDto) {
        Optional<User> userCheck = userRepository.findUserByEmail(createUserDto.email());
        if (userCheck.isPresent())
            throw new BadRequestException("Email in use");

        User user = userMapper.map(createUserDto);

        try {
            String hashPwd = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashPwd);
        } catch (Exception ex) {
            throw new BadRequestException("Error with password hashing!");
        }

        userRepository.save(user);

        return userMapper.map(user);
    }

    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::map);
    }

    @Override
    public UserDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(NotFoundException::new);
        return userMapper.map(user);
    }

    @Override
    public UserDto update(Long id, UpdateUserDto updateUserDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new BadRequestException("User not found!"));
        user.setFirstName(updateUserDto.firstName());
        user.setLastName(updateUserDto.lastName());
        Role role = user.getRole();

        ArrayList<String> roles = new ArrayList<>(Arrays.asList(updateUserDto.roles().split(",")));

        roles.forEach((permission) -> {
            if (permission.equals("can_crate_users")){
                role.setCreate(true);
            }

            if (permission.equals("can_update_users")){
                role.setUpdate(true);
            }

            if (permission.equals("can_read_users")){
                role.setRead(true);
            }

            if (permission.equals("can_delete_users")){
                role.setDelete(true);
            }

            if(permission.equals("can_delete_machines")){
                role.setCanDestroyMachines(true);
            }

            if(permission.equals("can_create_machines")){
                role.setCanCreateMachines(true);
            }

            if(permission.equals("can_restart_machines")){
                role.setCanRestartMachines(true);
            }

            if(permission.equals("can_stop_machines")){
                role.setCanStopMachines(true);
            }

            if(permission.equals("can_start_machines")){
                role.setCanStartMachines(true);
            }

            if(permission.equals("can_search_machines")){
                role.setCanSearchMachines(true);
            }
        });

        userRepository.save(user);

        return userMapper.map(user);
    }

    @Override
    public void remove(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new BadRequestException("User not found!"));
        userRepository.delete(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        Role role = user.getRole();

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        if (role.getCreate()) authorities.add(new SimpleGrantedAuthority("ROLE_CAN_CREATE"));
        if (role.getDelete()) authorities.add(new SimpleGrantedAuthority("ROLE_CAN_DELETE"));
        if (role.getRead()) authorities.add(new SimpleGrantedAuthority("ROLE_CAN_READ"));
        if (role.getUpdate()) authorities.add(new SimpleGrantedAuthority("ROLE_CAN_UPDATE"));
        if (role.getCanCreateMachines()) authorities.add(new SimpleGrantedAuthority("ROLE_CAN_CREATE_MACHINES"));
        if (role.getCanSearchMachines()) authorities.add(new SimpleGrantedAuthority("ROLE_CAN_SEARCH_MACHINES"));
        if (role.getCanStartMachines()) authorities.add(new SimpleGrantedAuthority("ROLE_CAN_START_MACHINES"));
        if (role.getCanStopMachines()) authorities.add(new SimpleGrantedAuthority("ROLE_CAN_STOP_MACHINES"));
        if (role.getCanRestartMachines()) authorities.add(new SimpleGrantedAuthority("ROLE_CAN_RESTART_MACHINES"));
        if (role.getCanDestroyMachines()) authorities.add(new SimpleGrantedAuthority("ROLE_CAN_DESTROY_MACHINES"));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}