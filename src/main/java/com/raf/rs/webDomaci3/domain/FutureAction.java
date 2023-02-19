package com.raf.rs.webDomaci3.domain;

import com.raf.rs.webDomaci3.domain.enums.Action;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "future_action")
public class FutureAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "machine_id", nullable = false)
    private Long machineId;
    @Column(name = "scheduled_time", nullable = false)
    private LocalDateTime dateTime;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Action action;

    public FutureAction() {
    }

    public FutureAction(Long id, Long machineId, LocalDateTime dateTime, Action action) {
        this.id = id;
        this.machineId = machineId;
        this.dateTime = dateTime;
        this.action = action;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMachineId() {
        return machineId;
    }

    public void setMachineId(Long machineId) {
        this.machineId = machineId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
