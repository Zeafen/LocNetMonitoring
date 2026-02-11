package com.zeafen.LocNetMonitoring.data.repositories;

import com.zeafen.LocNetMonitoring.domain.models.entity.MachineType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MachineTypesRepository extends JpaRepository<MachineType, Short> {

    @Query(
            value = "SELECT * FROM machine_types AS mt WHERE " +
                    "(:name IS NULL OR mt.name LIKE %:name%)",
            countQuery = "SELECT COUNT(*) FROM machine_types AS mt WHERE " +
                    "(:name IS NULL OR mt.name LIKE %:name%)",
            nativeQuery = true
    )
    Page<MachineType> findAllByName(
            @Param("name") String name,
            Pageable pageable
    );
}
