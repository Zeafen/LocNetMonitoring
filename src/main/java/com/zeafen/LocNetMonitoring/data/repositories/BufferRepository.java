package com.zeafen.LocNetMonitoring.data.repositories;

import com.zeafen.LocNetMonitoring.domain.models.entity.Buffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.UUID;


public interface BufferRepository extends JpaRepository<Buffer, UUID> {
    @Query(
            value = "SELECT * FROM buffer AS bf WHERE " +
                    "(:machineId IS NULL OR bf.machine_id = :machineId) AND " +
                    "(:maintenanceId IS NULL OR bf.maintenance_id = :maintenanceId) AND " +
                    "(CAST(:dateFrom AS TIMESTAMP WITH TIME ZONE) IS NULL OR bf.generated_date >= CAST(:dateFrom AS TIMESTAMP WITH TIME ZONE)) AND " +
                    "(CAST(:dateUntil AS TIMESTAMP WITH TIME ZONE) IS NULL OR bf.generated_date <= CAST(:dateUntil AS TIMESTAMP WITH TIME ZONE)) AND " +
                    "(:isRead IS NULL OR bf.is_read = :isRead) AND " +
                    "(:bufferType IS NULL OR bf.buffer_type = :bufferType)",
            countQuery = "SELECT COUNT(*) FROM buffer AS bf WHERE " +
                    "(:machineId IS NULL OR bf.machine_id = :machineId) AND " +
                    "(:maintenanceId IS NULL OR bf.maintenance_id = :maintenanceId) AND " +
                    "(CAST(:dateFrom AS TIMESTAMP WITH TIME ZONE) IS NULL OR bf.generated_date >= CAST(:dateFrom AS TIMESTAMP WITH TIME ZONE)) AND " +
                    "(CAST(:dateUntil AS TIMESTAMP WITH TIME ZONE) IS NULL OR bf.generated_date <= CAST(:dateUntil AS TIMESTAMP WITH TIME ZONE)) AND " +
                    "(:isRead IS NULL OR bf.is_read = :isRead) AND " +
                    "(:bufferType IS NULL OR bf.buffer_type = :bufferType)",
            nativeQuery = true
    )
    Page<Buffer> findAllFiltered(
            @Param("machineId") Integer machineId,
            @Param("maintenanceId") Integer maintenanceId,
            @Param("dateFrom") OffsetDateTime dateFrom,
            @Param("dateUntil") OffsetDateTime dateUntil,
            @Param("isRead") Boolean isRead,
            @Param("bufferType") Short bufferType,
            Pageable pageable
    );

    @Query(value = "SELECT COUNT(*) FROM buffer AS bf WHERE (:bufferType IS NULL OR bf.buffer_type = :bufferType) AND bf.is_read = FALSE",
            nativeQuery = true)
    Long countTypeUnread(
            @Param("bufferType") Short bufferType
    );
}
