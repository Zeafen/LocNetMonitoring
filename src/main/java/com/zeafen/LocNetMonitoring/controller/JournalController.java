package com.zeafen.LocNetMonitoring.controller;

import com.zeafen.LocNetMonitoring.domain.models.entity.JournalInfoView;
import com.zeafen.LocNetMonitoring.domain.models.entity.Machine;
import com.zeafen.LocNetMonitoring.domain.services.JournalService;
import com.zeafen.LocNetMonitoring.domain.services.MachinesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Controller
@PreAuthorize("hasAuthority('OPERATOR')")
@RequestMapping("/journal")
public class JournalController {
    @Autowired
    private JournalService _journalService;

    @Autowired
    private MachinesService _machinesService;

    @GetMapping
    public String getJournalRecords(
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "perPage", defaultValue = "10", required = false) Integer perPage,
            @RequestParam(name = "machineId", required = false) Integer machineId,
            @RequestParam(name = "machine", required = false) String machineName,
            @RequestParam(name = "requestType", required = false) String requestType,
            @RequestParam(name = "succeed", required = false) Boolean succeed,
            @RequestParam(name = "contentType", required = false) String contentType,
            @RequestParam(name = "codeNumber", required = false) Short codeNumber,
            @RequestParam(name = "outcoming", required = false) Boolean outcoming,
            @RequestParam(name = "sentFrom", required = false)
            LocalDateTime sentFrom,
            @RequestParam(name = "sentTo", required = false)
            LocalDateTime sentTo,
            @RequestParam(name = "orderByDate", required = false) Boolean orderByDateAscending,
            Model model
    ) {
        Page<JournalInfoView> journalInfoView = null;

        if (machineId != null) {
            Machine machine = _machinesService.getMachineByID(machineId);
            model.addAttribute("selectedMachine", machine);

            journalInfoView = _journalService.getJournalData(page, perPage,
                    machineId,
                    requestType,
                    succeed,
                    contentType,
                    codeNumber,
                    outcoming,
                    sentFrom == null ? null : sentFrom.atOffset(ZoneOffset.UTC),
                    sentTo == null ? null : sentTo.atOffset(ZoneOffset.UTC),
                    orderByDateAscending);
        }
        if (journalInfoView == null)
            journalInfoView = _journalService.getJournalData(page, perPage,
                    machineName,
                    requestType,
                    succeed,
                    contentType,
                    codeNumber,
                    outcoming,
                    sentFrom == null ? null : sentFrom.atOffset(ZoneOffset.UTC),
                    sentTo == null ? null : sentTo.atOffset(ZoneOffset.UTC),
                    orderByDateAscending);

        model.addAttribute("machineId", machineId);
        model.addAttribute("machineName", machineName);
        model.addAttribute("requestType", requestType);
        model.addAttribute("succeed", succeed);
        model.addAttribute("contentType", contentType);
        model.addAttribute("codeNumber", codeNumber);
        model.addAttribute("outcoming", outcoming);
        model.addAttribute("sentFrom", sentFrom);
        model.addAttribute("sentTo", sentTo);
        model.addAttribute("orderByDate", orderByDateAscending);
        model.addAttribute("journal", journalInfoView);
        return "journal/list";
    }
}
