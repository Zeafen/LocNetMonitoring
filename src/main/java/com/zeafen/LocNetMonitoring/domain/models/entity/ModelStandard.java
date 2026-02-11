package com.zeafen.LocNetMonitoring.domain.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

@Entity(name = "model_standards")
public class ModelStandard {
    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "model_standards_id_seq",
            sequenceName = "model_standards_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "model_standards_id_seq"
    )
    private Integer id;

    @Column(nullable = false, length = 50)
    @Min(0)
    private String parameterName;

    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal suggestionValue;

    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal warningValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", nullable = false)
    private MachineModel model;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(final String parameterName) {
        this.parameterName = parameterName;
    }

    public BigDecimal getSuggestionValue() {
        return suggestionValue;
    }

    public void setSuggestionValue(final BigDecimal suggestionValue) {
        this.suggestionValue = suggestionValue;
    }

    public BigDecimal getWarningValue() {
        return warningValue;
    }

    public void setWarningValue(final BigDecimal warningValue) {
        this.warningValue = warningValue;
    }

    public MachineModel getModel() {
        return model;
    }

    public void setModel(final MachineModel model) {
        this.model = model;
    }
}
