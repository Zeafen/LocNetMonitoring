package com.zeafen.LocNetMonitoring.domain.models.entity;

import com.zeafen.LocNetMonitoring.domain.models.MachineUiModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.Collection;


@Entity(name = "machines")
public class Machine {

    @Id
    @Column(nullable = false, updatable = false)
    private Integer id;

    @Size(min = 5, max = 25, message = "Наименование должно составлять от 5 до 25 символов")
    @Column(nullable = false, length = 25)
    private String name;

    @Column(nullable = false, columnDefinition = "text")
    private String address;

    @Min(1)
    @Column(nullable = false)
    private Integer serialNumber;

    @Column(nullable = false)
    private OffsetDateTime dateProduced;

    @Column
    private OffsetDateTime dateCommissioning;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", nullable = false)
    private MachineModel model;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "machine")
    private Collection<JournalIncoming> machineJournalIncomings;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "machine")
    private Collection<JournalOutcoming> machineJournalOutcomings;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "machine")
    private Collection<Buffer> machineBuffers;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "machine")
    private Collection<MaintenanceRecords> maintenanceRecords;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(final Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public OffsetDateTime getDateProduced() {
        return dateProduced;
    }

    public void setDateProduced(final OffsetDateTime dateProduced) {
        this.dateProduced = dateProduced;
    }

    public OffsetDateTime getDateCommissioning() {
        return dateCommissioning;
    }

    public void setDateCommissioning(final OffsetDateTime dateCommissioning) {
        this.dateCommissioning = dateCommissioning;
    }

    public MachineModel getModel() {
        return model;
    }

    public void setModel(final MachineModel model) {
        this.model = model;
    }

    public Collection<JournalIncoming> getMachineJournalIncomings() {
        return machineJournalIncomings;
    }

    public void setMachineJournalIncomings(final Collection<JournalIncoming> machineJournalIncomings) {
        this.machineJournalIncomings = machineJournalIncomings;
    }

    public Collection<JournalOutcoming> getMachineJournalOutcomings() {
        return machineJournalOutcomings;
    }

    public void setMachineJournalOutcomings(final Collection<JournalOutcoming> machineJournalOutcomings) {
        this.machineJournalOutcomings = machineJournalOutcomings;
    }

    public Collection<MaintenanceRecords> getMaintenanceRecords() {
        return maintenanceRecords;
    }

    public void setMaintenanceRecords(Collection<MaintenanceRecords> maintenanceRecords) {
        this.maintenanceRecords = maintenanceRecords;
    }

    public MachineUiModel totUiModel() {
        MachineUiModel machine = new MachineUiModel();
        machine.setId(this.id);
        machine.setName(this.name);
        machine.setAddress(this.address);
        machine.setSerialNumber(this.serialNumber);
        machine.setDateProduced(this.dateProduced.toLocalDateTime());
        machine.setDateCommissioning(this.dateCommissioning.toLocalDateTime());
        machine.setModel(this.model);
        return machine;
    }

    public Collection<Buffer> getMachineBuffers() {
        return machineBuffers;
    }

    public void setMachineBuffers(Collection<Buffer> machineBuffers) {
        this.machineBuffers = machineBuffers;
    }
}
