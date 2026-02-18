package com.zeafen.LocNetMonitoring.data.services;

import com.zeafen.LocNetMonitoring.data.repositories.RequestCodesRepository;
import com.zeafen.LocNetMonitoring.domain.models.entity.RequestCode;
import com.zeafen.LocNetMonitoring.domain.services.RequestCodesService;
import org.jspecify.annotations.Nullable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class JpaRequestCodesService implements RequestCodesService {
    private final RequestCodesRepository _requestCodes;

    public JpaRequestCodesService(RequestCodesRepository requestCodes) {
        _requestCodes = requestCodes;
    }

    @Override
    public Page<RequestCode> getRequestCodes(int page, int perPage, @Nullable Short typeId, @Nullable String text, @Nullable Boolean isSucceed, @Nullable Short code_number) {
        return _requestCodes.findAllFiltered(
                typeId,
                text == null || text.isBlank() ? null : text,
                isSucceed,
                code_number,
                PageRequest.of(page, perPage)
        );
    }

    @Override
    public RequestCode getRequestCodeByID(Short requestCode) {
        return _requestCodes.findById(requestCode).orElse(null);
    }

    @Override
    public RequestCode saveRequestCode(RequestCode code) {
        return _requestCodes.save(code);
    }

    @Override
    public void deleteRequestCode(Short requestCode) {
        _requestCodes.deleteById(requestCode);
    }
}
