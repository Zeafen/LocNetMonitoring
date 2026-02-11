package com.zeafen.LocNetMonitoring.data.repositories;

import com.zeafen.LocNetMonitoring.domain.models.entity.Maintenance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Integer> {
    @Query(
            value = "SELECT mtc.* FROM maintenance AS mtc " +
                    "INNER JOIN maintenance_types AS mtct ON mtc.type_id = mtct.id" +
                    " WHERE " +
                    "(:typeName IS NULL OR mtct.name LIKE %:typeName%) AND " +
                    "(CAST(:period AS INTERVAL)IS NULL OR mtct.period = CAST(:period AS INTERVAL)) AND " +
                    "(:workDescription IS NULL OR mtc.work_description LIKE %:workDescription%)",
            countQuery = "SELECT COUNT(mtc.*) FROM maintenance AS mtc " +
                    "INNER JOIN maintenance_types AS mtct ON mtc.type_id = mtct.id" +
                    " WHERE " +
                    "(:typeName IS NULL OR mtct.name LIKE %:typeName%) AND " +
                    "(CAST(:period AS INTERVAL) IS NULL OR mtct.period = CAST(:period AS INTERVAL)) AND " +
                    "(:workDescription IS NULL OR mtc.work_description LIKE %:workDescription%)",
            nativeQuery = true
    )
    Page<Maintenance> findAllFiltered(
            @Param("typeName") String typeName,
            @Param("period") String period,
            @Param("workDescription") String workDescription,
            Pageable pageable
    );

    @Query(
            value = "SELECT mtc.* FROM maintenance AS mtc " +
                    "INNER JOIN maintenance_types AS mtct ON mtc.type_id = mtct.id" +
                    " WHERE " +
                    "(:typeId IS NULL OR mtct.id = :typeId) AND " +
                    "(CAST(:period AS INTERVAL) IS NULL OR mtct.period = CAST(:period AS INTERVAL)) AND " +
                    "(:workDescription IS NULL OR mtc.work_description LIKE %:workDescription%)",
            countQuery = "SELECT COUNT(mtc.*) FROM maintenance AS mtc " +
                    "INNER JOIN maintenance_types AS mtct ON mtc.type_id = mtct.id" +
                    " WHERE " +
                    "(:typeId IS NULL OR mtct.id = :typeId) AND " +
                    "(CAST(:period AS INTERVAL) IS NULL OR mtct.period = CAST(:period AS INTERVAL)) AND " +
                    "(:workDescription IS NULL OR mtc.work_description LIKE %:workDescription%)",
            nativeQuery = true
    )
    Page<Maintenance> findByTypeFiltered(
            @Param("typeId") Short typeId,
            @Param("period") String period,
            @Param("workDescription") String workDescription,
            Pageable pageable
    );
}
