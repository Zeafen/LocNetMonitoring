package com.zeafen.LocNetMonitoring.controller;

import com.zeafen.LocNetMonitoring.domain.models.BufferType;
import com.zeafen.LocNetMonitoring.domain.models.entity.Buffer;
import com.zeafen.LocNetMonitoring.domain.services.BufferService;
import com.zeafen.LocNetMonitoring.domain.services.MachinesService;
import com.zeafen.LocNetMonitoring.domain.services.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;

@Controller
public class HomePageController {
    @Autowired
    private BufferService bufferService;
    @Autowired
    private MachinesService machinesService;
    @Autowired
    private MaintenanceService maintenanceService;

    /**
     * Processes get request for home page (buffer list)
     * @param page current page (actual page -1)
     * @param perPage page size
     * @param machineId selected machine identifier
     * @param maintenanceId selected maintenance identifier
     * @param dateFrom date generated from
     * @param dateUntil date generated until
     * @param isRead is buffer read
     * @param bufferType buffer type
     * @param model page model
     * @return route to home page
     */
    @GetMapping
    public String getHomePage(
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "perPage", defaultValue = "10", required = false) Integer perPage,
            @RequestParam(name = "machine", required = false) Integer machineId,
            @RequestParam(name = "maintenance", required = false) Integer maintenanceId,
            @RequestParam(name = "dateFrom", required = false) LocalDate dateFrom,
            @RequestParam(name = "dateUntil", required = false) LocalDate dateUntil,
            @RequestParam(name = "isRead", required = false, defaultValue = "false") Boolean isRead,
            @RequestParam(name = "type", required = false) Short bufferType,
            Model model) {
        Page<Buffer> buffer = bufferService.getBufferFiltered(page, perPage,
                machineId, maintenanceId,
                dateFrom == null
                        ? null
                        : dateFrom.atTime(OffsetTime.of(LocalTime.MIN, ZoneOffset.UTC)),
                dateUntil == null
                        ? null
                        : dateUntil.atTime(OffsetTime.of(LocalTime.MAX, ZoneOffset.UTC)),
                isRead, bufferType);

        ///appending buffer types
        model.addAttribute("bufferTypes", BufferType.values());
        for (var type : BufferType.values()) {
            model.addAttribute(type.getParameterName() + "Count",
                    bufferService.getBufferTypeUnreadCount((short) type.ordinal()));
        }

        if (machineId != null)
            model.addAttribute("selectedMachine", machinesService.getMachineByID(machineId));
        if (maintenanceId != null)
            model.addAttribute("selectedMaintenance", maintenanceService.getMaintenanceById(maintenanceId));
        model.addAttribute("buffer", buffer);
        model.addAttribute("dateFrom", dateFrom);
        model.addAttribute("dateUntil", dateUntil);
        model.addAttribute("isRead", isRead);
        model.addAttribute("bufferType", bufferType);

        return "home";
    }
}
