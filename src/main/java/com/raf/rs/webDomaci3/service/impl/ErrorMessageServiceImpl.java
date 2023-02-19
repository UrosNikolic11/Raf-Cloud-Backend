package com.raf.rs.webDomaci3.service.impl;

import com.raf.rs.webDomaci3.domain.User;
import com.raf.rs.webDomaci3.dto.response.ErrorMessageDto;
import com.raf.rs.webDomaci3.exception.NotFoundException;
import com.raf.rs.webDomaci3.mappers.ErrorMessageMapper;
import com.raf.rs.webDomaci3.repository.ErrorMessageRepository;
import com.raf.rs.webDomaci3.repository.UserRepository;
import com.raf.rs.webDomaci3.service.ErrorMessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ErrorMessageServiceImpl implements ErrorMessageService {

    private final ErrorMessageRepository errorMessageRepository;
    private final ErrorMessageMapper errorMessageMapper;
    private final UserRepository userRepository;

    public ErrorMessageServiceImpl(ErrorMessageRepository errorMessageRepository, ErrorMessageMapper errorMessageMapper, UserRepository userRepository) {
        this.errorMessageRepository = errorMessageRepository;
        this.errorMessageMapper = errorMessageMapper;
        this.userRepository = userRepository;
    }

    @Override
    public Page<ErrorMessageDto> findAll(Pageable pageable) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByEmail(username).orElseThrow(NotFoundException::new);
        return errorMessageRepository.findAllByUser(user, pageable).map(errorMessageMapper::map);
    }
}
