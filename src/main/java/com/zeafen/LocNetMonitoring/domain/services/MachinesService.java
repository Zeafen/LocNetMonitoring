package com.zeafen.LocNetMonitoring.domain.services;

import com.zeafen.LocNetMonitoring.domain.models.entity.Machine;
import com.zeafen.LocNetMonitoring.domain.models.entity.MachineStatsView;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;

import java.time.OffsetDateTime;

public interface MachinesService {
    Page<Machine> getMachines(
            int page,
            int perPage,
            @Nullable String name,
            @Nullable String address,
            @Nullable OffsetDateTime date_commissioning_from,
            @Nullable OffsetDateTime date_commissioning_to,
            @Nullable Integer serialNumber
    );

    Page<Machine> getMachinesByType(
            int page,
            int perPage,
            @Nullable Short typeID,
            @Nullable String name,
            @Nullable String address,
            @Nullable OffsetDateTime date_commissioning_from,
            @Nullable OffsetDateTime date_commissioning_to,
            @Nullable Integer serialNumber
    );
    Page<Machine> getMachinesByType(
            int page,
            int perPage,
            @Nullable String typeName,
            @Nullable String name,
            @Nullable String address,
            @Nullable OffsetDateTime date_commissioning_from,
            @Nullable OffsetDateTime date_commissioning_to,
            @Nullable Integer serialNumber
    );

    Page<Machine> getMachinesByModel(
            int page,
            int perPage,
            @Nullable Short modelID,
            @Nullable String name,
            @Nullable String address,
            @Nullable OffsetDateTime date_commissioning_from,
            @Nullable OffsetDateTime date_commissioning_to,
            @Nullable Integer serialNumber
    );

    Page<Machine> getMachinesByModel(
            int page,
            int perPage,
            @Nullable String modelName,
            @Nullable String name,
            @Nullable String address,
            @Nullable OffsetDateTime date_commissioning_from,
            @Nullable OffsetDateTime date_commissioning_to,
            @Nullable Integer serialNumber
    );

    Page<MachineStatsView> getMachineStats(
            int page,
            int perPage,
            @NotNull Integer machineID,
            @Nullable OffsetDateTime dayFrom,
            @Nullable OffsetDateTime dayTo
    );

    Machine getMachineByID(Integer id);

    Machine saveMachine(Machine machine);

    void deleteMachine(Integer id);
}
