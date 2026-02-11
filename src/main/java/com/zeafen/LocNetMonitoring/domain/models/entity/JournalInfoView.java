package com.zeafen.LocNetMonitoring.domain.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "journal_full_info")
public class JournalInfoView {
    @Id
    @Column(name = "ID")
    private UUID id;

    @Column(name = "session_id")
    private UUID sessionId;

    @Column(name = "machine_type_name")
    private String machineTypeName;

    @Column(name = "machine_id")
    private Integer machineId;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "machine_name")
    private String machineName;

    @Column(name = "address", columnDefinition = "text")
    private String address;

    @Column(name = "serial_number")
    private Integer serialNumber;

    @Column(name = "outcoming")
    private Boolean outcoming;

    @Column(name = "request_type")
    private String requestTypeName;

    @Column(name = "code_number", columnDefinition = "smallint")
    private Short codeNumber;

    @Column(name = "succeed")
    private Boolean succeed;

    @Column(name = "text")
    private String text;

    @Column(name = "request_data", columnDefinition = "BYTEA")
    private byte[] requestData;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "sent_time")
    private OffsetDateTime sentTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public String getMachineTypeName() {
        return machineTypeName;
    }

    public void setMachineTypeName(String machineTypeName) {
        this.machineTypeName = machineTypeName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Boolean getOutcoming() {
        return outcoming;
    }

    public void setOutcoming(Boolean outcoming) {
        this.outcoming = outcoming;
    }

    public String getRequestTypeName() {
        return requestTypeName;
    }

    public void setRequestTypeName(String requestTypeName) {
        this.requestTypeName = requestTypeName;
    }

    public Short getCodeNumber() {
        return codeNumber;
    }

    public void setCodeNumber(Short codeNumber) {
        this.codeNumber = codeNumber;
    }

    public Boolean getSucceed() {
        return succeed;
    }

    public void setSucceed(Boolean succeed) {
        this.succeed = succeed;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public byte[] getRequestData() {
        return requestData;
    }

    public void setRequestData(byte[] requestData) {
        this.requestData = requestData;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public OffsetDateTime getSentTime() {
        return sentTime;
    }

    public void setSentTime(OffsetDateTime sentTime) {
        this.sentTime = sentTime;
    }

    public Integer getMachineId() {
        return machineId;
    }

    public void setMachineId(Integer machineId) {
        this.machineId = machineId;
    }
}
