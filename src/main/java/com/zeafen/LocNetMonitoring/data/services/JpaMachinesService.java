package com.zeafen.LocNetMonitoring.data.services;

import com.zeafen.LocNetMonitoring.data.repositories.MachineStatsRepository;
import com.zeafen.LocNetMonitoring.data.repositories.MachinesRepository;
import com.zeafen.LocNetMonitoring.domain.models.entity.Machine;
import com.zeafen.LocNetMonitoring.domain.models.entity.MachineStatsView;
import com.zeafen.LocNetMonitoring.domain.services.MachinesService;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class JpaMachinesService implements MachinesService {
    private final MachinesRepository _machines;
    private final MachineStatsRepository _machineStats;

    public JpaMachinesService(MachinesRepository machines, MachineStatsRepository machineStats) {
        _machines = machines;
        _machineStats = machineStats;
    }

    @Override
    public void refreshStats() {
        _machineStats.refreshStats();
    }

    @Override
    public Page<Machine> getMachines(int page, int perPage,
                                     @Nullable String name,
                                     @Nullable String address,
                                     @Nullable OffsetDateTime date_commissioning_from,
                                     @Nullable OffsetDateTime date_commissioning_to,
                                     @Nullable Integer serialNumber) {
        return _machines.findAllFiltered(
                name == null || name.isBlank() ? null : name,
                address == null || address.isBlank() ? null : address,
                date_commissioning_from,
                date_commissioning_to,
                serialNumber,
                PageRequest.of(page, perPage));
    }

    @Override
    public Page<Machine> getMachinesByType(int page, int perPage,
                                           @Nullable Short typeID,
                                           @Nullable String name,
                                           @Nullable String address,
                                           @Nullable OffsetDateTime date_commissioning_from,
                                           @Nullable OffsetDateTime date_commissioning_to,
                                           @Nullable Integer serialNumber) {
        return _machines.findAllByTypeIdFiltered(
                typeID,
                name == null || name.isBlank() ? null : name,
                address == null || address.isBlank() ? null : address,
                date_commissioning_from,
                date_commissioning_to,
                serialNumber,
                PageRequest.of(page, perPage)
        );
    }

    @Override
    public Page<Machine> getMachinesByType(int page, int perPage,
                                           @Nullable String typeName,
                                           @Nullable String name,
                                           @Nullable String address,
                                           @Nullable OffsetDateTime date_commissioning_from,
                                           @Nullable OffsetDateTime date_commissioning_to,
                                           @Nullable Integer serialNumber) {
        return _machines.findAllByTypeNameFiltered(
                typeName == null || typeName.isBlank() ? null : typeName,
                name == null || name.isBlank() ? null : name,
                address == null || address.isBlank() ? null : address,
                date_commissioning_from,
                date_commissioning_to,
                serialNumber,
                PageRequest.of(page, perPage)
        );
    }

    @Override
    public Page<Machine> getMachinesByModel(int page, int perPage,
                                            @Nullable Short modelID,
                                            @Nullable String name,
                                            @Nullable String address,
                                            @Nullable OffsetDateTime date_commissioning_from,
                                            @Nullable OffsetDateTime date_commissioning_to,
                                            @Nullable Integer serialNumber) {
        return _machines.findAllByModelIdFiltered(
                modelID,
                name == null || name.isBlank() ? null : name,
                address == null || address.isBlank() ? null : address,
                date_commissioning_from,
                date_commissioning_to,
                serialNumber,
                PageRequest.of(page, perPage)
        );
    }

    @Override
    public Page<Machine> getMachinesByModel(int page, int perPage,
                                            @Nullable String modelName,
                                            @Nullable String name,
                                            @Nullable String address,
                                            @Nullable OffsetDateTime date_commissioning_from,
                                            @Nullable OffsetDateTime date_commissioning_to,
                                            @Nullable Integer serialNumber) {
        return _machines.findAllByModelNameFiltered(
                modelName == null || modelName.isBlank() ? null : modelName,
                name == null || name.isBlank() ? null : name,
                address == null || address.isBlank() ? null : address,
                date_commissioning_from,
                date_commissioning_to,
                serialNumber,
                PageRequest.of(page, perPage)
        );
    }

    @Override
    public Page<MachineStatsView> getMachineStats(int page, int perPage, @NonNull Integer machineID, @Nullable OffsetDateTime dayFrom, @Nullable OffsetDateTime dayTo) {
        return _machineStats.findAllFiltered(
                machineID,
                dayFrom,
                dayTo,
                PageRequest.of(page, perPage)
        );
    }

    @CacheEvict(
            value = "machine",
            key = "#id"
    )
    @Override
    public Machine getMachineByID(Integer id) {
        return _machines.findById(id).orElse(null);
    }

    @Override
    public Machine saveMachine(Machine machine) {
        return _machines.save(machine);
    }

    @Override
    public void deleteMachine(Integer id) {
        _machines.deleteById(id);
    }
}
