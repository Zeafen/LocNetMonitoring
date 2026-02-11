package com.zeafen.LocNetMonitoring.data.repositories;

import com.zeafen.LocNetMonitoring.domain.models.entity.MachineModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MachineModelsRepository extends JpaRepository<MachineModel, Short> {
    @Query(
            value = "SELECT * FROM machine_models AS mm WHERE " +
                    "(:typeID IS NULL OR mm.type_id = :typeID) AND " +
                    "(:modelName IS NULL OR mm.name LIKE %:modelName%)",
            countQuery = "SELECT COUNT(*) FROM machine_models AS mm WHERE " +
                    "(:typeID IS NULL OR mm.type_id = :typeID) AND " +
                    "(:modelName IS NULL OR mm.name LIKE %:modelName%)",
            nativeQuery = true
    )
    Page<MachineModel> findAllFiltered(
            @Param("typeID") Short typeID,
            @Param("modelName") String modelName,
            Pageable pageable
    );

    @Query(
            value = "SELECT mm.* FROM machine_models AS mm" +
                    "INNER JOIN machine_types AS mt ON mt.id = mm.type_id" +
                    " WHERE " +
                    "(:typeName IS NULL OR mt.name LIKE %:typeName%) AND " +
                    "(:modelName IS NULL OR mm.name LIKE %:modelName%)",
            countQuery = "SELECT count(mm.*) FROM machine_models AS mm" +
                    "INNER JOIN machine_types AS mt ON mt.id = mm.type_id" +
                    " WHERE " +
                    "(:typeName IS NULL OR mt.name LIKE %:typeName%) AND " +
                    "(:modelName IS NULL OR mm.name LIKE %:modelName%)",
            nativeQuery = true
    )
    Page<MachineModel> findAllByTypeNameFiltered(
            @Param("typeName") String typeName,
            @Param("modelName") String modelName,
            Pageable pageable
    );
}
