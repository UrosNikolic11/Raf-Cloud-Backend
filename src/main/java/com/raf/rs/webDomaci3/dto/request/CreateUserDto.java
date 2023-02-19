package com.raf.rs.webDomaci3.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserDto(@NotBlank String firstName, @NotBlank String lastName, @Email String email,
                            @NotBlank String password, @NotBlank String roles) {
}
