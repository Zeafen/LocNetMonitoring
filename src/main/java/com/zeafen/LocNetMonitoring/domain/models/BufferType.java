package com.zeafen.LocNetMonitoring.domain.models;

public enum BufferType {
    STATS_CHANGE("Изменение статистик устройств", "statsChange", "error"),
    UPCOMING_MAINTENANCE("Грядущие ТО", "upcomingMtc", "info"),
    MAINTENANCE_STATUS_CHANGE("Изменение статуса устройств", "mtcStatusChange", "warning");

    private final String displayName;
    private final String parameterName;
    private final String warningLevel;

    BufferType(String displayName, String parameterName, String warningLevel) {
        this.displayName = displayName;
        this.parameterName = parameterName;
        this.warningLevel = warningLevel;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getParameterName() {
        return parameterName;
    }

    public String getWarningLevel() {
        return warningLevel;
    }
}
