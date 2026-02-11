package com.zeafen.LocNetMonitoring.domain.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;

import java.util.Collection;


@Entity(name = "request_types")
public class RequestType {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "smallint")
    @SequenceGenerator(
            name = "request_types_id_seq",
            sequenceName = "request_types_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "request_types_id_seq"
    )
    private Short id;

    @Column(length = 25)
    private String name;

    @OneToMany(mappedBy = "requestType")
    private Collection<JournalIncoming> requestTypeJournalIncomings ;

    @OneToMany(mappedBy = "requestType")
    private Collection<JournalOutcoming> requestTypeJournalOutcomings ;

    public Short getId() {
        return id;
    }

    public void setId(final Short id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Collection<JournalIncoming> getRequestTypeJournalIncomings() {
        return requestTypeJournalIncomings;
    }

    public void setRequestTypeJournalIncomings(
            final Collection<JournalIncoming> requestTypeJournalIncomings) {
        this.requestTypeJournalIncomings = requestTypeJournalIncomings;
    }

    public Collection<JournalOutcoming> getRequestTypeJournalOutcomings() {
        return requestTypeJournalOutcomings;
    }

    public void setRequestTypeJournalOutcomings(
            final Collection<JournalOutcoming> requestTypeJournalOutcomings) {
        this.requestTypeJournalOutcomings = requestTypeJournalOutcomings;
    }

}
