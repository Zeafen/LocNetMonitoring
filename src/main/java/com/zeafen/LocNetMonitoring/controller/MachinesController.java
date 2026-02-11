package com.zeafen.LocNetMonitoring.controller;

import com.zeafen.LocNetMonitoring.domain.RoleNotAllowedException;
import com.zeafen.LocNetMonitoring.domain.models.*;
import com.zeafen.LocNetMonitoring.domain.models.entity.Machine;
import com.zeafen.LocNetMonitoring.domain.models.entity.MachineMaintenanceSummary;
import com.zeafen.LocNetMonitoring.domain.models.entity.MachineModel;
import com.zeafen.LocNetMonitoring.domain.models.entity.ModelStandard;
import com.zeafen.LocNetMonitoring.domain.services.MachineModelsService;
import com.zeafen.LocNetMonitoring.domain.services.MachineTypesService;
import com.zeafen.LocNetMonitoring.domain.services.MachinesService;
import com.zeafen.LocNetMonitoring.domain.services.MaintenanceService;
import com.zeafen.LocNetMonitoring.domain.stub.UserRole;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.*;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/machines")
public class MachinesController {
    @Autowired
    private MachinesService _machinesService;
    @Autowired
    private MachineTypesService _machineTypesService;
    @Autowired
    private MachineModelsService _modelsService;
    @Autowired
    private MaintenanceService _maintenanceService;


