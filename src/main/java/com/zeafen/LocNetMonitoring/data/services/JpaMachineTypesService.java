package com.zeafen.LocNetMonitoring.data.services;

import com.zeafen.LocNetMonitoring.data.repositories.MachineTypesRepository;
import com.zeafen.LocNetMonitoring.domain.models.entity.MachineType;
import com.zeafen.LocNetMonitoring.domain.services.MachineTypesService;
import org.jspecify.annotations.Nullable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
public class JpaMachineTypesService implements MachineTypesService {
    private final MachineTypesRepository _machineTypes;

    public JpaMachineTypesService(MachineTypesRepository machineTypes) {
        _machineTypes = machineTypes;
    }


    @CacheEvict(
            key = "machineTypes",
            allEntries = true
    )
    @Override
    public Page<MachineType> getMachineTypes(int page, int perPage, @Nullable String name) {
        return _machineTypes.findAllByName(
                name == null || name.isBlank() ? null : name,
                PageRequest.of(page, perPage));
    }

    @CacheEvict(
            key = "machineType",
            allEntries = true
    )
    @Override
    public MachineType getMachineTypeByID(Short id) {
        return _machineTypes.findById(id).orElse(null);
    }

    @Override
    public MachineType saveMachineType(MachineType machineType) {
        return _machineTypes.save(machineType);
    }

    @Override
    public void deleteMachineType(Short id) {
        _machineTypes.deleteById(id);
    }
}
