package com.masterteknoloji.net.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
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

    @Column(name = "battery_value")
    private Float batteryValue;

    @Column(name = "x_axis_value")
    private Float xAxisValue;

    @Column(name = "y_axis_value")
    private Float yAxisValue;

    @Column(name = "z_axis_value")
    private Float zAxisValue;

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

    public LorawanMessage getLoraMessage() {
        return loraMessage;
    }

    public VibrationEcoMessage loraMessage(LorawanMessage lorawanMessage) {
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
            ", batteryValue=" + getBatteryValue() +
            ", xAxisValue=" + getxAxisValue() +
            ", yAxisValue=" + getyAxisValue() +
            ", zAxisValue=" + getzAxisValue() +
            "}";
    }
}
