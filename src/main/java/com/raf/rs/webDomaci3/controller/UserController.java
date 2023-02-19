package com.raf.rs.webDomaci3.controller;

import com.raf.rs.webDomaci3.dto.request.CreateUserDto;
import com.raf.rs.webDomaci3.dto.request.UpdateUserDto;
import com.raf.rs.webDomaci3.dto.response.UserDto;
import com.raf.rs.webDomaci3.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('CAN_CREATE')")
    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody @Valid CreateUserDto createUserDto) {
        return new ResponseEntity<>(userService.create(createUserDto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('CAN_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable("id") Long id
            , @RequestBody @Valid UpdateUserDto updateUserDto) {
        return new ResponseEntity<>(userService.update(id, updateUserDto), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('CAN_READ')")
    @GetMapping
    public ResponseEntity<Page<UserDto>> findAll(Pageable pageable) {
        return new ResponseEntity<>(userService.findAll(pageable), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('CAN_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('CAN_DELETE')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id){
        userService.remove(id);
    }
}
