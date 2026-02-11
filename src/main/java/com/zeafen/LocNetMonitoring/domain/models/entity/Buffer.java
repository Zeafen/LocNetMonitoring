package com.zeafen.LocNetMonitoring.domain.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity(name = "buffer")
public class Buffer {
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue
    @UuidGenerator
    private UUID id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id", nullable = false)
    private Machine machine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maintenance_id")
    private Maintenance maintenance;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead;

    @Column(name = "generated_date", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime dateGenerated;

    @Column(name = "buffer_type", nullable = false, columnDefinition = "SMALLINT")
    @Min(0)
    private Short bufferType;

    public OffsetDateTime getDateGenerated() {
        return dateGenerated;
    }

    public void setDateGenerated(OffsetDateTime dateGenerated) {
        this.dateGenerated = dateGenerated;
    }

    public Short getBufferType() {
        return bufferType;
    }

    public void setBufferType(Short bufferType) {
        this.bufferType = bufferType;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public Maintenance getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(Maintenance maintenance) {
        this.maintenance = maintenance;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
