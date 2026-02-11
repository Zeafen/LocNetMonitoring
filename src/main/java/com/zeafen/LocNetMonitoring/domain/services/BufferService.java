package com.zeafen.LocNetMonitoring.domain.services;

import com.zeafen.LocNetMonitoring.domain.models.entity.Buffer;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;

import java.time.OffsetDateTime;

public interface BufferService {
    public Page<Buffer> getBufferFiltered(
            int page, int perPage,
            @Nullable Integer machineId,
            @Nullable Integer maintenanceId,
            @Nullable OffsetDateTime dateFrom,
            @Nullable OffsetDateTime dateUntil,
            @Nullable Boolean isRead,
            @Nullable Short bufferType
    );

    Long getBufferTypeUnreadCount(@Nullable Short bufferType);
}
