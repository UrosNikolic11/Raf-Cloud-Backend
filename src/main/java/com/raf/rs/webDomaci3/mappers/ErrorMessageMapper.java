package com.raf.rs.webDomaci3.mappers;

import com.raf.rs.webDomaci3.domain.ErrorMessage;
import com.raf.rs.webDomaci3.dto.response.ErrorMessageDto;
import org.springframework.stereotype.Component;

@Component
public class ErrorMessageMapper {

    public ErrorMessageDto map(ErrorMessage errorMessage) {
        return new ErrorMessageDto(errorMessage.getId(), errorMessage.getAction(),
                errorMessage.getMachineId(), errorMessage.getDate(), errorMessage.getMessage(), errorMessage.getUser().getId());
    }
}