    @GetMapping
    public String getMachinesList(
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "perPage", defaultValue = "10", required = false) Integer perPage,
            @RequestParam(name = "selectedMachine", required = false) Integer selectedMachineId,
            @RequestParam(name = "model", required = false) Short modelId,
            @RequestParam(name = "modelName", required = false) String modelName,
            @RequestParam(name = "type", required = false) Short typeId,
            @RequestParam(name = "typeName", required = false) String typeName,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "address", required = false) String address,
            @RequestParam(name = "serialNumber", required = false) Integer serialNumber,
            @RequestParam(name = "commissionedFrom", required = false)
            LocalDateTime dateCommissionedFrom,
            @RequestParam(name = "commissionedUntil", required = false)
            LocalDateTime dateCommissionedUntil,
            Model model
    ) {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        Page<Machine> machines;
        if (modelId != null) {
            machines = _machinesService.getMachinesByModel(page, perPage,
                    modelId,
                    name,
                    address,
                    dateCommissionedFrom == null ? null : dateCommissionedFrom.atOffset(ZoneOffset.UTC),
                    dateCommissionedUntil == null ? null : dateCommissionedUntil.atOffset(ZoneOffset.UTC),
                    serialNumber);
            model.addAttribute("selectedModel", _modelsService.getModelById(modelId));
        } else if (typeId != null) {
            machines = _machinesService.getMachinesByType(page, perPage,
                    typeId,
                    name,
                    address,
                    dateCommissionedFrom == null ? null : dateCommissionedFrom.atOffset(ZoneOffset.UTC),
                    dateCommissionedUntil == null ? null : dateCommissionedUntil.atOffset(ZoneOffset.UTC),
                    serialNumber);
            model.addAttribute("selectedType", _machineTypesService.getMachineTypeByID(typeId));
        } else {
            machines = modelName != null && !modelName.isBlank()
                    ? _machinesService.getMachinesByModel(page, perPage,
                    modelName,
                    name,
                    address,
                    dateCommissionedFrom == null ? null : dateCommissionedFrom.atOffset(ZoneOffset.UTC),
                    dateCommissionedUntil == null ? null : dateCommissionedUntil.atOffset(ZoneOffset.UTC),
                    serialNumber)
                    : (typeName != null && !typeName.isBlank()
                    ? _machinesService.getMachinesByType(page, perPage,
                    typeName,
                    name,
                    address,
                    dateCommissionedFrom == null ? null : dateCommissionedFrom.atOffset(ZoneOffset.UTC),
                    dateCommissionedUntil == null ? null : dateCommissionedUntil.atOffset(ZoneOffset.UTC),
                    serialNumber)
                    : _machinesService.getMachines(page, perPage,
                    name,
                    address,
                    dateCommissionedFrom == null ? null : dateCommissionedFrom.atOffset(ZoneOffset.UTC),
                    dateCommissionedUntil == null ? null : dateCommissionedUntil.atOffset(ZoneOffset.UTC),
                    serialNumber));
        }

        if (selectedMachineId != null && modelId != null) {
            Machine selectedMachine = null;
            if (selectedMachineId > 0)
                selectedMachine = _machinesService.getMachineByID(selectedMachineId);
            if (selectedMachine == null && auth != null &&
                    auth.getAuthorities().stream().anyMatch(ath -> Objects.equals(ath, UserRole.STUFF))) {
                selectedMachine = new Machine();
                selectedMachine.setModel((MachineModel) model.getAttribute("selectedModel"));
            }
            model.addAttribute("machineModel", selectedMachine != null ? selectedMachine.totUiModel() : null);
        }
        model.addAttribute("machines", machines);
        model.addAttribute("name", name);
        model.addAttribute("typeName", typeName);
        model.addAttribute("modelName", modelName);
        model.addAttribute("address", address);
        model.addAttribute("serialNumber", serialNumber);
        model.addAttribute("commissionedFrom", dateCommissionedFrom);
        model.addAttribute("commissionedUntil", dateCommissionedUntil);

        return "machines/list";
    }


    @GetMapping("/{id}")
    public String getMachineInfo(
            @PathVariable(name = "id") Integer id,
            @RequestParam(name = "s_page", required = false, defaultValue = "0") int statsPage,
            @RequestParam(name = "s_perPage", required = false, defaultValue = "10") int statsPerPage,
            @RequestParam(name = "s_fromDay", required = false)
            LocalDate statsFrom,
            @RequestParam(name = "s_untilDay", required = false)
            LocalDate statsUntil,
            @RequestParam(name = "mtc_page", required = false, defaultValue = "0") int maintenancePage,
            @RequestParam(name = "mtc_perPage", required = false, defaultValue = "10") int maintenancePerPage,
            Model model
    ) {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        Machine machineInfo = _machinesService.getMachineByID(id);
        if (machineInfo == null)
            throw new EntityNotFoundException("Machine with id " + id + " was not found");

        if (auth != null && auth.getAuthorities().stream()
                .anyMatch(authority ->
                        Objects.equals(authority.getAuthority(), UserRole.OPERATOR.getAuthority()))) {
            List<ModelStandard> standards = _modelsService.getModelStandards(1, Integer.MAX_VALUE, machineInfo.getModel().getId(), null).get().toList();
            var machineStats = _machinesService.getMachineStats(statsPage, statsPerPage,
                    id,
                    statsFrom == null ? null : statsFrom.atTime(OffsetTime.of(LocalTime.MIN, ZoneOffset.UTC)),
                    statsUntil == null ? null : statsUntil.atTime(OffsetTime.of(LocalTime.MAX, ZoneOffset.UTC))
            ).map(stats -> stats.compareToStandards(standards));
            model.addAttribute("stats", machineStats);
            model.addAttribute("s_fromDay", statsFrom);
            model.addAttribute("s_untilDay", statsUntil);
        }

        Page<MachineMaintenanceSummary> summary = _maintenanceService.getMaintenanceSummary(maintenancePage, maintenancePerPage, id, null, null, null);

        model.addAttribute("machineModel", machineInfo);
        model.addAttribute("maintenanceSummary", summary);

        return "machines/info";
    }


    //Adding machine segment
    @PreAuthorize("hasAnyAuthority('OPERATOR', 'STUFF')")
    @PostMapping("/save")
    public String addMachine(
            @Valid @ModelAttribute(name = "machine") MachineUiModel machine,
            BindingResult bindingResult,
            RedirectAttributes attributes
    ) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (machine.getId() == null && (auth != null && auth.getAuthorities().stream().anyMatch(authority -> UserRole.OPERATOR.getAuthority().equals(authority.getAuthority()))))
            throw new RoleNotAllowedException("OPERATOR", "add machine");

        if (bindingResult.hasErrors()) {
            attributes.addFlashAttribute("errorMessage", bindingResult.getAllErrors());
            return "redirect:/error";
        }

        _machinesService.saveMachine(machine.toMachine());
        return "redirect:/machines";
    }

    //Deleting machine segment
    @GetMapping("/delete/{id}")
    public String deleteMachine(
            @PathVariable Integer id
    ) {
        Machine machine = _machinesService.getMachineByID(id);

        if (machine == null)
            throw new EntityNotFoundException("Machine with id " + id.toString() + " was not found");
        _machinesService.deleteMachine(id);
        return "redirect:/machines";
    }
}
