package com.zeafen.LocNetMonitoring.data.repositories;

import com.zeafen.LocNetMonitoring.domain.models.entity.ModelStandard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ModelStandardsRepository extends JpaRepository<ModelStandard, Integer> {
    @Query(
            value = "SELECT * FROM model_standards AS mms WHERE " +
                    "(:modelId IS NULL OR mms.model_id = :modelId) AND " +
                    "(:parameterName IS NULL OR mms.parameter_name LIKE %:parameterName%)",
            countQuery = "SELECT * FROM model_standards AS mms WHERE " +
                    "(:modelId IS NULL OR mms.model_id = :modelId) AND " +
                    "(:parameterName IS NULL OR mms.parameter_name LIKE %:parameterName%)",
            nativeQuery = true
    )
    Page<ModelStandard> findAllFiltered(
            @Param("modelId") Short modelId,
            @Param("parameterName") String parameterName,
            Pageable pageable
    );
}
