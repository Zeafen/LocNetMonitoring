package com.zeafen.LocNetMonitoring.domain.stub;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    ADMIN,
    OPERATOR,
    STUFF;


    @Override
    public String getAuthority() {
        return name();
    }
}
