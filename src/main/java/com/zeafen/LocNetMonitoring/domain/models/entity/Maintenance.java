package com.zeafen.LocNetMonitoring.domain.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.Collection;


@Entity(name = "maintenance")
public class Maintenance {

    @Id
    @Column(nullable = false, updatable = false)
    private Integer id;

    @Size(min = 25, max = 255, message = "Описание работ должно составлять от 25 до 255 символов")
    @Column(nullable = false, length = 255, name = "work_description")
    private String workDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private MaintenanceType maintenanceType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "maintenance")
    private Collection<MaintenanceRecords> maintenanceRecords ;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "maintenance")
    private Collection<Buffer> maintenanceBuffers ;


    public Collection<MaintenanceRecords> getMaintenanceRecords() {
        return maintenanceRecords;
    }

    public void setMaintenanceRecords(Collection<MaintenanceRecords> maintenanceRecords) {
        this.maintenanceRecords = maintenanceRecords;
    }

    public MaintenanceType getMaintenanceType() {
        return maintenanceType;
    }

    public void setMaintenanceType(MaintenanceType maintenanceType) {
        this.maintenanceType = maintenanceType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWorkDescription() {
        return workDescription;
    }

    public void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }

    public Collection<Buffer> getMaintenanceBuffers() {
        return maintenanceBuffers;
    }

    public void setMaintenanceBuffers(Collection<Buffer> maintenanceBuffers) {
        this.maintenanceBuffers = maintenanceBuffers;
    }
}
