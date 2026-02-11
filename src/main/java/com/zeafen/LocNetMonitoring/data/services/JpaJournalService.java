package com.zeafen.LocNetMonitoring.data.services;

import com.zeafen.LocNetMonitoring.data.repositories.JournalInfoViewRepository;
import com.zeafen.LocNetMonitoring.domain.models.entity.JournalInfoView;
import com.zeafen.LocNetMonitoring.domain.services.JournalService;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class JpaJournalService implements JournalService {
    private final JournalInfoViewRepository _journal;

    public JpaJournalService(JournalInfoViewRepository journal) {
        _journal = journal;
    }

    @Override
    public Page<JournalInfoView> getJournalData(
            int page, int perPage,
            @Nullable Integer machineID,
            @Nullable String requestType,
            @Nullable Boolean succeed,
            @Nullable String contentType,
            @Nullable Short codeNumber,
            @Nullable Boolean outcoming,
            @Nullable OffsetDateTime sentFrom,
            @Nullable OffsetDateTime sentTo,
            @Nullable Boolean orderByDateAscending) {
        Pageable pageable = orderByDateAscending == null ?
                PageRequest.of(page, perPage)
                : PageRequest.of(page, perPage, orderByDateAscending ? Sort.Direction.ASC : Sort.Direction.DESC, "sent_time");
        return _journal.findFilteredJournal(
                machineID,
                requestType == null || requestType.isBlank() ? null : requestType,
                succeed,
                contentType == null || contentType.isBlank() ? null : contentType,
                codeNumber,
                outcoming,
                sentFrom,
                sentTo,
                pageable);
    }

    @Override
    public Page<JournalInfoView> getJournalData(
            int page, int perPage,
            @Nullable String machineName,
            @Nullable String requestType,
            @Nullable Boolean succeed,
            @Nullable String contentType,
            @Nullable Short codeNumber,
            @Nullable Boolean outcoming,
            @Nullable OffsetDateTime sentFrom,
            @Nullable OffsetDateTime sentTo,
            @Nullable Boolean orderByDateAscending) {
        Pageable pageable = orderByDateAscending == null ?
                PageRequest.of(page, perPage)
                : PageRequest.of(page, perPage, orderByDateAscending ? Sort.Direction.ASC : Sort.Direction.DESC, "sent_time");
        return _journal.findFilteredJournal(
                machineName == null || machineName.isBlank() ? null : machineName,
                requestType == null || requestType.isBlank() ? null : requestType,
                succeed,
                contentType == null || contentType.isBlank() ? null : contentType,
                codeNumber,
                outcoming,
                sentFrom,
                sentTo,
                pageable);
    }
}
