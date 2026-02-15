package com.zeafen.LocNetMonitoring.domain.models.entity;

import com.zeafen.LocNetMonitoring.data.ClassToMapConverter;
import com.zeafen.LocNetMonitoring.domain.models.ComparedValue;
import com.zeafen.LocNetMonitoring.domain.models.WarningLevels;
import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Immutable
@Table(name = "device_stats")
public class MachineStatsView {
    @EmbeddedId
    private MachineStatsId id;

    @Column(name = "total_bytes_operated")
    private Long bytesOperated;

    @Column(name = "average_bytes_operated")
    private Float avgBytesOperated;

    @Column(name = "total_requests_count")
    private Long requestsCount;

    @Column(name = "succeed_requests_percetage")
    private Float succeedPrecentage;

    @Column(name = "requests_frequency_rate")
    private Float requestsFrequency;

    public Long getBytesOperated() {
        return bytesOperated;
    }

    public void setBytesOperated(Long bytesOperated) {
        this.bytesOperated = bytesOperated;
    }

    public Float getAvgBytesOperated() {
        return avgBytesOperated;
    }

    public void setAvgBytesOperated(Float avgBytesOperated) {
        this.avgBytesOperated = avgBytesOperated;
    }

    public Long getRequestsCount() {
        return requestsCount;
    }

    public void setRequestsCount(Long requestsCount) {
        this.requestsCount = requestsCount;
    }

    public Float getSucceedPrecentage() {
        return succeedPrecentage;
    }

    public void setSucceedPrecentage(Float succeedPrecentage) {
        this.succeedPrecentage = succeedPrecentage;
    }

    public Float getRequestsFrequency() {
        return requestsFrequency;
    }

    public void setRequestsFrequency(Float requestsFrequency) {
        this.requestsFrequency = requestsFrequency;
    }

    public List<ComparedValue<Object>> compareToStandards(List<ModelStandard> standards) {
        try {
            var map = ClassToMapConverter.convertToMap(this);
            List<ComparedValue<Object>> comparedValues = new ArrayList<>();

            map.keySet().forEach(key -> {
                var value = map.get(key);
                if (value instanceof MachineStatsId id) {
                    comparedValues.add(new ComparedValue<>("sentTime", id.getSentTime(), null));
                    comparedValues.add(new ComparedValue<>("outcoming", id.getOutcoming(), null));
                    comparedValues.add(new ComparedValue<>("id", id.getId(), null));
                    return;
                }

                ComparedValue<Object> comparedValue = new ComparedValue<>();
                comparedValue.setValue(value);
                comparedValue.setParameterName(key);
                comparedValue.setLevel(WarningLevels.LOW);

                var standard = standards.stream().filter(st -> st.getParameterName().equals(key)).findFirst().orElse(null);
                if (standard != null) {
                    BigDecimal valueDecimal = new BigDecimal(value.toString());
                    if (valueDecimal.compareTo(standard.getWarningValue()) > 0 && !standard.getGreater() ||
                            valueDecimal.compareTo(standard.getWarningValue()) < 0 && standard.getGreater())
                        comparedValue.setLevel(WarningLevels.CRITICAL);
                    else if (valueDecimal.compareTo(standard.getSuggestionValue()) > 0 && !standard.getGreater() ||
                            valueDecimal.compareTo(standard.getSuggestionValue()) < 0 && standard.getGreater())
                        comparedValue.setLevel(WarningLevels.AVERAGE);
                }

                comparedValues.add(comparedValue);
            });
            Collections.reverse(comparedValues);
            return comparedValues;
        } catch (IllegalAccessException ex) {
            return new ArrayList<>();
        }

    }
}
