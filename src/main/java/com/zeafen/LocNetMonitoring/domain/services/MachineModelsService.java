package com.zeafen.LocNetMonitoring.domain.services;

import com.zeafen.LocNetMonitoring.domain.models.entity.MachineModel;
import com.zeafen.LocNetMonitoring.domain.models.entity.ModelStandard;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;

public interface MachineModelsService {
    Page<MachineModel> getMachineModels(
            int page, int perPage,
            @Nullable Short typeID,
            @Nullable String modelName
    );

    Page<MachineModel> getMachineModelsByTypeName(
            int page, int perPage,
            @Nullable String typeName,
            @Nullable String modelName
    );
    MachineModel getModelById(Short id);
    MachineModel saveMachineModel(MachineModel model);
    void deleteMachineModel(Short id);

    Page<ModelStandard> getModelStandards(
            int page, int perPage,
            @Nullable Short modelId,
            @Nullable String parameterName
    );
    ModelStandard getModelStandardById(Integer id);
    ModelStandard saveModelStandard(ModelStandard model);
    void deleteModelStandard(Integer id);
}
