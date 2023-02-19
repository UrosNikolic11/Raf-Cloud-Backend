package com.raf.rs.webDomaci3.controller;

import com.raf.rs.webDomaci3.domain.enums.Action;
import com.raf.rs.webDomaci3.domain.enums.Status;
import com.raf.rs.webDomaci3.dto.request.CreateMachineDto;
import com.raf.rs.webDomaci3.dto.request.SearchDto;
import com.raf.rs.webDomaci3.dto.response.MachineDto;
import com.raf.rs.webDomaci3.service.MachineService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@CrossOrigin
@RequestMapping("/machines")
public class MachineController {

    private final MachineService machineService;

    public MachineController(MachineService machineService) {
        this.machineService = machineService;
    }

    @PreAuthorize("hasRole('CAN_CREATE_MACHINES')")
    @PostMapping
    public ResponseEntity<MachineDto> create(@RequestBody @Valid CreateMachineDto createMachineDto) {
        return ResponseEntity.ok(machineService.create(createMachineDto));
    }

    @PreAuthorize("hasRole('CAN_DESTROY_MACHINES')")
    @PutMapping("/{id}")
    public ResponseEntity<MachineDto> destroy(@PathVariable("id") Long id) {
        return ResponseEntity.ok(machineService.destroy(id));
    }

    @PreAuthorize("hasRole('CAN_START_MACHINES')")
    @PutMapping("/{id}/start")
    public ResponseEntity<?> start(@PathVariable("id") Long id) {
        machineService.start(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('CAN_STOP_MACHINES')")
    @PutMapping("/{id}/stop")
    public ResponseEntity<?> stop(@PathVariable("id") Long id) {
        machineService.stop(id);
      return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('CAN_RESTART_MACHINES')")
    @PutMapping("/{id}/restart")
    public ResponseEntity<?> restart(@PathVariable("id") Long id) {
        machineService.restart(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('CAN_SEARCH_MACHINES')")
    @GetMapping
    public ResponseEntity<Page<MachineDto>> searchAllMachines(@RequestParam String name, @RequestParam String status,
                                                              @RequestParam String from,
                                                              @RequestParam String to , Pageable pageable) {
        SearchDto searchDto;
        LocalDateTime fromFormatted = null;
        LocalDateTime toFormatted = null;
        if (!from.equals("")){
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            fromFormatted = LocalDateTime.parse(from, formatter);
            toFormatted = LocalDateTime.parse(to, formatter);
        }

        if (status.equals(Status.STOPPED.toString())){

            searchDto = new SearchDto(name,Status.STOPPED,fromFormatted,toFormatted);


        }else if(status.equals(Status.RUNNING.toString())){

            searchDto = new SearchDto(name,Status.RUNNING,fromFormatted,toFormatted);


        }else{
            searchDto = new SearchDto(name,null,fromFormatted,toFormatted);
        }
        return ResponseEntity.ok(machineService.search(searchDto, pageable));
    }

    @PreAuthorize("hasRole('CAN_CREATE_MACHINES')")
    @PostMapping("/scheduleAction")
    public ResponseEntity<?> scheduleAction(@RequestParam Long machineId, @RequestParam Action action,
                                                     @RequestParam String date) {
        LocalDateTime dateFormatted = null;
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        dateFormatted = LocalDateTime.parse(date, formatter);

        machineService.scheduleAction(machineId, dateFormatted, action);

        return ResponseEntity.ok().build();
    }
}
