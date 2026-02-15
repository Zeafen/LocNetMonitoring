package com.zeafen.LocNetMonitoring.controller;


import com.zeafen.LocNetMonitoring.domain.models.entity.MachineType;
import com.zeafen.LocNetMonitoring.domain.models.entity.RequestCode;
import com.zeafen.LocNetMonitoring.domain.services.MachineTypesService;
import com.zeafen.LocNetMonitoring.domain.services.RequestCodesService;
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
@RequestMapping("/types")
public class MachineTypesController {
    @Autowired
    private MachineTypesService machineTypes;

    @Autowired
    private RequestCodesService requestCodesService;

    /// Machine types segment ///
    @GetMapping
    public String getMachineTypes(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") int perPage,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "selectedType", required = false) Short selectedTypeId,
            Model model
    ) {
        Page<MachineType> types = machineTypes.getMachineTypes(page, perPage, name);

        if (selectedTypeId != null) {
            MachineType type = null;
            if (selectedTypeId > 0)
                type = machineTypes.getMachineTypeByID(selectedTypeId);
            if (type == null)
                type = new MachineType();

            model.addAttribute("typeModel", type);
        }
        model.addAttribute("types", types);
        model.addAttribute("name", name);
        return "machineTypes/list";
    }

    @PreAuthorize("hasAuthority('STUFF')")
    @GetMapping("/{id}")
    public String getMachineTypeInfo(
            @PathVariable(name = "id") Short id,
            @RequestParam(name = "rc_page", required = false, defaultValue = "0") int rc_page,
            @RequestParam(name = "rc_perPage", required = false, defaultValue = "10") int rc_perPage,
            @RequestParam(name = "rc_text", required = false) String rc_text,
            @RequestParam(name = "rc_succeed", required = false) Boolean rc_succeed,
            @RequestParam(name = "rc_codeNumber", required = false) Short rc_code_number,
            @RequestParam(name = "rc_selected", required = false) Short rc_selected,
            Model model
    ) {
        MachineType type = machineTypes.getMachineTypeByID(id);
        if (type == null)
            throw new EntityNotFoundException("Machine type with id " + id.toString() + " was not found!");
        Page<RequestCode> typeCodes = requestCodesService.getRequestCodes(
                rc_page,
                rc_perPage,
                id,
                rc_text,
                rc_succeed,
                rc_code_number);

        Map<String, Object> attributes = new HashMap<>();
        if (rc_selected != null) {
            RequestCode code = null;
            if (rc_selected > 0)
                code = requestCodesService.getRequestCodeByID(rc_selected);
            if (code == null) {
                code = new RequestCode();
                code.setType(type);
            }
            attributes.put("codeModel", code);
        }
        attributes.put("rc_text", rc_text);
        attributes.put("rc_succeed", rc_succeed);
        attributes.put("rc_codeNumber", rc_code_number);
        attributes.put("typeModel", type);
        attributes.put("requestCodes", typeCodes);

        model.addAllAttributes(attributes);
        return "machineTypes/info";
    }


    //Adding machine type segment
    @PreAuthorize("hasAuthority('STUFF')")
    @PostMapping("/save")
    public String addMachineType(
            @Valid @ModelAttribute MachineType type,
            BindingResult bindingResult,
            RedirectAttributes attributes
    ) {
        if (bindingResult.hasErrors()) {
            attributes.addFlashAttribute("errorMessage", bindingResult.getAllErrors());
            return "redirect:/error";
        }

        machineTypes.saveMachineType(type);
        return "redirect:/types";
    }

    //Deleting machine type segment
    @PreAuthorize("hasAuthority('STUFF')")
    @GetMapping("/delete/{id}")
    public String deleteMachine(
            @PathVariable Short id
    ) {
        MachineType machine = machineTypes.getMachineTypeByID(id);

        if (machine == null)
            throw new EntityNotFoundException("Machine with id " + id.toString() + " was not found");
        machineTypes.deleteMachineType(id);
        return "redirect:/machines";
    }


    ///  Request codes segment ///
    //adding codes segment
    @PreAuthorize("hasAuthority('STUFF')")
    @PostMapping("/codes/save")
    public String addCodeToMachineType(
            @Valid @ModelAttribute RequestCode code,
            BindingResult bindingResult,
            RedirectAttributes attributes
    ) {
        if (bindingResult.hasErrors()) {
            attributes.addFlashAttribute("errorMessage", bindingResult.getAllErrors());
            return "redirect:/error";
        }

        requestCodesService.saveRequestCode(code);
        return "redirect:/types/" + code.getType().getId().toString();
    }

    //Deleting request code segment
    @PreAuthorize("hasAuthority('STUFF')")
    @GetMapping("/codes/delete/{id}")
    public String deleteRequestCode(
            @PathVariable Short id
    ) {
        RequestCode machine = requestCodesService.getRequestCodeByID(id);

        if (machine == null)
            throw new EntityNotFoundException("Machine with id " + id.toString() + " was not found");
        requestCodesService.deleteRequestCode(id);
        return "redirect:/machines";
    }
}
