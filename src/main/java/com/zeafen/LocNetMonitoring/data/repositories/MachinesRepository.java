package com.zeafen.LocNetMonitoring.data.repositories;

import com.zeafen.LocNetMonitoring.domain.models.entity.Machine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;

public interface MachinesRepository extends JpaRepository<Machine, Integer> {
    @Query(
            value = "SELECT * FROM machines AS m WHERE " +
                    "(:name IS NULL OR m.name LIKE %:name%) AND " +
                    "(:address IS NULL OR m.address LIKE %:address%) AND " +
                    "(CAST(:date_commissioning_from AS TIMESTAMP WITH TIME ZONE) IS NULL OR m.date_commissioning >= CAST(:date_commissioning_from AS TIMESTAMP WITH TIME ZONE)) AND " +
                    "(CAST(:date_commissioning_to AS TIMESTAMP WITH TIME ZONE) IS NULL OR m.date_commissioning <= CAST(:date_commissioning_to AS TIMESTAMP WITH TIME ZONE)) AND " +
                    "(:serialNumber IS NULL OR m.serial_number = :serialNumber)",
            countQuery = "SELECT COUNT(*) FROM machines AS m WHERE " +
                    "(:name IS NULL OR m.name LIKE %:name%) AND " +
                    "(:address IS NULL OR m.address LIKE %:address%) AND " +
                    "(CAST(:date_commissioning_from AS TIMESTAMP WITH TIME ZONE) IS NULL OR m.date_commissioning >= CAST(:date_commissioning_from AS TIMESTAMP WITH TIME ZONE)) AND " +
                    "(CAST(:date_commissioning_to AS TIMESTAMP WITH TIME ZONE) IS NULL OR m.date_commissioning <= CAST(:date_commissioning_to AS TIMESTAMP WITH TIME ZONE)) AND " +
                    "(:serialNumber IS NULL OR m.serial_number = :serialNumber)",
            nativeQuery = true
    )
    Page<Machine> findAllFiltered(
            @Param("name") String name,
            @Param("address") String address,
            @Param("date_commissioning_from") OffsetDateTime date_commissioning_from,
            @Param("date_commissioning_to") OffsetDateTime date_commissioning_to,
            @Param("serialNumber") Integer serialNumber,
            Pageable pageable
    );

    @Query(
            value = "SELECT m.* FROM machines AS m " +
                    "INNER JOIN machine_models AS mm ON m.model_id = mm.id " +
                    "INNER JOIN machine_types AS mt ON mm.type_id = mt.id " +
                    " WHERE " +
                    "(:typeID IS NULL OR mt.id = :typeID) AND " +
                    "(:name IS NULL OR m.name LIKE %:name%) AND " +
                    "(:address IS NULL OR m.address LIKE %:address%) AND " +
                    "(CAST(:date_commissioning_from AS TIMESTAMP WITH TIME ZONE) IS NULL OR m.date_commissioning >= CAST(:date_commissioning_from AS TIMESTAMP WITH TIME ZONE)) AND " +
                    "(CAST(:date_commissioning_to AS TIMESTAMP WITH TIME ZONE) IS NULL OR m.date_commissioning <= CAST(:date_commissioning_to AS TIMESTAMP WITH TIME ZONE)) AND " +
                    "(:serialNumber IS NULL OR m.serial_number = :serialNumber)",
            countQuery = "SELECT COUNT(m.*) FROM machines AS m " +
                    "INNER JOIN machine_models AS mm ON m.model_id = mm.id " +
                    "INNER JOIN machine_types AS mt ON mm.type_id = mt.id " +
                    " WHERE " +
                    "(:typeID IS NULL OR mt.id = :typeID) AND " +
                    "(:name IS NULL OR m.name LIKE %:name%) AND " +
                    "(:address IS NULL OR m.address LIKE %:address%) AND " +
                    "(CAST(:date_commissioning_from AS TIMESTAMP WITH TIME ZONE) IS NULL OR m.date_commissioning >= CAST(:date_commissioning_from AS TIMESTAMP WITH TIME ZONE)) AND " +
                    "(CAST(:date_commissioning_to AS TIMESTAMP WITH TIME ZONE) IS NULL OR m.date_commissioning <= CAST(:date_commissioning_to AS TIMESTAMP WITH TIME ZONE)) AND " +
                    "(:serialNumber IS NULL OR m.serial_number = :serialNumber)",
            nativeQuery = true
    )
    Page<Machine> findAllByTypeIdFiltered(
            @Param("typeID") Short typeID,
            @Param("name") String name,
            @Param("address") String address,
            @Param("date_commissioning_from") OffsetDateTime date_commissioning_from,
            @Param("date_commissioning_to") OffsetDateTime date_commissioning_to,
            @Param("serialNumber") Integer serialNumber,
            Pageable pageable
    );

