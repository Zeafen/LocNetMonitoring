package com.zeafen.LocNetMonitoring.domain.services;

import com.zeafen.LocNetMonitoring.domain.models.entity.JournalInfoView;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;

import java.time.OffsetDateTime;

public interface JournalService {
    Page<JournalInfoView> getJournalData(
            int page,
            int perPage,
            @Nullable Integer machineID,
            @Nullable String requestType,
            @Nullable Boolean succeed,
            @Nullable String contentType,
            @Nullable Short codeNumber,
            @Nullable Boolean outcoming,
            @Nullable OffsetDateTime sentFrom,
            @Nullable OffsetDateTime sentTo,
            @Nullable Boolean orderByDateAscending
    );

    Page<JournalInfoView> getJournalData(
            int page,
            int perPage,
            @Nullable String machineName,
            @Nullable String requestType,
            @Nullable Boolean succeed,
            @Nullable String contentType,
            @Nullable Short codeNumber,
            @Nullable Boolean outcoming,
            @Nullable OffsetDateTime sentFrom,
            @Nullable OffsetDateTime sentTo,
            @Nullable Boolean orderByDateAscending
    );
}
