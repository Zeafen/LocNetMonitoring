package com.zeafen.LocNetMonitoring.domain.models;

import com.zeafen.LocNetMonitoring.domain.models.entity.Machine;
import com.zeafen.LocNetMonitoring.domain.models.entity.Maintenance;
import com.zeafen.LocNetMonitoring.domain.models.entity.MaintenanceRecords;
import com.zeafen.LocNetMonitoring.domain.validation.FieldsCompare;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@FieldsCompare(greaterField = "dateFinished", lesserField = "dateStarted", message = "Значение даты окончания ТО должно быть больше знаяения начала")
public class MaintenanceRecordUiModel {
    private UUID id;

    private LocalDateTime dateFinished;

    private LocalDateTime dateStarted;

    @Size(min = 10, max = 100, message = "Причина должна составлять от 10 до 100 символов")
    private String reason;

    @Min(0)
    private Integer status;

    @NotNull
    private Maintenance maintenance;

    @NotNull
    private Machine machine;

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public Maintenance getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(Maintenance maintenance) {
        this.maintenance = maintenance;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(LocalDateTime dateStarted) {
        this.dateStarted = dateStarted;
    }

    public LocalDateTime getDateFinished() {
        return dateFinished;
    }

    public void setDateFinished(LocalDateTime dateFinished) {
        this.dateFinished = dateFinished;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public MaintenanceRecords toEntity(){

        var maintenanceRecord = new MaintenanceRecords();
        maintenanceRecord.setMaintenance(this.maintenance);
        maintenanceRecord.setId(this.id);
        maintenanceRecord.setDateFinished(this.dateFinished.atOffset(ZoneOffset.UTC));
        maintenanceRecord.setDateStarted(this.dateStarted.atOffset(ZoneOffset.UTC));
        maintenanceRecord.setMachine(this.machine);
        maintenanceRecord.setReason(this.reason);
        maintenanceRecord.setStatus(this.status);
        return maintenanceRecord;
    }
}
