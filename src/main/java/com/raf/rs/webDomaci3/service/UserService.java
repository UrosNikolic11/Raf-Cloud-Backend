package com.raf.rs.webDomaci3.service;

import com.raf.rs.webDomaci3.dto.request.CreateUserDto;
import com.raf.rs.webDomaci3.dto.request.UpdateUserDto;
import com.raf.rs.webDomaci3.dto.response.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDto create(CreateUserDto createUserDto);
    Page<UserDto> findAll(Pageable pageable);
    UserDto findById(Long id);
    UserDto update(Long id, UpdateUserDto updateUserDto);
    void remove(Long id);
}
