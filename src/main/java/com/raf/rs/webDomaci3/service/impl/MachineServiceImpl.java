package com.raf.rs.webDomaci3.service.impl;

import com.raf.rs.webDomaci3.domain.ErrorMessage;
import com.raf.rs.webDomaci3.domain.FutureAction;
import com.raf.rs.webDomaci3.domain.Machine;
import com.raf.rs.webDomaci3.domain.User;
import com.raf.rs.webDomaci3.domain.enums.Action;
import com.raf.rs.webDomaci3.domain.enums.Status;
import com.raf.rs.webDomaci3.dto.request.CreateMachineDto;
import com.raf.rs.webDomaci3.dto.request.SearchDto;
import com.raf.rs.webDomaci3.dto.response.MachineDto;
import com.raf.rs.webDomaci3.exception.BadRequestException;
import com.raf.rs.webDomaci3.exception.NotFoundException;
import com.raf.rs.webDomaci3.mappers.MachineMapper;
import com.raf.rs.webDomaci3.repository.ErrorMessageRepository;
import com.raf.rs.webDomaci3.repository.FutureActionRepository;
import com.raf.rs.webDomaci3.repository.MachineRepository;
import com.raf.rs.webDomaci3.repository.UserRepository;
import com.raf.rs.webDomaci3.service.MachineService;
import com.raf.rs.webDomaci3.service.threads.MachineThread;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@EnableScheduling
public class MachineServiceImpl implements MachineService {

    private final MachineMapper machineMapper;
    private final MachineRepository machineRepository;
    private final TaskScheduler taskScheduler;
    private final UserRepository userRepository;
    private final ErrorMessageRepository errorMessageRepository;
    private final FutureActionRepository futureActionRepository;

    public MachineServiceImpl(MachineMapper machineMapper, MachineRepository machineRepository, TaskScheduler taskScheduler, UserRepository userRepository, ErrorMessageRepository errorMessageRepository, FutureActionRepository futureActionRepository) {
        this.machineMapper = machineMapper;
        this.machineRepository = machineRepository;
        this.taskScheduler = taskScheduler;
        this.userRepository = userRepository;
        this.errorMessageRepository = errorMessageRepository;
        this.futureActionRepository = futureActionRepository;
    }

    @Override
    public MachineDto create(CreateMachineDto createMachineDto) {
        Machine machine = machineMapper.map(createMachineDto);
        machineRepository.save(machine);
        return machineMapper.map(machine);
    }

    @Override
    public MachineDto destroy(Long id) {
        Machine machine = machineRepository.findById(id).orElseThrow(NotFoundException::new);
        try {
            machine.setActive(false);
            machineRepository.save(machine);
            return machineMapper.map(machine);
        } catch (ObjectOptimisticLockingFailureException e) {
            ErrorMessage errorMessage = new ErrorMessage(null, Action.DESTROY, machine.getId(), LocalDateTime.now(),
                    "Unable to destroy this machine at the moment, try again later", machine.getCreatedBy());
            errorMessageRepository.save(errorMessage);
            throw new BadRequestException("Unable to destroy this machine at the moment, try again later");
        }
    }

    @Override
    public void start(Long id) {
        Machine machine = machineRepository.findById(id).orElseThrow(NotFoundException::new);
        if (!machine.getActive()) {
            ErrorMessage errorMessage = new ErrorMessage(null, Action.START, machine.getId(), LocalDateTime.now(),
                    "Starting destroyed machine", machine.getCreatedBy());
            errorMessageRepository.save(errorMessage);
            throw new BadRequestException("Machine is destroyed");
        }
        if (!machine.getStatus().equals(Status.STOPPED)) {
            ErrorMessage errorMessage = new ErrorMessage(null, Action.START, machine.getId(), LocalDateTime.now(),
                    "Starting machine that is already started", machine.getCreatedBy());
            errorMessageRepository.save(errorMessage);
            throw new BadRequestException("This machine is already started");
        }
        MachineThread machineThread = new MachineThread(machineRepository, errorMessageRepository, Action.START, machine);
        taskScheduler.schedule(machineThread, Instant.now());
    }

