package com.zeafen.LocNetMonitoring.data.services;

import com.zeafen.LocNetMonitoring.data.repositories.MachineModelsRepository;
import com.zeafen.LocNetMonitoring.data.repositories.ModelStandardsRepository;
import com.zeafen.LocNetMonitoring.domain.models.entity.MachineModel;
import com.zeafen.LocNetMonitoring.domain.models.entity.ModelStandard;
import com.zeafen.LocNetMonitoring.domain.services.MachineModelsService;
import org.jspecify.annotations.Nullable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class JpaMachineModelsService implements MachineModelsService {
    private final MachineModelsRepository _machineModels;
    private final ModelStandardsRepository _modelStandards;

    public JpaMachineModelsService(MachineModelsRepository machineModels, ModelStandardsRepository modelStandards) {
        _machineModels = machineModels;
        _modelStandards = modelStandards;
    }

    @Override
    public Page<MachineModel> getMachineModels(int page, int perPage, @Nullable Short typeID, @Nullable String modelName) {
        return _machineModels.findAllFiltered(
                typeID,
                modelName == null || modelName.isBlank() ? null : modelName,
                PageRequest.of(page, perPage));
    }

    @Override
    public Page<MachineModel> getMachineModelsByTypeName(int page, int perPage, @Nullable String typeName, @Nullable String modelName) {
        return _machineModels.findAllByTypeNameFiltered(
                typeName == null || typeName.isBlank() ? null : typeName,
                modelName == null || modelName.isBlank() ? null : modelName,
                PageRequest.of(page, perPage));
    }

    @CacheEvict(
            value = "machineModel",
            key = "#id"
    )
    @Override
    public MachineModel getModelById(Short id) {
        return _machineModels.findById(id).orElse(null);
    }

    @Override
    public MachineModel saveMachineModel(MachineModel model) {
        return _machineModels.save(model);
    }

    @Override
    public void deleteMachineModel(Short id) {
        _machineModels.deleteById(id);
    }

    @CacheEvict(value = "standards")
    @Override
    public Page<ModelStandard> getModelStandards(int page, int perPage, @Nullable Short modelId, @Nullable String parameterName) {
        return _modelStandards.findAllFiltered(
                modelId,
                parameterName == null || parameterName.isBlank() ? null : parameterName,
                PageRequest.of(page, perPage));
    }

    @CacheEvict(
            value = "standard",
            key = "#id"
    )
    @Override
    public ModelStandard getModelStandardById(Integer id) {
        return _modelStandards.findById(id).orElse(null);
    }

    @Override
    public ModelStandard saveModelStandard(ModelStandard model) {
        return _modelStandards.save(model);
    }

    @Override
    public void deleteModelStandard(Integer id) {
        _modelStandards.deleteById(id);
    }
}
