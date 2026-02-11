package com.zeafen.LocNetMonitoring.data.repositories;

import com.zeafen.LocNetMonitoring.domain.models.entity.MaintenanceRecords;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface MaintenanceRecordsRepository extends JpaRepository<MaintenanceRecords, UUID> {
    @Query(
            value = "SELECT * FROM maintenance_records AS mtcr WHERE " +
                    "(:machineId IS NULL OR mtcr.machine_id = :machineId) AND " +
                    "(:maintenanceId IS NULL OR mtcr.maintenance_id = :maintenanceId) AND " +
                    "(:status IS NULL OR mtcr.status = :status) AND " +
                    "(:dateFrom IS NULL OR (mtcr.date_commissioning >= :dateFrom OR mtcr.date_finished >= :dateFrom)) AND " +
                    "(:dateUntil IS NULL OR (mtcr.date_commissioning <= :dateUntil OR mtcr.date_finished <= :dateUntil))",
            countQuery = "SELECT COUNT(*) FROM maintenance_records AS mtcr WHERE " +
                    "(:machineId IS NULL OR mtcr.machine_id = :machineId) AND " +
                    "(:maintenanceId IS NULL OR mtcr.maintenance_id = :maintenanceId) AND " +
                    "(:status IS NULL OR mtcr.status = :status) AND " +
                    "(:dateFrom IS NULL OR (mtcr.date_commissioning >= :dateFrom OR mtcr.date_finished >= :dateFrom)) AND " +
                    "(:dateUntil IS NULL OR (mtcr.date_commissioning <= :dateUntil OR mtcr.date_finished <= :dateUntil))",
            nativeQuery = true
    )
    Page<MaintenanceRecords> findAllFiltered(
            @Param("machineId") Integer machineId,
            @Param("maintenanceId") Integer maintenanceId,
            @Param("status") Integer status,
            @Param("dateFrom") OffsetDateTime dateFrom,
            @Param("dateUntil") OffsetDateTime dateUntil,
            Pageable pageable
    );
}