    @Query(
            value = "SELECT m.* FROM machines AS m " +
                    "INNER JOIN machine_models AS mm ON m.model_id = mm.id " +
                    "INNER JOIN machine_types AS mt ON mm.type_id = mt.id " +
                    " WHERE " +
                    "(:typeName IS NULL OR mt.name LIKE %:typeName%) AND " +
                    "(:name IS NULL OR m.name LIKE %:name%) AND " +
                    "(:address IS NULL OR m.address LIKE %:address%) AND " +
                    "(CAST(:date_commissioning_from AS TIMESTAMP WITH TIME ZONE) IS NULL OR m.date_commissioning >= CAST(:date_commissioning_from AS TIMESTAMP WITH TIME ZONE)) AND " +
                    "(CAST(:date_commissioning_to AS TIMESTAMP WITH TIME ZONE) IS NULL OR m.date_commissioning <= CAST(:date_commissioning_to AS TIMESTAMP WITH TIME ZONE)) AND " +
                    "(:serialNumber IS NULL OR m.serial_number = :serialNumber)",
            countQuery = "SELECT COUNT(m.*) FROM machines AS m " +
                    "INNER JOIN machine_models AS mm ON m.model_id = mm.id " +
                    "INNER JOIN machine_types AS mt ON mm.type_id = mt.id " +
                    " WHERE " +
                    "(:typeName IS NULL OR mt.name LIKE %:typeName%) AND " +
                    "(:name IS NULL OR m.name LIKE %:name%) AND " +
                    "(:address IS NULL OR m.address LIKE %:address%) AND " +
                    "(CAST(:date_commissioning_from AS TIMESTAMP WITH TIME ZONE) IS NULL OR m.date_commissioning >= CAST(:date_commissioning_from AS TIMESTAMP WITH TIME ZONE)) AND " +
                    "(CAST(:date_commissioning_to AS TIMESTAMP WITH TIME ZONE) IS NULL OR m.date_commissioning <= CAST(:date_commissioning_to AS TIMESTAMP WITH TIME ZONE)) AND " +
                    "(:serialNumber IS NULL OR m.serial_number = :serialNumber)",
            nativeQuery = true
    )
    Page<Machine> findAllByTypeNameFiltered(
            @Param("typeName") String typeName,
            @Param("name") String name,
            @Param("address") String address,
            @Param("date_commissioning_from") OffsetDateTime date_commissioning_from,
            @Param("date_commissioning_to") OffsetDateTime date_commissioning_to,
            @Param("serialNumber") Integer serialNumber,
            Pageable pageable
    );

