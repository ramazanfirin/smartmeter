package com.masterteknoloji.net.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A M2mMessage.
 */
@Entity
@Table(name = "m_2_m_message")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class M2mMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 4000)
    @Column(name = "base_64_message", length = 4000)
    private String base64Message;

    @Size(max = 4000)
    @Column(name = "hex_message", length = 4000)
    private String hexMessage;

    @Column(name = "jhi_index")
    private Long index;

    @Column(name = "total_message_count")
    private Long totalMessageCount;

    @Column(name = "insert_date")
    private ZonedDateTime insertDate;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Column(name = "battery_value")
    private Float batteryValue;

    @Column(name = "sensor_value")
    private Float sensorValue;

    @Column(name = "port")
    private Long port;

    @Column(name = "image_data")
    private Boolean imageData;

    @Column(name = "valid_image")
    private Boolean validImage;

    @ManyToOne
    private Sensor sensor;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBase64Message() {
        return base64Message;
    }

    public M2mMessage base64Message(String base64Message) {
        this.base64Message = base64Message;
        return this;
    }

    public void setBase64Message(String base64Message) {
        this.base64Message = base64Message;
    }

    public String getHexMessage() {
        return hexMessage;
    }

    public M2mMessage hexMessage(String hexMessage) {
        this.hexMessage = hexMessage;
        return this;
    }

    public void setHexMessage(String hexMessage) {
        this.hexMessage = hexMessage;
    }

    public Long getIndex() {
        return index;
    }

    public M2mMessage index(Long index) {
        this.index = index;
        return this;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Long getTotalMessageCount() {
        return totalMessageCount;
    }

    public M2mMessage totalMessageCount(Long totalMessageCount) {
        this.totalMessageCount = totalMessageCount;
        return this;
    }

    public void setTotalMessageCount(Long totalMessageCount) {
        this.totalMessageCount = totalMessageCount;
    }

    public ZonedDateTime getInsertDate() {
        return insertDate;
    }

    public M2mMessage insertDate(ZonedDateTime insertDate) {
        this.insertDate = insertDate;
        return this;
    }

    public void setInsertDate(ZonedDateTime insertDate) {
        this.insertDate = insertDate;
    }

    public byte[] getImage() {
        return image;
    }

    public M2mMessage image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public M2mMessage imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Float getBatteryValue() {
        return batteryValue;
    }

    public M2mMessage batteryValue(Float batteryValue) {
        this.batteryValue = batteryValue;
        return this;
    }

    public void setBatteryValue(Float batteryValue) {
        this.batteryValue = batteryValue;
    }

    public Float getSensorValue() {
        return sensorValue;
    }

    public M2mMessage sensorValue(Float sensorValue) {
        this.sensorValue = sensorValue;
        return this;
    }

    public void setSensorValue(Float sensorValue) {
        this.sensorValue = sensorValue;
    }

    public Long getPort() {
        return port;
    }

    public M2mMessage port(Long port) {
        this.port = port;
        return this;
    }

    public void setPort(Long port) {
        this.port = port;
    }

    public Boolean isImageData() {
        return imageData;
    }

    public M2mMessage imageData(Boolean imageData) {
        this.imageData = imageData;
        return this;
    }

    public void setImageData(Boolean imageData) {
        this.imageData = imageData;
    }

    public Boolean isValidImage() {
        return validImage;
    }

    public M2mMessage validImage(Boolean validImage) {
        this.validImage = validImage;
        return this;
    }

    public void setValidImage(Boolean validImage) {
        this.validImage = validImage;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public M2mMessage sensor(Sensor sensor) {
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
        M2mMessage m2mMessage = (M2mMessage) o;
        if (m2mMessage.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), m2mMessage.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "M2mMessage{" +
            "id=" + getId() +
            ", base64Message='" + getBase64Message() + "'" +
            ", hexMessage='" + getHexMessage() + "'" +
            ", index=" + getIndex() +
            ", totalMessageCount=" + getTotalMessageCount() +
            ", insertDate='" + getInsertDate() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", batteryValue=" + getBatteryValue() +
            ", sensorValue=" + getSensorValue() +
            ", port=" + getPort() +
            ", imageData='" + isImageData() + "'" +
            ", validImage='" + isValidImage() + "'" +
            "}";
    }
}
