package com.zeafen.LocNetMonitoring.domain.models;

public class ComparedValue<T> {
    private String parameterName;
    private T value;
    private WarningLevels level;

    public ComparedValue(){}

    public ComparedValue(
            String parameterName,
            T value,
            WarningLevels level
    ){
        this.parameterName = parameterName;
        this.value = value;
        this.level = level;
    }

    public WarningLevels getLevel() {
        return level;
    }

    public void setLevel(WarningLevels level) {
        this.level = level;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }
}