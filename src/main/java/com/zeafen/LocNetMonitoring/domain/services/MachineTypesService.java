package com.zeafen.LocNetMonitoring.domain.services;

import com.zeafen.LocNetMonitoring.domain.models.entity.MachineType;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;

public interface MachineTypesService {
    Page<MachineType> getMachineTypes(
            int page,
            int perPage,
            @Nullable String name
    );
    MachineType getMachineTypeByID(Short id);
    MachineType saveMachineType(MachineType machineType);
    void deleteMachineType(Short id);
}
