package com.masterteknoloji.net.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
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

    @ManyToOne
    private LorawanMessage loraMessage;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LorawanMessage getLoraMessage() {
        return loraMessage;
    }

    public VibrationProMessage loraMessage(LorawanMessage lorawanMessage) {
        this.loraMessage = lorawanMessage;
        return this;
    }

    public void setLoraMessage(LorawanMessage lorawanMessage) {
        this.loraMessage = lorawanMessage;
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
            ", batteryValue=" + getBatteryValue() +
            ", xAxisValue=" + getxAxisValue() +
            ", yAxisValue=" + getyAxisValue() +
            ", zAxisValue=" + getzAxisValue() +
            ", temperature=" + getTemperature() +
            "}";
    }
}
