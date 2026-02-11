package com.zeafen.LocNetMonitoring.domain.models.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

@Embeddable
public class MachineStatsId implements Serializable {
    private Integer id;
    private OffsetDateTime sentTime;
    private Boolean outcoming;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OffsetDateTime getSentTime() {
        return sentTime;
    }

    public void setSentTime(OffsetDateTime sentTime) {
        this.sentTime = sentTime;
    }

    public Boolean getOutcoming() {
        return outcoming;
    }

    public void setOutcoming(Boolean outcoming) {
        this.outcoming = outcoming;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MachineStatsId compared = (MachineStatsId) o;
        return Objects.equals(this.id, compared.id)
                && Objects.equals(this.outcoming, compared.outcoming)
                && Objects.equals(this.sentTime, compared.sentTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, outcoming, sentTime);
    }
}
