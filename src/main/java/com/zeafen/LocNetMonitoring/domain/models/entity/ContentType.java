package com.zeafen.LocNetMonitoring.domain.models.entity;

import jakarta.persistence.*;

import java.util.Collection;


@Entity(name = "content_types")
public class ContentType {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "smallint")
    @SequenceGenerator(
            name = "content_types_id_seq",
            sequenceName = "content_types_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "content_types_id_seq"
    )
    private Short id;

    @Column(nullable = false, length = 25)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contentType")
    private Collection<JournalIncoming> contentTypeJournalIncomings ;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contentType")
    private Collection<JournalOutcoming> contentTypeJournalOutcomings ;

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

    public Collection<JournalIncoming> getContentTypeJournalIncomings() {
        return contentTypeJournalIncomings;
    }

    public void setContentTypeJournalIncomings(
            final Collection<JournalIncoming> contentTypeJournalIncomings) {
        this.contentTypeJournalIncomings = contentTypeJournalIncomings;
    }

    public Collection<JournalOutcoming> getContentTypeJournalOutcomings() {
        return contentTypeJournalOutcomings;
    }

    public void setContentTypeJournalOutcomings(
            final Collection<JournalOutcoming> contentTypeJournalOutcomings) {
        this.contentTypeJournalOutcomings = contentTypeJournalOutcomings;
    }

}
