package com.zeafen.LocNetMonitoring.data.repositories;

import com.zeafen.LocNetMonitoring.domain.models.entity.RequestCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RequestCodesRepository extends JpaRepository<RequestCode, Short> {
    @Query(
            value = "SELECT * FROM request_codes AS rc WHERE " +
                    "(:typeId IS NULL OR rc.type_id = :typeId) AND " +
                    "(:text IS NULL OR rc.text LIKE %:text%) AND " +
                    "(:isSucceed IS NULL OR rc.succeed = :isSucceed) AND " +
                    "(:code_number IS NULL OR rc.code_number = :code_number)",
            countQuery = "SELECT COUNT(*) FROM request_codes AS rc WHERE " +
                    "(:typeId IS NULL OR rc.type_id = :typeId) AND " +
                    "(:text IS NULL OR rc.text LIKE %:text%) AND " +
                    "(:isSucceed IS NULL OR rc.succeed = :isSucceed) AND " +
                    "(:code_number IS NULL OR rc.code_number = :code_number)",
            nativeQuery = true
    )
    Page<RequestCode> findAllFiltered(
            @Param("typeId") Short typeId,
            @Param("text") String text,
            @Param("isSucceed") Boolean isSucceed,
            @Param("code_number") Short code_number,
            Pageable pageable
    );
}
