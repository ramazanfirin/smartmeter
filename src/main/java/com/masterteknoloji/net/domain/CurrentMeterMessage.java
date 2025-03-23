package com.masterteknoloji.net.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
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

    @Column(name = "battery_value")
    private Float batteryValue;

    @Column(name = "jhi_current")
    private Float current;

    @Column(name = "total_energy")
    private Float totalEnergy;

    @Column(name = "reason")
    private String reason;

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

    public LorawanMessage getLoraMessage() {
        return loraMessage;
    }

    public CurrentMeterMessage loraMessage(LorawanMessage lorawanMessage) {
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
            ", batteryValue=" + getBatteryValue() +
            ", current=" + getCurrent() +
            ", totalEnergy=" + getTotalEnergy() +
            ", reason='" + getReason() + "'" +
            "}";
    }
}
