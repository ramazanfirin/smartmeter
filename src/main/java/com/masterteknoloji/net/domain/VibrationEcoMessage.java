package com.masterteknoloji.net.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A VibrationEcoMessage.
 */
@Entity
@Table(name = "vibration_eco_message")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class VibrationEcoMessage implements Serializable {

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

    public VibrationEcoMessage insertDate(ZonedDateTime insertDate) {
        this.insertDate = insertDate;
        return this;
    }

    public void setInsertDate(ZonedDateTime insertDate) {
        this.insertDate = insertDate;
    }

    public String getBase64Message() {
        return base64Message;
    }

    public VibrationEcoMessage base64Message(String base64Message) {
        this.base64Message = base64Message;
        return this;
    }

    public void setBase64Message(String base64Message) {
        this.base64Message = base64Message;
    }

    public String getHexMessage() {
        return hexMessage;
    }

    public VibrationEcoMessage hexMessage(String hexMessage) {
        this.hexMessage = hexMessage;
        return this;
    }

    public void setHexMessage(String hexMessage) {
        this.hexMessage = hexMessage;
    }

    public Float getBatteryValue() {
        return batteryValue;
    }

    public VibrationEcoMessage batteryValue(Float batteryValue) {
        this.batteryValue = batteryValue;
        return this;
    }

    public void setBatteryValue(Float batteryValue) {
        this.batteryValue = batteryValue;
    }

    public Float getxAxisValue() {
        return xAxisValue;
    }

    public VibrationEcoMessage xAxisValue(Float xAxisValue) {
        this.xAxisValue = xAxisValue;
        return this;
    }

    public void setxAxisValue(Float xAxisValue) {
        this.xAxisValue = xAxisValue;
    }

    public Float getyAxisValue() {
        return yAxisValue;
    }

    public VibrationEcoMessage yAxisValue(Float yAxisValue) {
        this.yAxisValue = yAxisValue;
        return this;
    }

    public void setyAxisValue(Float yAxisValue) {
        this.yAxisValue = yAxisValue;
    }

    public Float getzAxisValue() {
        return zAxisValue;
    }

    public VibrationEcoMessage zAxisValue(Float zAxisValue) {
        this.zAxisValue = zAxisValue;
        return this;
    }

    public void setzAxisValue(Float zAxisValue) {
        this.zAxisValue = zAxisValue;
    }

    public String getfPort() {
        return fPort;
    }

    public VibrationEcoMessage fPort(String fPort) {
        this.fPort = fPort;
        return this;
    }

    public void setfPort(String fPort) {
        this.fPort = fPort;
    }

    public Long getfCnt() {
        return fCnt;
    }

    public VibrationEcoMessage fCnt(Long fCnt) {
        this.fCnt = fCnt;
        return this;
    }

    public void setfCnt(Long fCnt) {
        this.fCnt = fCnt;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public VibrationEcoMessage sensor(Sensor sensor) {
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
        VibrationEcoMessage vibrationEcoMessage = (VibrationEcoMessage) o;
        if (vibrationEcoMessage.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), vibrationEcoMessage.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VibrationEcoMessage{" +
            "id=" + getId() +
            ", insertDate='" + getInsertDate() + "'" +
            ", base64Message='" + getBase64Message() + "'" +
            ", hexMessage='" + getHexMessage() + "'" +
            ", batteryValue=" + getBatteryValue() +
            ", xAxisValue=" + getxAxisValue() +
            ", yAxisValue=" + getyAxisValue() +
            ", zAxisValue=" + getzAxisValue() +
            ", fPort='" + getfPort() + "'" +
            ", fCnt=" + getfCnt() +
            "}";
    }
}
