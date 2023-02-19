package com.raf.rs.webDomaci3.dto.response;

import java.util.List;

public record UserDto(Long id, String firstName, String lastName, String email, List<String> roles) {
}
