package com.raf.rs.webDomaci3.service;

import com.raf.rs.webDomaci3.domain.enums.Action;
import com.raf.rs.webDomaci3.dto.request.CreateMachineDto;
import com.raf.rs.webDomaci3.dto.request.SearchDto;
import com.raf.rs.webDomaci3.dto.response.MachineDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface MachineService {
    MachineDto create(CreateMachineDto createMachineDto);

    MachineDto destroy(Long id);

    void start(Long id);

    void stop(Long id);

    void restart(Long id);

    Page<MachineDto> search(SearchDto searchDto, Pageable pageable);

    void scheduleAction(Long machineId, LocalDateTime dateTime, Action action);
}
