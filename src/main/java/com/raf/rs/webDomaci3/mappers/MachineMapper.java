package com.raf.rs.webDomaci3.mappers;

import com.raf.rs.webDomaci3.domain.Machine;
import com.raf.rs.webDomaci3.domain.User;
import com.raf.rs.webDomaci3.domain.enums.Status;
import com.raf.rs.webDomaci3.dto.request.CreateMachineDto;
import com.raf.rs.webDomaci3.dto.response.MachineDto;
import com.raf.rs.webDomaci3.exception.NotFoundException;
import com.raf.rs.webDomaci3.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MachineMapper {

    private final UserRepository userRepository;

    public MachineMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public MachineDto map(Machine machine) {
        return new MachineDto(machine.getId(), machine.getName(), machine.getStatus(), machine.getActive());
    }

    public Machine map(CreateMachineDto createMachineDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByEmail(username).orElseThrow(NotFoundException::new);
        return new Machine(null, createMachineDto.name(), Status.STOPPED, true, user, LocalDateTime.now());
    }
}
