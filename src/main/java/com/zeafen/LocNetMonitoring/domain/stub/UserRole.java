package com.zeafen.LocNetMonitoring.domain.stub;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    ADMIN("Администратор системы"),
    OPERATOR("Оператор"),
    STUFF("Персонал");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }


    @Override
    public String getAuthority() {
        return name();
    }

    public String getDisplayName() {
        return displayName;
    }
}
