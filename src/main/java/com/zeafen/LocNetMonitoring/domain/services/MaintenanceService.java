package com.zeafen.LocNetMonitoring.domain.services;

import com.zeafen.LocNetMonitoring.domain.models.entity.MachineMaintenanceSummary;
import com.zeafen.LocNetMonitoring.domain.models.entity.Maintenance;
import com.zeafen.LocNetMonitoring.domain.models.entity.MaintenanceRecords;
import com.zeafen.LocNetMonitoring.domain.models.entity.MaintenanceType;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface MaintenanceService {
    Page<Maintenance> getMaintenance(
            int page, int perPage,
            @Nullable String typeName,
            @Nullable String period,
            @Nullable String workDescription
    );

    Page<Maintenance> getMaintenanceByType(
            int page, int perPage,
            @Nullable Short typeId,
            @Nullable String period,
            @Nullable String workDescription
    );

    Maintenance getMaintenanceById(Integer maintenanceId);

    Maintenance saveMaintenance(Maintenance maintenance);

    void deleteMaintenance(Integer maintenanceId);

    Page<MaintenanceRecords> getMaintenanceRecords(
            int page, int perPage,
            @Nullable Integer machineId,
            @Nullable Integer maintenanceId,
            @Nullable Integer status,
            @Nullable OffsetDateTime dateFrom,
            @Nullable OffsetDateTime dateUntil
    );

    MaintenanceRecords getMaintenanceRecordById(UUID recordId);

    MaintenanceRecords saveMaintenanceRecord(MaintenanceRecords maintenanceRecord);

    void deleteMaintenanceRecord(UUID recordId);

    Page<MaintenanceType> getMaintenanceTypes(
            int page, int perPage,
            @Nullable String typeName,
            @Nullable String period
    );

    Page<MachineMaintenanceSummary> getMaintenanceSummary(
            int page, int perPage,
            @Nullable Integer machineId,
            @Nullable Integer maintenanceId,
            @Nullable Short typeId,
            @Nullable Boolean orderByNextDateAscending
    );

    MaintenanceType getMaintenanceTypeById(Short typeId);

    MaintenanceType saveMaintenanceType(MaintenanceType type);

    void deleteMaintenanceType(Short typeId);
}
