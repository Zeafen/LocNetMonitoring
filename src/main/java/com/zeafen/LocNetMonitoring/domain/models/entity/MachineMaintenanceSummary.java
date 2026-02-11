package com.zeafen.LocNetMonitoring.domain.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

@Entity
@Table(name = "machine_maintenance_summary_view")
public class MachineMaintenanceSummary {
    @Id
    @Column(name = "maintenanceId")
    private Integer maintenanceId;

    @Column(name = "machine_id")
    private Integer machineId;

    @Column(name = "type_id", columnDefinition = "smallint")
    private Short typeId;

    @Column(name = "type_name")
    private String typeName;

    @Column(name = "last_maintenance_date")
    private OffsetDateTime lastMaintenanceDate;

    @Column(name = "next_maintenance_date")
    private OffsetDateTime nextMaintenanceDate;

    @Column(name = "period")
    private String period;

    @Column(name = "work_description")
    private String workDescription;

    @Column(name = "maintenance_frequency")
    private Float maintenanceFrequency;

    public Float getMaintenanceFrequency() {
        return maintenanceFrequency;
    }

    public void setMaintenanceFrequency(Float maintenanceFrequency) {
        this.maintenanceFrequency = maintenanceFrequency;
    }

    public String getWorkDescription() {
        return workDescription;
    }

    public void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public OffsetDateTime getNextMaintenanceDate() {
        return nextMaintenanceDate;
    }

    public void setNextMaintenanceDate(OffsetDateTime nextMaintenanceDate) {
        this.nextMaintenanceDate = nextMaintenanceDate;
    }

    public OffsetDateTime getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public void setLastMaintenanceDate(OffsetDateTime lastMaintenanceDate) {
        this.lastMaintenanceDate = lastMaintenanceDate;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Short getTypeId() {
        return typeId;
    }

    public void setTypeId(Short typeId) {
        this.typeId = typeId;
    }

    public Integer getMachineId() {
        return machineId;
    }

    public void setMachineId(Integer machineId) {
        this.machineId = machineId;
    }

    public Integer getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(Integer maintenanceId) {
        this.maintenanceId = maintenanceId;
    }
}
