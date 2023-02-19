package com.raf.rs.webDomaci3.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserDto(@NotBlank String firstName, @NotBlank String lastName, @NotBlank String roles) {
}
