package com.zeafen.LocNetMonitoring.data.services;

import com.zeafen.LocNetMonitoring.data.repositories.BufferRepository;
import com.zeafen.LocNetMonitoring.domain.models.entity.Buffer;
import com.zeafen.LocNetMonitoring.domain.services.BufferService;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class JpaBufferService implements BufferService {
    private final BufferRepository _buffer;

    public JpaBufferService(BufferRepository buffer) {
        _buffer = buffer;
    }

    @Override
    public Page<Buffer> getBufferFiltered(int page, int perPage,
                                          @Nullable Integer machineId,
                                          @Nullable Integer maintenanceId,
                                          @Nullable OffsetDateTime dateFrom,
                                          @Nullable OffsetDateTime dateUntil,
                                          @Nullable Boolean isRead,
                                          @Nullable Short bufferType) {
        return _buffer.findAllFiltered(
                machineId,
                maintenanceId,
                dateFrom,
                dateUntil,
                isRead,
                bufferType,
                PageRequest.of(page, perPage)
        );
    }

    @Override
    public Long getBufferTypeUnreadCount(@Nullable Short bufferType) {
        return _buffer.countTypeUnread(bufferType);
    }
}
