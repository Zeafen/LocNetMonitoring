package com.zeafen.LocNetMonitoring.data.services;

import com.zeafen.LocNetMonitoring.data.repositories.MaintenanceRecordsRepository;
import com.zeafen.LocNetMonitoring.data.repositories.MaintenanceRepository;
import com.zeafen.LocNetMonitoring.data.repositories.MaintenanceSummaryRepository;
import com.zeafen.LocNetMonitoring.data.repositories.MaintenanceTypesRepository;
import com.zeafen.LocNetMonitoring.domain.models.entity.MachineMaintenanceSummary;
import com.zeafen.LocNetMonitoring.domain.models.entity.Maintenance;
import com.zeafen.LocNetMonitoring.domain.models.entity.MaintenanceRecords;
import com.zeafen.LocNetMonitoring.domain.models.entity.MaintenanceType;
import com.zeafen.LocNetMonitoring.domain.services.MaintenanceService;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class JpaMaintenanceService implements MaintenanceService {
    private final MaintenanceTypesRepository _maintenanceTypes;
    private final MaintenanceRepository _maintenance;
    private final MaintenanceRecordsRepository _maintenanceRecords;
    private final MaintenanceSummaryRepository _maintenanceSummary;

    public JpaMaintenanceService(MaintenanceTypesRepository maintenanceTypes, MaintenanceRepository maintenance, MaintenanceRecordsRepository maintenanceRecords, MaintenanceSummaryRepository maintenanceSummary) {
        _maintenanceTypes = maintenanceTypes;
        _maintenance = maintenance;
        _maintenanceRecords = maintenanceRecords;
        _maintenanceSummary = maintenanceSummary;
    }

    @Override
    public Page<Maintenance> getMaintenance(int page, int perPage, @Nullable String typeName, @Nullable String period, @Nullable String workDescription) {
        return _maintenance.findAllFiltered(
                typeName == null || typeName.isBlank() ? null : typeName,
                period == null || period.isBlank() ? null : period,
                workDescription == null || workDescription.isBlank() ? null : workDescription,
                PageRequest.of(page, perPage)
        );
    }

    @Override
    public Page<Maintenance> getMaintenanceByType(int page, int perPage, @Nullable Short typeId, @Nullable String period, @Nullable String workDescription) {
        return _maintenance.findByTypeFiltered(
                typeId,
                period == null || period.isBlank() ? null : period,
                workDescription == null || workDescription.isBlank() ? null : workDescription,
                PageRequest.of(page, perPage)
        );
    }

    @Override
    public Maintenance getMaintenanceById(Integer maintenanceId) {
        return _maintenance.findById(maintenanceId).orElse(null);
    }

    @Override
    public Maintenance saveMaintenance(Maintenance maintenance) {
        return _maintenance.save(maintenance);
    }

    @Override
    public void deleteMaintenance(Integer maintenanceId) {
        _maintenance.deleteById(maintenanceId);
    }

    @Override
    public Page<MaintenanceRecords> getMaintenanceRecords(int page, int perPage, @Nullable Integer machineId, @Nullable Integer maintenanceId, @Nullable Integer status, @Nullable OffsetDateTime dateFrom, @Nullable OffsetDateTime dateUntil) {
        return _maintenanceRecords.findAllFiltered(
                machineId,
                maintenanceId,
                status,
                dateFrom,
                dateUntil,
                PageRequest.of(page, perPage)
        );
    }

    @Override
    public MaintenanceRecords getMaintenanceRecordById(UUID recordId) {
        return _maintenanceRecords.findById(recordId).orElse(null);
    }

    @Override
    public MaintenanceRecords saveMaintenanceRecord(MaintenanceRecords maintenanceRecord) {
        return _maintenanceRecords.save(maintenanceRecord);
    }

    @Override
    public void deleteMaintenanceRecord(UUID recordId) {
        _maintenanceRecords.deleteById(recordId);
    }

    @Override
    public Page<MaintenanceType> getMaintenanceTypes(int page, int perPage, @Nullable String typeName, @Nullable String period) {
        return _maintenanceTypes.findAllFiltered(
                typeName == null || typeName.isBlank() ? null : typeName,
                period == null || period.isBlank() ? null : period,
                PageRequest.of(page, perPage));
    }

    @Override
    public Page<MachineMaintenanceSummary> getMaintenanceSummary(int page, int perPage, @Nullable Integer machineId, @Nullable Integer maintenanceId, @Nullable Short typeId, @Nullable Boolean orderByNextDateAscending) {
        return _maintenanceSummary.findAllFiltered(
                machineId,
                maintenanceId,
                typeId,
                PageRequest.of(page, perPage));
    }

    @Override
    public MaintenanceType getMaintenanceTypeById(Short typeId) {
        return _maintenanceTypes.findById(typeId).orElse(null);
    }

    @Override
    public MaintenanceType saveMaintenanceType(MaintenanceType type) {
        return _maintenanceTypes.save(type);
    }

    @Override
    public void deleteMaintenanceType(Short typeId) {
        _maintenanceTypes.deleteById(typeId);
    }
}
