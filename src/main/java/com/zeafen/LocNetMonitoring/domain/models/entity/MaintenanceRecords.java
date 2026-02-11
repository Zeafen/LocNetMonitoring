package com.zeafen.LocNetMonitoring.domain.models.entity;

import com.zeafen.LocNetMonitoring.domain.models.MaintenanceRecordUiModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity(name = "maintenance_records")
public class MaintenanceRecords {
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "date_finished")
    private OffsetDateTime dateFinished;

    @Column(nullable = false, name = "date_commissioned")
    private OffsetDateTime dateStarted;

    @Size(min = 10, max = 100, message = "Причина должна составлять от 10 до 100 символов")
    @Column(nullable = false, length = 100)
    private String reason;

    @Column(nullable = false)
    @Min(0)
    private Integer status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maintenance_id", nullable = false)
    private Maintenance maintenance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id", nullable = false)
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

    public OffsetDateTime getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(OffsetDateTime dateStarted) {
        this.dateStarted = dateStarted;
    }

    public OffsetDateTime getDateFinished() {
        return dateFinished;
    }

    public void setDateFinished(OffsetDateTime dateFinished) {
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

    public MaintenanceRecordUiModel toUiModel() {
        var maintenanceRecord = new MaintenanceRecordUiModel();
        maintenanceRecord.setMaintenance(this.maintenance);
        maintenanceRecord.setId(this.id);
        maintenanceRecord.setDateFinished(this.dateFinished.toLocalDateTime());
        maintenanceRecord.setDateStarted(this.dateStarted.toLocalDateTime());
        maintenanceRecord.setMachine(this.machine);
        maintenanceRecord.setReason(this.reason);
        maintenanceRecord.setStatus(this.status);
        return maintenanceRecord;
    }
}
