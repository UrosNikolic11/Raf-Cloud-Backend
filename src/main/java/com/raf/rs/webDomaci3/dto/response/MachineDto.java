package com.raf.rs.webDomaci3.dto.response;

import com.raf.rs.webDomaci3.domain.enums.Status;

public record MachineDto(Long id, String name, Status status, Boolean active) {
}
