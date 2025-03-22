package com.masterteknoloji.net.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A CurrentMeterMessage.
 */
@Entity
@Table(name = "current_meter_message")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CurrentMeterMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "insert_date")
    private ZonedDateTime insertDate;

    @Size(max = 4000)
    @Column(name = "base_64_message", length = 4000)
    private String base64Message;

    @Size(max = 4000)
    @Column(name = "hex_message", length = 4000)
    private String hexMessage;

    @Column(name = "battery_value")
    private Float batteryValue;

    @Column(name = "jhi_current")
    private Float current;

    @Column(name = "total_energy")
    private Float totalEnergy;

    @Column(name = "reason")
    private String reason;

    @Column(name = "f_port")
    private String fPort;

    @Column(name = "f_cnt")
    private Long fCnt;

    @ManyToOne
    private Sensor sensor;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getInsertDate() {
        return insertDate;
    }

    public CurrentMeterMessage insertDate(ZonedDateTime insertDate) {
        this.insertDate = insertDate;
        return this;
    }

    public void setInsertDate(ZonedDateTime insertDate) {
        this.insertDate = insertDate;
    }

    public String getBase64Message() {
        return base64Message;
    }

    public CurrentMeterMessage base64Message(String base64Message) {
        this.base64Message = base64Message;
        return this;
    }

    public void setBase64Message(String base64Message) {
        this.base64Message = base64Message;
    }

    public String getHexMessage() {
        return hexMessage;
    }

    public CurrentMeterMessage hexMessage(String hexMessage) {
        this.hexMessage = hexMessage;
        return this;
    }

    public void setHexMessage(String hexMessage) {
        this.hexMessage = hexMessage;
    }

    public Float getBatteryValue() {
        return batteryValue;
    }

    public CurrentMeterMessage batteryValue(Float batteryValue) {
        this.batteryValue = batteryValue;
        return this;
    }

    public void setBatteryValue(Float batteryValue) {
        this.batteryValue = batteryValue;
    }

    public Float getCurrent() {
        return current;
    }

    public CurrentMeterMessage current(Float current) {
        this.current = current;
        return this;
    }

    public void setCurrent(Float current) {
        this.current = current;
    }

    public Float getTotalEnergy() {
        return totalEnergy;
    }

    public CurrentMeterMessage totalEnergy(Float totalEnergy) {
        this.totalEnergy = totalEnergy;
        return this;
    }

    public void setTotalEnergy(Float totalEnergy) {
        this.totalEnergy = totalEnergy;
    }

    public String getReason() {
        return reason;
    }

    public CurrentMeterMessage reason(String reason) {
        this.reason = reason;
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getfPort() {
        return fPort;
    }

    public CurrentMeterMessage fPort(String fPort) {
        this.fPort = fPort;
        return this;
    }

    public void setfPort(String fPort) {
        this.fPort = fPort;
    }

    public Long getfCnt() {
        return fCnt;
    }

    public CurrentMeterMessage fCnt(Long fCnt) {
        this.fCnt = fCnt;
        return this;
    }

    public void setfCnt(Long fCnt) {
        this.fCnt = fCnt;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public CurrentMeterMessage sensor(Sensor sensor) {
        this.sensor = sensor;
        return this;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CurrentMeterMessage currentMeterMessage = (CurrentMeterMessage) o;
        if (currentMeterMessage.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), currentMeterMessage.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CurrentMeterMessage{" +
            "id=" + getId() +
            ", insertDate='" + getInsertDate() + "'" +
            ", base64Message='" + getBase64Message() + "'" +
            ", hexMessage='" + getHexMessage() + "'" +
            ", batteryValue=" + getBatteryValue() +
            ", current=" + getCurrent() +
            ", totalEnergy=" + getTotalEnergy() +
            ", reason='" + getReason() + "'" +
            ", fPort='" + getfPort() + "'" +
            ", fCnt=" + getfCnt() +
            "}";
    }
}
