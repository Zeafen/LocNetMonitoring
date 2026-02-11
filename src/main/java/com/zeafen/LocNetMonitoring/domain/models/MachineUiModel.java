package com.zeafen.LocNetMonitoring.domain.models;

import com.zeafen.LocNetMonitoring.domain.models.entity.Machine;
import com.zeafen.LocNetMonitoring.domain.models.entity.MachineModel;
import com.zeafen.LocNetMonitoring.domain.validation.FieldsCompare;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@FieldsCompare(greaterField = "dateCommissioning", lesserField = "dateProduced", message = "Дата постановки в работу должна быть позже даты сборки")
public class MachineUiModel {
    private Integer id;

    @NotNull
    @Size(min = 5, max = 25, message = "Наименование должно составлять от 5 до 25 символов")
    private String name;

    @NotNull
    @Size(min = 5, message = "Адресс устройства должен составлять как минимум 5 символов")
    private String address;

    @Min(1)
    @NotNull
    private Integer serialNumber;

    @NotNull
    private LocalDateTime dateProduced;

    private LocalDateTime dateCommissioning;

    @NotNull
    private MachineModel model;

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

    public LocalDateTime getDateProduced() {
        return dateProduced;
    }

    public void setDateProduced(final LocalDateTime dateProduced) {
        this.dateProduced = dateProduced;
    }

    public LocalDateTime getDateCommissioning() {
        return dateCommissioning;
    }

    public void setDateCommissioning(final LocalDateTime dateCommissioning) {
        this.dateCommissioning = dateCommissioning;
    }

    public MachineModel getModel() {
        return model;
    }

    public void setModel(final MachineModel model) {
        this.model = model;
    }

    public Machine toMachine() {
        Machine machine = new Machine();
        machine.setId(this.id);
        machine.setName(this.name);
        machine.setAddress(this.address);
        machine.setSerialNumber(this.serialNumber);
        machine.setDateProduced(this.dateProduced != null ? this.dateProduced.atOffset(ZoneOffset.UTC) : null);
        machine.setDateCommissioning(this.dateCommissioning != null ? this.dateCommissioning.atOffset(ZoneOffset.UTC) : null);
        machine.setModel(this.model);
        return machine;
    }
}
