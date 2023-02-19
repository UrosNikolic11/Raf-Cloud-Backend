package com.raf.rs.webDomaci3.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateMachineDto(@NotBlank String name) {
}
