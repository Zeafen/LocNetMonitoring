package com.zeafen.LocNetMonitoring.domain.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.Collection;


@Entity(name = "request_codes")
public class RequestCode {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "smallint")
    private Short codeNumber;

    @Column(nullable = false)
    private Boolean succeed;

    @Column(length = 100)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private MachineType type;

    @OneToMany(mappedBy = "requestCode")
    private Collection<JournalIncoming> requestCodeJournalIncomings ;

    @OneToMany(mappedBy = "requestCode")
    private Collection<JournalOutcoming> requestCodeJournalOutcomings ;

    public Short getCodeNumber() {
        return codeNumber;
    }

    public void setCodeNumber(final Short codeNumber) {
        this.codeNumber = codeNumber;
    }

    public Boolean getSucceed() {
        return succeed;
    }

    public void setSucceed(final Boolean succeed) {
        this.succeed = succeed;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public MachineType getType() {
        return type;
    }

    public void setType(final MachineType type) {
        this.type = type;
    }

    public Collection<JournalIncoming> getRequestCodeJournalIncomings() {
        return requestCodeJournalIncomings;
    }

    public void setRequestCodeJournalIncomings(
            final Collection<JournalIncoming> requestCodeJournalIncomings) {
        this.requestCodeJournalIncomings = requestCodeJournalIncomings;
    }

    public Collection<JournalOutcoming> getRequestCodeJournalOutcomings() {
        return requestCodeJournalOutcomings;
    }

    public void setRequestCodeJournalOutcomings(
            final Collection<JournalOutcoming> requestCodeJournalOutcomings) {
        this.requestCodeJournalOutcomings = requestCodeJournalOutcomings;
    }

}
