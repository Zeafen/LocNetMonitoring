package com.zeafen.LocNetMonitoring.domain.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;


@Entity(name = "journal_incoming")
public class JournalIncoming {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private UUID sessionId;

    @Column(columnDefinition = "BYTEA")
    private byte[] requestData;

    @Column(nullable = false)
    private OffsetDateTime sentTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id", nullable = false)
    private Machine machine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_type_id", nullable = false)
    private RequestType requestType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_code")
    private RequestCode requestCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_type_id")
    private ContentType contentType;

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(final UUID sessionId) {
        this.sessionId = sessionId;
    }

    public byte[] getRequestData() {
        return requestData;
    }

    public void setRequestData(final byte[] requestData) {
        this.requestData = requestData;
    }

    public OffsetDateTime getSentTime() {
        return sentTime;
    }

    public void setSentTime(final OffsetDateTime sentTime) {
        this.sentTime = sentTime;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(final Machine machine) {
        this.machine = machine;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(final RequestType requestType) {
        this.requestType = requestType;
    }

    public RequestCode getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(final RequestCode requestCode) {
        this.requestCode = requestCode;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(final ContentType contentType) {
        this.contentType = contentType;
    }

}
