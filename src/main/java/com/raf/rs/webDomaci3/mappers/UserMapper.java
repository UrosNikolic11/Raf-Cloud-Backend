package com.raf.rs.webDomaci3.mappers;

import com.raf.rs.webDomaci3.domain.Role;
import com.raf.rs.webDomaci3.domain.User;
import com.raf.rs.webDomaci3.dto.request.CreateUserDto;
import com.raf.rs.webDomaci3.dto.response.UserDto;
import com.raf.rs.webDomaci3.repository.RoleRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class UserMapper {

    private final RoleRepository repository;

    public UserMapper(RoleRepository repository) {
        this.repository = repository;
    }

    public UserDto map(User user){
        Role role = user.getRole();
        List<String> permissions = new ArrayList<>();
        if (role.getCreate()) permissions.add("can_create_users");
        if (role.getUpdate()) permissions.add("can_update_users");
        if (role.getDelete()) permissions.add("can_delete_users");
        if (role.getRead()) permissions.add("can_read_users");
        if (role.getCanCreateMachines()) permissions.add("can_create_machines");
        if (role.getCanSearchMachines()) permissions.add("can_search_machines");
        if (role.getCanStartMachines()) permissions.add("can_start_machines");
        if (role.getCanStopMachines()) permissions.add("can_stop_machines");
        if (role.getCanRestartMachines()) permissions.add("can_restart_machines");
        if (role.getCanDestroyMachines()) permissions.add("can_destroy_machines");
        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), permissions);
    }

    public User map(CreateUserDto createUserDto) {
        Role role = new Role();
        role.setCreate(false);
        role.setUpdate(false);
        role.setRead(false);
        role.setDelete(false);
        role.setCanCreateMachines(false);
        role.setCanSearchMachines(false);
        role.setCanStartMachines(false);
        role.setCanStopMachines(false);
        role.setCanRestartMachines(false);
        role.setCanDestroyMachines(false);

        ArrayList<String> roles = new ArrayList<>(Arrays.asList(createUserDto.roles().split(",")));

        roles.forEach((permission) -> {
            if (permission.equals("can_create_users")){
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
            if (permission.equals("can_create_machines")){
                role.setCanCreateMachines(true);
            }
            if (permission.equals("can_search_machines")){
                role.setCanSearchMachines(true);
            }
            if (permission.equals("can_start_machines")){
                role.setCanStartMachines(true);
            }
            if (permission.equals("can_stop_machines")){
                role.setCanStopMachines(true);
            }
            if (permission.equals("can_restart_machines")){
                role.setCanRestartMachines(true);
            }
            if (permission.equals("can_destroy_machines")){
                role.setCanDestroyMachines(true);
            }
        });

        repository.save(role);
        return new User(null, createUserDto.firstName(), createUserDto.lastName(), createUserDto.email(),
                createUserDto.password(), role);
    }
}


