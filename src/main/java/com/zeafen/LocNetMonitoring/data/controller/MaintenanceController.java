package com.zeafen.LocNetMonitoring.data.controller;

import com.zeafen.LocNetMonitoring.domain.models.MachineUiModel;
import com.zeafen.LocNetMonitoring.domain.models.MaintenanceRecordUiModel;
import com.zeafen.LocNetMonitoring.domain.models.entity.Maintenance;
import com.zeafen.LocNetMonitoring.domain.models.entity.MaintenanceRecords;
import com.zeafen.LocNetMonitoring.domain.models.entity.MaintenanceType;
import com.zeafen.LocNetMonitoring.domain.services.MachinesService;
import com.zeafen.LocNetMonitoring.domain.services.MaintenanceService;
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

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/maintenance")
public class MaintenanceController {

    @Autowired
    private MaintenanceService maintenanceService;

    @Autowired
    private MachinesService machinesServices;

    /// Getting maintenance segment ///
    @GetMapping
    public String getMaintenances(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "perPage", defaultValue = "10", required = false) int perPage,
            @RequestParam(name = "type", required = false) Short typeId,
            @RequestParam(name = "maintenance", required = false) Integer maintenanceId,
            @RequestParam(name = "machine", required = false) Integer machineId,
            @RequestParam(name = "typeName", required = false) String typeName,
            @RequestParam(name = "period", required = false) String period,
            @RequestParam(name = "workDescription", required = false) String workDescription,
            Model model
    ) {
        Page<Maintenance> maintenance = typeId == null
                ? maintenanceService.getMaintenance(
                page, perPage,
                typeName,
                period,
                workDescription)
                : maintenanceService.getMaintenanceByType(
                page, perPage,
                typeId,
                period,
                workDescription);
        if (typeId != null)
            model.addAttribute("selectedType", maintenanceService.getMaintenanceTypeById(typeId));
        if (maintenanceId != null) {
            if (maintenanceId > 0)
                model.addAttribute("selectedMaintenance", maintenanceService.getMaintenanceById(maintenanceId));
            if (maintenanceId < 0 && model.getAttribute("selectedType") instanceof MaintenanceType selectedType) {
                Maintenance selectedMaintenance = new Maintenance();
                selectedMaintenance.setMaintenanceType(selectedType);
                model.addAttribute("selectedMaintenance", selectedMaintenance);
            }
        }


        model.addAttribute("maintenance", maintenance);
        model.addAttribute("machineId", machineId);
        model.addAttribute("typeName", typeName);
        model.addAttribute("period", period);
        model.addAttribute("workDescription", workDescription);
        return "maintenance/list";
    }

    @GetMapping("/types")
    public String getMaintenanceTypes(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "perPage", defaultValue = "10", required = false) int perPage,
            @RequestParam(name = "type", required = false) Short typeId,
            @RequestParam(name = "typeName", required = false) String typeName,
            @RequestParam(name = "period", required = false) String period,
            Model model
    ) {
        Page<MaintenanceType> types = maintenanceService.getMaintenanceTypes(
                page, perPage, typeName, period
        );

        if (typeId != null) {
            MaintenanceType type = null;
            if (typeId > 0)
                type = maintenanceService.getMaintenanceTypeById(typeId);
            if (type == null)
                type = new MaintenanceType();
            model.addAttribute("selectedType", type);
        }
        model.addAttribute("types", types);
        model.addAttribute("typeName", typeName);
        model.addAttribute("period", period);
        return "maintenance/types/list";
    }

    @GetMapping("/records")
    public String getMaintenancesRecords(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "perPage", defaultValue = "10", required = false) int perPage,
            @RequestParam(name = "machine", required = false) Integer machineId,
            @RequestParam(name = "maintenance", required = false) Integer maintenanceId,
            @RequestParam(name = "record", required = false) String recordId,
            @RequestParam(name = "status", required = false) Integer status,
            @RequestParam(name = "dateFrom", required = false) LocalDateTime dateFrom,
            @RequestParam(name = "dateUntil", required = false) LocalDateTime dateUntil,
            Model model
    ) {
        Page<MaintenanceRecordUiModel> records = maintenanceService.getMaintenanceRecords(
                        page, perPage,
                        machineId,
                        maintenanceId,
                        status,
                        dateFrom != null ? dateFrom.atOffset(ZoneOffset.UTC) : null,
                        dateUntil != null ? dateUntil.atOffset(ZoneOffset.UTC) : null)
                .map(MaintenanceRecords::toUiModel);

        //Identifying models
        if (machineId != null) {
            var selectedMachine = machinesServices.getMachineByID(machineId).totUiModel();
            model.addAttribute("selectedMachine", selectedMachine);
        }
        if (maintenanceId != null) {
            var selectedMaintenance = maintenanceService.getMaintenanceById(maintenanceId);
            model.addAttribute("selectedMaintenance", selectedMaintenance);
        }
        if (recordId != null) {
            MaintenanceRecordUiModel record = null;
            if (Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
                    .matcher(recordId).matches())
                record = maintenanceService.getMaintenanceRecordById(UUID.fromString(recordId)).toUiModel();
            if (record == null
                    && model.getAttribute("selectedMaintenance") instanceof Maintenance selectedMaintenance
                    && model.getAttribute("selectedMachine") instanceof MachineUiModel selectedMachine) {
                record = new MaintenanceRecordUiModel();
                record.setMaintenance(selectedMaintenance);
                record.setMachine(selectedMachine.toMachine());
            }
            model.addAttribute("selectedRecord", record);
        }

        model.addAttribute("records", records);
        model.addAttribute("status", status);
        model.addAttribute("dateFrom", dateFrom);
        model.addAttribute("dateUntil", dateUntil);
        return "maintenance/records/list";
    }

    /// Posting maintenance segment ///
    @PostMapping("/records/save")
    public String addMaintenanceRecord(
            @Valid @ModelAttribute MaintenanceRecordUiModel record,
            BindingResult bindingResult,
            RedirectAttributes attributes
    ) {
        if (bindingResult.hasErrors()) {
            attributes.addFlashAttribute("errorMessage", bindingResult.getAllErrors());
            return "redirect:/error";
        }

        maintenanceService.saveMaintenanceRecord(record.toEntity());
        return "redirect:/maintenance/records";
    }

    @PreAuthorize("hasAuthority('STUFF')")
    @PostMapping("/save")
    public String addMaintenance(
            @Valid @ModelAttribute Maintenance maintenance,
            BindingResult bindingResult,
            RedirectAttributes attributes
    ) {
        if (bindingResult.hasErrors()) {
            attributes.addFlashAttribute("errorMessage", bindingResult.getAllErrors());
            return "redirect:/error";
        }

        maintenanceService.saveMaintenance(maintenance);
        return "redirect:/maintenance/records";
    }

    @PreAuthorize("hasAuthority('STUFF')")
    @PostMapping("/types/save")
    public String addMaintenanceType(
            @Valid @ModelAttribute MaintenanceType type,
            BindingResult bindingResult,
            RedirectAttributes attributes
    ) {
        if (bindingResult.hasErrors()) {
            attributes.addFlashAttribute("errorMessage", bindingResult.getAllErrors());
            return "redirect:/error";
        }

        maintenanceService.saveMaintenanceType(type);
        return "redirect:/maintenance/types";
    }


    /// Deleting maintenance segment ///
    @PreAuthorize("hasAuthority('STUFF')")
    @GetMapping("/delete/{id}")
    public String deleteMaintenance(
            @PathVariable Integer id
    ) {
        Maintenance maintenance = maintenanceService.getMaintenanceById(id);

        if (maintenance == null)
            throw new EntityNotFoundException("Maintenance with id " + id.toString() + " was not found");
        maintenanceService.deleteMaintenance(id);
        return "redirect:/maintenance";
    }

    @GetMapping("/records/delete/{id}")
    public String deleteMaintenanceRecord(
            @PathVariable UUID id
    ) {
        MaintenanceRecords maintenance = maintenanceService.getMaintenanceRecordById(id);

        if (maintenance == null)
            throw new EntityNotFoundException("Maintenance record with id " + id.toString() + " was not found");
        maintenanceService.deleteMaintenanceRecord(id);
        return "redirect:/maintenance/records";
    }

    @GetMapping("/types/delete/{id}")
    @PreAuthorize("hasAuthority('STUFF')")
    public String deleteMaintenanceType(
            @PathVariable Short id
    ) {
        MaintenanceType type = maintenanceService.getMaintenanceTypeById(id);

        if (type == null)
            throw new EntityNotFoundException("Maintenance type with id " + id.toString() + " was not found");
        maintenanceService.deleteMaintenanceType(id);
        return "redirect:/maintenance/records";
    }
}
