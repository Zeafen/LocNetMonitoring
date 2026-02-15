package com.zeafen.LocNetMonitoring.data.repositories;

import com.zeafen.LocNetMonitoring.domain.models.entity.MaintenanceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MaintenanceTypesRepository extends JpaRepository<MaintenanceType, Short> {
    @Query(
            value = "SELECT * FROM maintenance_types AS mtct WHERE " +
                    "(:typeName IS NULL OR mtct.name LIKE %:typeName%) AND " +
                    "(:period IS NULL OR mtct.period LIKE :period%)",
            countQuery = "SELECT COUNT(*) FROM maintenance_types AS mtct WHERE " +
                    "(:typeName IS NULL OR mtct.name LIKE %:typeName%) AND " +
                    "(:period IS NULL OR mtct.period LIKE :period%)",
            nativeQuery = true
    )
    Page<MaintenanceType> findAllFiltered(
            @Param("typeName") String typeName,
            @Param("period") String period,
            Pageable pageable
    );
}
