package com.zeafen.LocNetMonitoring.controller;

import com.zeafen.LocNetMonitoring.domain.models.entity.MachineModel;
import com.zeafen.LocNetMonitoring.domain.models.entity.MachineType;
import com.zeafen.LocNetMonitoring.domain.models.entity.ModelStandard;
import com.zeafen.LocNetMonitoring.domain.services.MachineModelsService;
import com.zeafen.LocNetMonitoring.domain.services.MachineTypesService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/models")
public class MachineModelsController {
    @Autowired
    private MachineTypesService machineTypes;
    @Autowired
    private MachineModelsService modelsService;


    ///Getting models segment ///
    @GetMapping
    public String getModels(
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "perPage", defaultValue = "10", required = false) Integer perPage,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "typeId", required = false) Short typeId,
            @RequestParam(name = "typeName", required = false) String typeName,
            @RequestParam(name = "selectedModel", required = false) Short selectedModelId,
            Model model
    ) {
        Page<MachineModel> models = typeId == null && (typeName != null && !typeName.isBlank())
                ? modelsService.getMachineModelsByTypeName(page, perPage, typeName, name)
                : modelsService.getMachineModels(page, perPage, typeId, name);
        MachineType selectedType = null;
        var selectedTypes = machineTypes.getMachineTypes(0, 10, null);

        if (typeId != null)
            selectedType = machineTypes.getMachineTypeByID(typeId);

        if (selectedModelId != null) {
            MachineModel selectedModel = null;
            if (selectedModelId > 0)
                selectedModel = modelsService.getModelById(selectedModelId);
            if (selectedModel == null && selectedType != null) {
                selectedModel = new MachineModel();
                selectedModel.setType(selectedType);
            }
            model.addAttribute("modelModel", selectedModel);
        }
        model.addAttribute("models", models);
        model.addAttribute("name", name);
        model.addAttribute("typeName", typeName);
        model.addAttribute("selectedType", selectedType);

        return "models/list";
    }


    @PreAuthorize("hasAnyAuthority('OPERATOR')")
    @GetMapping("/{id}")
    public String getModelInfo(
            @PathVariable(name = "id") Short modelId,
            @RequestParam(name = "st_page", required = false, defaultValue = "0") Integer standardsPage,
            @RequestParam(name = "st_perPage", required = false, defaultValue = "10") Integer standardsPerPage,
            @RequestParam(name = "st_parameterName", required = false) String standardsParameterName,
            @RequestParam(name = "st_selected", required = false) Integer selectedStandard,
            Model model
    ) {
        MachineModel machineModel = modelsService.getModelById(modelId);
        if (machineModel == null)
            throw new EntityNotFoundException("Model with id" + modelId + " was not found!");
        Page<ModelStandard> standards = modelsService.getModelStandards(standardsPage, standardsPerPage, modelId, standardsParameterName);

        Map<String, Object> attributes = new HashMap<>();
        if (selectedStandard != null) {
            ModelStandard standard = null;
            if (selectedStandard > 0)
                standard = modelsService.getModelStandardById(selectedStandard);
            if (standard == null) {
                standard = new ModelStandard();
                standard.setModel(machineModel);
            }
            attributes.put("standardModel", standard);
        }
        attributes.put("modelModel", machineModel);
        attributes.put("standards", standards);
        attributes.put("st_parameterName", standardsParameterName);

        model.addAllAttributes(attributes);
        return "models/info";
    }


    /// Adding standard segment ///
    @PreAuthorize("hasAnyAuthority('OPERATOR')")
    @PostMapping("/standards/save")
    public String addModelStandard(
            @Valid @ModelAttribute ModelStandard standard,
            BindingResult bindingResult,
            RedirectAttributes attributes
    ) {
        if (bindingResult.hasErrors()) {
            attributes.addFlashAttribute("errorMessage", bindingResult.getAllErrors());
            return "redirect:/error";
        }

        modelsService.saveModelStandard(standard);
        return "redirect:/models/" + standard.getModel().getId();
    }


    ///Deleting model standard segment
    @PreAuthorize("hasAnyAuthority('OPERATOR')")
    @GetMapping("/standards/delete/{id}")
    public String deleteModelStandard(
            @PathVariable(name = "id") Integer standardId
    ) {
        ModelStandard standard = modelsService.getModelStandardById(standardId);
        if (standard == null)
            throw new EntityNotFoundException("Standard with id " + standardId + " was not found");

        modelsService.deleteModelStandard(standardId);
        return "redirect:/models/" + standard.getModel().getId();
    }


    ///Adding models segment ///
    @PreAuthorize("hasAnyAuthority('STUFF')")
    @PostMapping("/save")
    public String addModel(
            @Valid @ModelAttribute MachineModel model,
            BindingResult bindingResult,
            RedirectAttributes attributes
    ) {
        if (bindingResult.hasErrors()) {
            attributes.addFlashAttribute("errorMessage", bindingResult.getAllErrors());
            return "redirect:/error";
        }

        modelsService.saveMachineModel(model);
        return "redirect:/models";
    }


    ///Deleting model segment
    @PreAuthorize("hasAnyAuthority('STUFF')")
    @GetMapping("/delete/{id}")
    public String deleteModel(
            @PathVariable Short id
    ) {
        MachineModel model = modelsService.getModelById(id);

        if (model == null)
            throw new EntityNotFoundException("Model with id " + id.toString() + " was not found");
        modelsService.deleteMachineModel(id);
        return "redirect:/models";
    }
}
