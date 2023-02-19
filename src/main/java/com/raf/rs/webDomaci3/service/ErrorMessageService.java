package com.raf.rs.webDomaci3.service;

import com.raf.rs.webDomaci3.dto.response.ErrorMessageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ErrorMessageService {

    Page<ErrorMessageDto> findAll(Pageable pageable);
}
