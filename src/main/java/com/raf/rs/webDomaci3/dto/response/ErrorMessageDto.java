package com.raf.rs.webDomaci3.dto.response;

import com.raf.rs.webDomaci3.domain.enums.Action;

import java.time.LocalDateTime;

public record ErrorMessageDto(Long id, Action action, Long machineId, LocalDateTime date, String message, Long userId) {
}