    @Override
    public void stop(Long id) {
        Machine machine = machineRepository.findById(id).orElseThrow(NotFoundException::new);
        if (!machine.getActive()) {
            ErrorMessage errorMessage = new ErrorMessage(null, Action.STOP, machine.getId(), LocalDateTime.now(),
                    "Stopping destroyed machine", machine.getCreatedBy());
            errorMessageRepository.save(errorMessage);
            throw new BadRequestException("Machine is destroyed");
        }
        if (!machine.getStatus().equals(Status.RUNNING)) {
            ErrorMessage errorMessage = new ErrorMessage(null, Action.STOP, machine.getId(), LocalDateTime.now(),
                    "Stopping already stopped machine", machine.getCreatedBy());
            errorMessageRepository.save(errorMessage);
            throw new BadRequestException("This machine is already stopped");
        }
        MachineThread machineThread = new MachineThread(machineRepository, errorMessageRepository, Action.STOP, machine);
        taskScheduler.schedule(machineThread, Instant.now());
    }

    @Override
    public void restart(Long id) {
        Machine machine = machineRepository.findById(id).orElseThrow(NotFoundException::new);
        if (!machine.getActive()) {
            ErrorMessage errorMessage = new ErrorMessage(null, Action.RESTART, machine.getId(), LocalDateTime.now(),
                    "Restarting destroyed machine", machine.getCreatedBy());
            errorMessageRepository.save(errorMessage);
            throw new BadRequestException("Machine is destroyed");
        }
        if (!machine.getStatus().equals(Status.RUNNING)) {
            ErrorMessage errorMessage = new ErrorMessage(null, Action.STOP, machine.getId(), LocalDateTime.now(),
                    "Restarting stopped machine", machine.getCreatedBy());
            errorMessageRepository.save(errorMessage);
            throw new BadRequestException("This machine can't be restarted");
        }
        MachineThread machineThread = new MachineThread(machineRepository, errorMessageRepository, Action.RESTART, machine);
        taskScheduler.schedule(machineThread, Instant.now());
    }

    @Override
    public Page<MachineDto> search(SearchDto searchDto, Pageable pageable) {

        String name;
        String status;
        LocalDateTime from;
        LocalDateTime to;

        if (StringUtils.isBlank(searchDto.name())) name = "";
            else name = searchDto.name();

        if (Objects.isNull(searchDto.status())) status = "";
        else status = searchDto.status().toString();

        if (Objects.isNull(searchDto.from()) || Objects.isNull(searchDto.to())) {
            from = LocalDateTime.of(2000, 1, 1, 1, 1, 1);
            to = LocalDateTime.now();
        }
        else {
            from = searchDto.from();
            to = searchDto.to();
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByEmail(username).orElseThrow(NotFoundException::new);

        return machineRepository.search(pageable, name, status, from, to, user.getId())
                .map(machineMapper::map);
    }

    @Override
    public void scheduleAction(Long machineId, LocalDateTime dateTime, Action action) {
        Machine machine = machineRepository.findById(machineId).orElseThrow(NotFoundException::new);
        FutureAction futureAction = new FutureAction(null, machineId, dateTime, action);
        futureActionRepository.save(futureAction);
    }

    @Scheduled(cron = "0 * * * * ?")
    public void checkFutureActions() {
        List<FutureAction> futureActions = futureActionRepository.findAll();
        futureActions.forEach((futureAction) -> {
            System.out.println(futureAction.getDateTime());
            System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
            if (futureAction.getDateTime().toString().equals(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")))) {

                if (futureAction.getAction().equals(Action.START)) this.start(futureAction.getMachineId());
                if (futureAction.getAction().equals(Action.STOP)) this.stop(futureAction.getMachineId());
                if (futureAction.getAction().equals(Action.RESTART)) this.restart(futureAction.getMachineId());

            }
        });
    }
}
