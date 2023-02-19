package com.raf.rs.webDomaci3.service.threads;

import com.raf.rs.webDomaci3.domain.ErrorMessage;
import com.raf.rs.webDomaci3.domain.Machine;
import com.raf.rs.webDomaci3.domain.enums.Action;
import com.raf.rs.webDomaci3.domain.enums.Status;
import com.raf.rs.webDomaci3.exception.BadRequestException;
import com.raf.rs.webDomaci3.exception.NotFoundException;
import com.raf.rs.webDomaci3.repository.ErrorMessageRepository;
import com.raf.rs.webDomaci3.repository.MachineRepository;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.time.LocalDateTime;

public class MachineThread implements Runnable{

    private final MachineRepository machineRepository;
    private final ErrorMessageRepository errorMessageRepository;
    private final Action action;
    private final Machine machine;

    public MachineThread(MachineRepository machineRepository, ErrorMessageRepository errorMessageRepository, Action action, Machine machine) {
        this.machineRepository = machineRepository;
        this.errorMessageRepository = errorMessageRepository;
        this.action = action;
        this.machine = machine;
    }

    @Override
    public void run() {
        if (action.equals(Action.START)) {
            try {
                Thread.sleep(10000);

                try {
                    this.machine.setStatus(Status.RUNNING);
                    machineRepository.save(this.machine);
                } catch (ObjectOptimisticLockingFailureException e) {
                    ErrorMessage errorMessage = new ErrorMessage(null, Action.START, this.machine.getId(), LocalDateTime.now(),
                            "Unable to start machine", machine.getCreatedBy());
                    errorMessageRepository.save(errorMessage);
                    throw new BadRequestException("Unable to start this machine at the moment, try again later");
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if (action.equals(Action.STOP)) {
            try {
                Thread.sleep(10000);

                try {
                    this.machine.setStatus(Status.STOPPED);
                    machineRepository.save(this.machine);
                } catch (ObjectOptimisticLockingFailureException e) {
                    ErrorMessage errorMessage = new ErrorMessage(null, Action.STOP, this.machine.getId(), LocalDateTime.now(),
                            "Unable to stop machine", machine.getCreatedBy());
                    errorMessageRepository.save(errorMessage);
                    throw new BadRequestException("Unable to stop this machine at the moment, try again later");
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if (action.equals(Action.RESTART)) {
            try {
                Thread.sleep(5000);

                try {
                    this.machine.setStatus(Status.STOPPED);
                    machineRepository.save(this.machine);
                } catch (ObjectOptimisticLockingFailureException e) {
                    ErrorMessage errorMessage = new ErrorMessage(null, Action.RESTART, this.machine.getId(), LocalDateTime.now(),
                            "Unable to restart machine", machine.getCreatedBy());
                    errorMessageRepository.save(errorMessage);
                    throw new BadRequestException("Unable to restart this machine at the moment, try again later");
                }

                Thread.sleep(5000);

                try {
                    Machine m = machineRepository.findById(this.machine.getId()).orElseThrow(NotFoundException::new);
                    m.setStatus(Status.RUNNING);
                    machineRepository.save(m);
                } catch (ObjectOptimisticLockingFailureException e) {
                    ErrorMessage errorMessage = new ErrorMessage(null, Action.RESTART, this.machine.getId(), LocalDateTime.now(),
                            "Unable to restart machine", machine.getCreatedBy());
                    errorMessageRepository.save(errorMessage);
                    throw new BadRequestException("Unable to restart this machine at the moment, try again later");
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


    }
}
