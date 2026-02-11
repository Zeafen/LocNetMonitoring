package com.zeafen.LocNetMonitoring.domain.services;

import com.zeafen.LocNetMonitoring.domain.models.entity.RequestCode;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;

public interface RequestCodesService {
    Page<RequestCode> getRequestCodes(
            int page, int perPage,
            @Nullable Short typeId,
            @Nullable String text,
            @Nullable Boolean isSucceed,
            @Nullable Short code_number
    );
    RequestCode getRequestCodeByID(
            Short requestCode
    );
    RequestCode saveRequestCode(RequestCode code);
    void deleteRequestCode(Short requestCode);
}
