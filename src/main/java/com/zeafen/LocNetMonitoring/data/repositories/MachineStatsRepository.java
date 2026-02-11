package com.zeafen.LocNetMonitoring.data.repositories;

import com.zeafen.LocNetMonitoring.domain.models.entity.MachineStatsId;
import com.zeafen.LocNetMonitoring.domain.models.entity.MachineStatsView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;

public interface MachineStatsRepository extends JpaRepository<MachineStatsView, MachineStatsId> {
    @Query(
            value = "SELECT * FROM device_stats as ds WHERE " +
                    "(:machineID IS NULL OR ds.id = :machineID) AND" +
                    "(CAST(:dayFrom AS TIMESTAMP WITH TIME ZONE) IS NULL OR ds.sent_time >= date_trunc('day', CAST(:dayFrom AS TIMESTAMP WITH TIME ZONE))) AND" +
                    "(CAST(:dayTo AS TIMESTAMP WITH TIME ZONE) IS NULL OR ds.sent_time <= date_trunc('day', CAST(:dayTo AS TIMESTAMP WITH TIME ZONE)))",
            countQuery = "SELECT COUNT(*) FROM device_stats as ds WHERE " +
                    "(:machineID IS NULL OR ds.id = :machineID) AND" +
                    "(CAST(:dayFrom AS TIMESTAMP WITH TIME ZONE) IS NULL OR ds.sent_time >= date_trunc('day', CAST(:dayFrom AS TIMESTAMP WITH TIME ZONE))) AND" +
                    "(CAST(:dayTo AS TIMESTAMP WITH TIME ZONE) IS NULL OR ds.sent_time <= date_trunc('day', CAST(:dayTo AS TIMESTAMP WITH TIME ZONE)))",
            nativeQuery = true
    )
    Page<MachineStatsView> findAllFiltered(
            @Param("machineID") Integer machineID,
            @Param("dayFrom") OffsetDateTime dayFrom,
            @Param("dayTo") OffsetDateTime dayTo,
            Pageable pageable
    );
}
