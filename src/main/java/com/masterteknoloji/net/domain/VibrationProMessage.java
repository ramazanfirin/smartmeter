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

    @Column(name = "temperature")
    private Float temperature;

    @Column(name = "x_velocity")
    private Float xVelocity;

    @Column(name = "x_acceleration")
    private Float xAcceleration;

    @Column(name = "y_velocity")
    private Float yVelocity;

    @Column(name = "y_acceleration")
    private Float yAcceleration;

    @Column(name = "z_velocity")
    private Float zVelocity;

    @Column(name = "z_acceleration")
    private Float zAcceleration;

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

    public Float getxVelocity() {
        return xVelocity;
    }

    public VibrationProMessage xVelocity(Float xVelocity) {
        this.xVelocity = xVelocity;
        return this;
    }

    public void setxVelocity(Float xVelocity) {
        this.xVelocity = xVelocity;
    }

    public Float getxAcceleration() {
        return xAcceleration;
    }

    public VibrationProMessage xAcceleration(Float xAcceleration) {
        this.xAcceleration = xAcceleration;
        return this;
    }

    public void setxAcceleration(Float xAcceleration) {
        this.xAcceleration = xAcceleration;
    }

    public Float getyVelocity() {
        return yVelocity;
    }

    public VibrationProMessage yVelocity(Float yVelocity) {
        this.yVelocity = yVelocity;
        return this;
    }

    public void setyVelocity(Float yVelocity) {
        this.yVelocity = yVelocity;
    }

    public Float getyAcceleration() {
        return yAcceleration;
    }

    public VibrationProMessage yAcceleration(Float yAcceleration) {
        this.yAcceleration = yAcceleration;
        return this;
    }

    public void setyAcceleration(Float yAcceleration) {
        this.yAcceleration = yAcceleration;
    }

    public Float getzVelocity() {
        return zVelocity;
    }

    public VibrationProMessage zVelocity(Float zVelocity) {
        this.zVelocity = zVelocity;
        return this;
    }

    public void setzVelocity(Float zVelocity) {
        this.zVelocity = zVelocity;
    }

    public Float getzAcceleration() {
        return zAcceleration;
    }

    public VibrationProMessage zAcceleration(Float zAcceleration) {
        this.zAcceleration = zAcceleration;
        return this;
    }

    public void setzAcceleration(Float zAcceleration) {
        this.zAcceleration = zAcceleration;
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
            ", temperature=" + getTemperature() +
            ", xVelocity=" + getxVelocity() +
            ", xAcceleration=" + getxAcceleration() +
            ", yVelocity=" + getyVelocity() +
            ", yAcceleration=" + getyAcceleration() +
            ", zVelocity=" + getzVelocity() +
            ", zAcceleration=" + getzAcceleration() +
            "}";
    }
}
