package com.raf.rs.webDomaci3.controller;

import com.raf.rs.webDomaci3.dto.response.ErrorMessageDto;
import com.raf.rs.webDomaci3.service.ErrorMessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/error-history")
public class ErrorMessageController {

    private final ErrorMessageService errorMessageService;

    public ErrorMessageController(ErrorMessageService errorMessageService) {
        this.errorMessageService = errorMessageService;
    }

    @GetMapping
    public ResponseEntity<Page<ErrorMessageDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok(errorMessageService.findAll(pageable));
    }
}
