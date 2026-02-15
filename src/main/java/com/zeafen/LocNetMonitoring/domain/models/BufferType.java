package com.zeafen.LocNetMonitoring.domain.models;

public enum BufferType {
    STATS_CHANGE("Изменение статистик устройств", "statsChange"),
    UPCOMING_MAINTENANCE("Грядущие ТО", "upcomingMtc"),
    MAINTENANCE_STATUS_CHANGE("Изменение статуса устройств", "mtcStatusChange");

    private final String displayName;
    private final String parameterName;

    BufferType(String displayName, String parameterName) {
        this.displayName = displayName;
        this.parameterName = parameterName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getParameterName() {
        return parameterName;
    }
}
