package com.raf.rs.webDomaci3.domain;

import com.raf.rs.webDomaci3.domain.enums.Action;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "error_message")
public class ErrorMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Action action;
    @Column(nullable = false)
    private Long machineId;
    @Column(nullable = false)
    private LocalDateTime date;
    @Column(nullable = false)
    private String message;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public ErrorMessage() {
    }

    public ErrorMessage(Long id, Action action, Long machineId, LocalDateTime date, String message, User user) {
        this.id = id;
        this.action = action;
        this.machineId = machineId;
        this.date = date;
        this.message = message;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getMachineId() {
        return machineId;
    }

    public void setMachineId(Long machineId) {
        this.machineId = machineId;
    }
}
