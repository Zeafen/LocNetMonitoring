package com.zeafen.LocNetMonitoring.data.repositories;

import com.zeafen.LocNetMonitoring.domain.models.entity.MachineMaintenanceSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MaintenanceSummaryRepository extends JpaRepository<MachineMaintenanceSummary, Integer> {
    @Query(
            value = "SELECT * FROM machine_maintenance_summary_view AS mtcs WHERE " +
                    "(:machineId IS NULL OR mtcs.machine_id = :machineId) AND " +
                    "(:maintenanceId IS NULL OR mtcs.maintenance_id = :maintenanceId) AND " +
                    "(:typeId IS NULL OR mtcs.type_id = :typeId)",
            countQuery = "SELECT COUNT(*) FROM machine_maintenance_summary_view AS mtcs WHERE " +
                    "(:machineId IS NULL OR mtcs.machine_id = :machineId) AND " +
                    "(:maintenanceId IS NULL OR mtcs.maintenance_id = :maintenanceId) AND " +
                    "(:typeId IS NULL OR mtcs.type_id = :typeId)",
            nativeQuery = true
    )
    Page<MachineMaintenanceSummary> findAllFiltered(
            @Param("machineId") Integer machineId,
            @Param("maintenanceId") Integer maintenanceId,
            @Param("typeId") Short typeId,
            Pageable pageable
    );
}
