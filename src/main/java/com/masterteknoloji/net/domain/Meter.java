package com.masterteknoloji.net.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Meter.
 */
@Entity
@Table(name = "meter")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Meter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "meter_no")
    private String meterNo;

    @Column(name = "customer_no")
    private String customerNo;

    @Column(name = "address")
    private String address;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMeterNo() {
        return meterNo;
    }

    public Meter meterNo(String meterNo) {
        this.meterNo = meterNo;
        return this;
    }

    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public Meter customerNo(String customerNo) {
        this.customerNo = customerNo;
        return this;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getAddress() {
        return address;
    }

    public Meter address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
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
        Meter meter = (Meter) o;
        if (meter.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), meter.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Meter{" +
            "id=" + getId() +
            ", meterNo='" + getMeterNo() + "'" +
            ", customerNo='" + getCustomerNo() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
