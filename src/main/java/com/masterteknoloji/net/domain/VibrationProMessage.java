package com.masterteknoloji.net.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A VibrationProMessage.
 */
@Entity
@Table(name = "vibration_pro_message")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class VibrationProMessage implements Serializable {

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

    @Column(name = "x_axis_value")
    private Float xAxisValue;

    @Column(name = "y_axis_value")
    private Float yAxisValue;

    @Column(name = "z_axis_value")
    private Float zAxisValue;

    @Column(name = "temperature")
    private Float temperature;

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

    public VibrationProMessage insertDate(ZonedDateTime insertDate) {
        this.insertDate = insertDate;
        return this;
    }

    public void setInsertDate(ZonedDateTime insertDate) {
        this.insertDate = insertDate;
    }

    public String getBase64Message() {
        return base64Message;
    }

    public VibrationProMessage base64Message(String base64Message) {
        this.base64Message = base64Message;
        return this;
    }

    public void setBase64Message(String base64Message) {
        this.base64Message = base64Message;
    }

    public String getHexMessage() {
        return hexMessage;
    }

    public VibrationProMessage hexMessage(String hexMessage) {
        this.hexMessage = hexMessage;
        return this;
    }

    public void setHexMessage(String hexMessage) {
        this.hexMessage = hexMessage;
    }

    public Float getBatteryValue() {
        return batteryValue;
    }

    public VibrationProMessage batteryValue(Float batteryValue) {
        this.batteryValue = batteryValue;
        return this;
    }

    public void setBatteryValue(Float batteryValue) {
        this.batteryValue = batteryValue;
    }

    public Float getxAxisValue() {
        return xAxisValue;
    }

    public VibrationProMessage xAxisValue(Float xAxisValue) {
        this.xAxisValue = xAxisValue;
        return this;
    }

    public void setxAxisValue(Float xAxisValue) {
        this.xAxisValue = xAxisValue;
    }

    public Float getyAxisValue() {
        return yAxisValue;
    }

    public VibrationProMessage yAxisValue(Float yAxisValue) {
        this.yAxisValue = yAxisValue;
        return this;
    }

    public void setyAxisValue(Float yAxisValue) {
        this.yAxisValue = yAxisValue;
    }

    public Float getzAxisValue() {
        return zAxisValue;
    }

    public VibrationProMessage zAxisValue(Float zAxisValue) {
        this.zAxisValue = zAxisValue;
        return this;
    }

    public void setzAxisValue(Float zAxisValue) {
        this.zAxisValue = zAxisValue;
    }

    public Float getTemperature() {
        return temperature;
    }

    public VibrationProMessage temperature(Float temperature) {
        this.temperature = temperature;
        return this;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public String getfPort() {
        return fPort;
    }

    public VibrationProMessage fPort(String fPort) {
        this.fPort = fPort;
        return this;
    }

    public void setfPort(String fPort) {
        this.fPort = fPort;
    }

    public Long getfCnt() {
        return fCnt;
    }

    public VibrationProMessage fCnt(Long fCnt) {
        this.fCnt = fCnt;
        return this;
    }

    public void setfCnt(Long fCnt) {
        this.fCnt = fCnt;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public VibrationProMessage sensor(Sensor sensor) {
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
        VibrationProMessage vibrationProMessage = (VibrationProMessage) o;
        if (vibrationProMessage.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), vibrationProMessage.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VibrationProMessage{" +
            "id=" + getId() +
            ", insertDate='" + getInsertDate() + "'" +
            ", base64Message='" + getBase64Message() + "'" +
            ", hexMessage='" + getHexMessage() + "'" +
            ", batteryValue=" + getBatteryValue() +
            ", xAxisValue=" + getxAxisValue() +
            ", yAxisValue=" + getyAxisValue() +
            ", zAxisValue=" + getzAxisValue() +
            ", temperature=" + getTemperature() +
            ", fPort='" + getfPort() + "'" +
            ", fCnt=" + getfCnt() +
            "}";
    }
}