    @Query(
            value = "SELECT m.* FROM machines AS m " +
                    "INNER JOIN machine_models AS mm ON m.model_id = mm.id " +
                    " WHERE " +
                    "(:modelName IS NULL OR mm.name LIKE %:modelName%) AND " +
                    "(:name IS NULL OR m.name LIKE %:name%) AND " +
                    "(:address IS NULL OR m.address LIKE %:address%) AND " +
                    "(CAST(:date_commissioning_from AS TIMESTAMP WITH TIME ZONE) IS NULL OR m.date_commissioning >= CAST(:date_commissioning_from AS TIMESTAMP WITH TIME ZONE)) AND " +
                    "(CAST(:date_commissioning_to AS TIMESTAMP WITH TIME ZONE) IS NULL OR m.date_commissioning <= CAST(:date_commissioning_to AS TIMESTAMP WITH TIME ZONE)) AND " +
                    "(:serialNumber IS NULL OR m.serial_number = :serialNumber)",
            countQuery = "SELECT COUNT(m.*) FROM machines AS m " +
                    "INNER JOIN machine_models AS mm ON m.model_id = mm.id " +
                    " WHERE " +
                    "(:modelName IS NULL OR mm.name LIKE %:modelName%) AND " +
                    "(:name IS NULL OR m.name LIKE %:name%) AND " +
                    "(:address IS NULL OR m.address LIKE %:address%) AND " +
                    "(CAST(:date_commissioning_from AS TIMESTAMP WITH TIME ZONE) IS NULL OR m.date_commissioning >= CAST(:date_commissioning_from AS TIMESTAMP WITH TIME ZONE)) AND " +
                    "(CAST(:date_commissioning_to AS TIMESTAMP WITH TIME ZONE) IS NULL OR m.date_commissioning <= CAST(:date_commissioning_to AS TIMESTAMP WITH TIME ZONE)) AND " +
                    "(:serialNumber IS NULL OR m.serial_number = :serialNumber)",
            nativeQuery = true
    )
    Page<Machine> findAllByModelNameFiltered(
            @Param("modelName") String modelName,
            @Param("name") String name,
            @Param("address") String address,
            @Param("date_commissioning_from") OffsetDateTime date_commissioning_from,
            @Param("date_commissioning_to") OffsetDateTime date_commissioning_to,
            @Param("serialNumber") Integer serialNumber,
            Pageable pageable
    );

    @Query(
            value = "SELECT m.* FROM machines AS m WHERE " +
                    "(:modelID IS NULL OR m.model_id = :modelID) AND " +
                    "(:name IS NULL OR m.name LIKE %:name%) AND " +
                    "(:address IS NULL OR m.address LIKE %:address%) AND " +
                    "(CAST(:date_commissioning_from AS TIMESTAMP WITH TIME ZONE) IS NULL OR m.date_commissioning >= CAST(:date_commissioning_from AS TIMESTAMP WITH TIME ZONE)) AND " +
                    "(CAST(:date_commissioning_to AS TIMESTAMP WITH TIME ZONE) IS NULL OR m.date_commissioning <= CAST(:date_commissioning_to AS TIMESTAMP WITH TIME ZONE)) AND " +
                    "(:serialNumber IS NULL OR m.serial_number = :serialNumber)",
            countQuery = "SELECT COUNT(m.*) FROM machines AS m WHERE " +
                    "(:modelID IS NULL OR m.model_id = :modelID) AND " +
                    "(:name IS NULL OR m.name LIKE %:name%) AND " +
                    "(:address IS NULL OR m.address LIKE %:address%) AND " +
                    "(CAST(:date_commissioning_from AS TIMESTAMP WITH TIME ZONE) IS NULL OR m.date_commissioning >= CAST(:date_commissioning_from AS TIMESTAMP WITH TIME ZONE)) AND " +
                    "(CAST(:date_commissioning_to AS TIMESTAMP WITH TIME ZONE) IS NULL OR m.date_commissioning <= CAST(:date_commissioning_to AS TIMESTAMP WITH TIME ZONE)) AND " +
                    "(:serialNumber IS NULL OR m.serial_number = :serialNumber)",
            nativeQuery = true
    )
    Page<Machine> findAllByModelIdFiltered(
            @Param("modelID") Short modleID,
            @Param("name") String name,
            @Param("address") String address,
            @Param("date_commissioning_from") OffsetDateTime date_commissioning_from,
            @Param("date_commissioning_to") OffsetDateTime date_commissioning_to,
            @Param("serialNumber") Integer serialNumber,
            Pageable pageable
    );
}
