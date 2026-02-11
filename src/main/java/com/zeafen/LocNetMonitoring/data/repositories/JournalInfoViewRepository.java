package com.zeafen.LocNetMonitoring.data.repositories;

import com.zeafen.LocNetMonitoring.domain.models.entity.JournalInfoView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface JournalInfoViewRepository extends JpaRepository<JournalInfoView, UUID> {

    @Query(
            value = "SELECT * FROM journal_full_info AS j WHERE" +
                    "(:machineID IS NULL OR j.machine_id = :machineID) AND " +
                    "(:requestType IS NULL OR j.request_type LIKE %:requestType%) AND " +
                    "(:succeed IS NULL OR j.succeed = :succeed) AND " +
                    "(:contentType IS NULL OR j.content_type LIKE %:contentType%) AND " +
                    "(:codeNumber IS NULL OR j.code_number = :codeNumber) AND " +
                    "(:outcoming IS NULL OR j.outcoming = :outcoming) AND " +
                    "(CAST(:sentFrom AS TIMESTAMP WITH TIME ZONE) IS NULL OR j.sent_time >= CAST(:sentFrom AS TIMESTAMP WITH TIME ZONE)) AND " +
                    "(CAST(:sentTo AS TIMESTAMP WITH TIME ZONE) IS NULL OR j.sent_time <= CAST(:sentTo AS TIMESTAMP WITH TIME ZONE))",
            countQuery = "SELECT COUNT(*) FROM journal_full_info AS j WHERE" +
                    "(:machineID IS NULL OR j.machine_id = :machineID) AND " +
                    "(:requestType IS NULL OR j.request_type LIKE %:requestType%) AND " +
                    "(:succeed IS NULL OR j.succeed = :succeed) AND " +
                    "(:contentType IS NULL OR j.content_type LIKE %:contentType%) AND " +
                    "(:codeNumber IS NULL OR j.code_number = :codeNumber) AND " +
                    "(:outcoming IS NULL OR j.outcoming = :outcoming) AND " +
                    "(CAST(:sentFrom AS TIMESTAMP WITH TIME ZONE) IS NULL OR j.sent_time >= CAST(:sentFrom AS TIMESTAMP WITH TIME ZONE)) AND " +
                    "(CAST(:sentTo AS TIMESTAMP WITH TIME ZONE) IS NULL OR j.sent_time <= CAST(:sentTo AS TIMESTAMP WITH TIME ZONE))",
            nativeQuery = true
    )
    Page<JournalInfoView> findFilteredJournal(
            @Param("machineID") Integer machineID,
            @Param("requestType") String requestType,
            @Param("succeed") Boolean succeed,
            @Param("contentType") String contentType,
            @Param("codeNumber") Short codeNumber,
            @Param("outcoming") Boolean outcoming,
            @Param("sentFrom") OffsetDateTime sentFrom,
            @Param("sentTo") OffsetDateTime sentTo,
            Pageable pageable
    );

    @Query(
            value = "SELECT * FROM journal_full_info AS j WHERE" +
                    "(:machineName IS NULL OR j.machine_name LIKE %:machineName%) AND " +
                    "(:requestType IS NULL OR j.request_type LIKE %:requestType%) AND " +
                    "(:succeed IS NULL OR j.succeed = :succeed) AND " +
                    "(:contentType IS NULL OR j.content_type LIKE %:contentType%) AND " +
                    "(:codeNumber IS NULL OR j.code_number = :codeNumber) AND " +
                    "(:outcoming IS NULL OR j.outcoming = :outcoming) AND " +
                    "(CAST(:sentFrom AS TIMESTAMP WITH TIME ZONE) IS NULL OR j.sent_time >= CAST(:sentFrom AS TIMESTAMP WITH TIME ZONE)) AND " +
                    "(CAST(:sentTo AS TIMESTAMP WITH TIME ZONE) IS NULL OR j.sent_time <= CAST(:sentTo AS TIMESTAMP WITH TIME ZONE))",
            countQuery = "SELECT COUNT(*) FROM journal_full_info AS j WHERE" +
                    "(:machineName IS NULL OR j.machine_name LIKE %:machineName%) AND " +
                    "(:requestType IS NULL OR j.request_type LIKE %:requestType%) AND " +
                    "(:succeed IS NULL OR j.succeed = :succeed) AND " +
                    "(:contentType IS NULL OR j.content_type LIKE %:contentType%) AND " +
                    "(:codeNumber IS NULL OR j.code_number = :codeNumber) AND " +
                    "(:outcoming IS NULL OR j.outcoming = :outcoming) AND " +
                    "(CAST(:sentFrom AS TIMESTAMP WITH TIME ZONE) IS NULL OR j.sent_time >= CAST(:sentFrom AS TIMESTAMP WITH TIME ZONE)) AND " +
                    "(CAST(:sentTo AS TIMESTAMP WITH TIME ZONE) IS NULL OR j.sent_time <= CAST(:sentTo AS TIMESTAMP WITH TIME ZONE))",
            nativeQuery = true
    )
    Page<JournalInfoView> findFilteredJournal(
            @Param("machineName") String machineName,
            @Param("requestType") String requestType,
            @Param("succeed") Boolean succeed,
            @Param("contentType") String contentType,
            @Param("codeNumber") Short codeNumber,
            @Param("outcoming") Boolean outcoming,
            @Param("sentFrom") OffsetDateTime sentFrom,
            @Param("sentTo") OffsetDateTime sentTo,
            Pageable pageable
    );

}
