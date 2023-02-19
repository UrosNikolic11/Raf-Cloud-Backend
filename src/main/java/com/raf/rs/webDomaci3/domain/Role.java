package com.raf.rs.webDomaci3.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "can_create")
    private Boolean create;
    @Column(name = "can_update")
    private Boolean update;
    @Column(name = "can_read")
    private Boolean read;
    @Column(name = "can_delete")
    private Boolean delete;
    @Column(name = "can_search_machines")
    private Boolean canSearchMachines;
    @Column(name = "can_start_machines")
    private Boolean canStartMachines;
    @Column(name = "can_stop_machines")
    private Boolean canStopMachines;
    @Column(name = "can_restart_machines")
    private Boolean canRestartMachines;
    @Column(name = "can_create_machines")
    private Boolean canCreateMachines;
    @Column(name = "can_delete_machines")
    private Boolean canDestroyMachines;


    public Role() {
    }

    public Role(Long id, Boolean create, Boolean update, Boolean read, Boolean delete, Boolean canSearchMachines, Boolean canStartMachines, Boolean canStopMachines, Boolean canRestartMachines, Boolean canCreateMachines, Boolean canDestroyMachines) {
        this.id = id;
        this.create = create;
        this.update = update;
        this.read = read;
        this.delete = delete;
        this.canSearchMachines = canSearchMachines;
        this.canStartMachines = canStartMachines;
        this.canStopMachines = canStopMachines;
        this.canRestartMachines = canRestartMachines;
        this.canCreateMachines = canCreateMachines;
        this.canDestroyMachines = canDestroyMachines;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getCreate() {
        return create;
    }

    public void setCreate(Boolean create) {
        this.create = create;
    }

    public Boolean getUpdate() {
        return update;
    }

    public void setUpdate(Boolean update) {
        this.update = update;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    public Boolean getCanSearchMachines() {
        return canSearchMachines;
    }

    public void setCanSearchMachines(Boolean canSearchMachines) {
        this.canSearchMachines = canSearchMachines;
    }

    public Boolean getCanStartMachines() {
        return canStartMachines;
    }

    public void setCanStartMachines(Boolean canStartMachines) {
        this.canStartMachines = canStartMachines;
    }

    public Boolean getCanStopMachines() {
        return canStopMachines;
    }

    public void setCanStopMachines(Boolean canStopMachines) {
        this.canStopMachines = canStopMachines;
    }

    public Boolean getCanRestartMachines() {
        return canRestartMachines;
    }

    public void setCanRestartMachines(Boolean canRestartMachines) {
        this.canRestartMachines = canRestartMachines;
    }

    public Boolean getCanCreateMachines() {
        return canCreateMachines;
    }

    public void setCanCreateMachines(Boolean canCreateMachines) {
        this.canCreateMachines = canCreateMachines;
    }

    public Boolean getCanDestroyMachines() {
        return canDestroyMachines;
    }

    public void setCanDestroyMachines(Boolean canDestroyMachines) {
        this.canDestroyMachines = canDestroyMachines;
    }
}
