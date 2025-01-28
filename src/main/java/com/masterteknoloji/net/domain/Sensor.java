package com.masterteknoloji.net.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.masterteknoloji.net.domain.enumeration.ConnectionType;

import com.masterteknoloji.net.domain.enumeration.Type;

/**
 * A Sensor.
 */
@Entity
@Table(name = "sensor")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Sensor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "dev_eui", nullable = false)
    private String devEui;

    @Column(name = "app_eui")
    private String appEui;

    @Column(name = "app_key")
    private String appKey;

    @Column(name = "imei")
    private String imei;

    @Enumerated(EnumType.STRING)
    @Column(name = "connection_type")
    private ConnectionType connectionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private Type type;

    @Column(name = "last_seen_date")
    private ZonedDateTime lastSeenDate;

    @Size(max = 4000)
    @Column(name = "last_message", length = 4000)
    private String lastMessage;

    @Lob
    @Column(name = "last_image")
    private byte[] lastImage;

    @Column(name = "last_image_content_type")
    private String lastImageContentType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDevEui() {
        return devEui;
    }

    public Sensor devEui(String devEui) {
        this.devEui = devEui;
        return this;
    }

    public void setDevEui(String devEui) {
        this.devEui = devEui;
    }

    public String getAppEui() {
        return appEui;
    }

    public Sensor appEui(String appEui) {
        this.appEui = appEui;
        return this;
    }

    public void setAppEui(String appEui) {
        this.appEui = appEui;
    }

    public String getAppKey() {
        return appKey;
    }

    public Sensor appKey(String appKey) {
        this.appKey = appKey;
        return this;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getImei() {
        return imei;
    }

    public Sensor imei(String imei) {
        this.imei = imei;
        return this;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public Sensor connectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
        return this;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    public Type getType() {
        return type;
    }

    public Sensor type(Type type) {
        this.type = type;
        return this;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public ZonedDateTime getLastSeenDate() {
        return lastSeenDate;
    }

    public Sensor lastSeenDate(ZonedDateTime lastSeenDate) {
        this.lastSeenDate = lastSeenDate;
        return this;
    }

    public void setLastSeenDate(ZonedDateTime lastSeenDate) {
        this.lastSeenDate = lastSeenDate;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public Sensor lastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
        return this;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public byte[] getLastImage() {
        return lastImage;
    }

    public Sensor lastImage(byte[] lastImage) {
        this.lastImage = lastImage;
        return this;
    }

    public void setLastImage(byte[] lastImage) {
        this.lastImage = lastImage;
    }

    public String getLastImageContentType() {
        return lastImageContentType;
    }

    public Sensor lastImageContentType(String lastImageContentType) {
        this.lastImageContentType = lastImageContentType;
        return this;
    }

    public void setLastImageContentType(String lastImageContentType) {
        this.lastImageContentType = lastImageContentType;
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
        Sensor sensor = (Sensor) o;
        if (sensor.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sensor.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Sensor{" +
            "id=" + getId() +
            ", devEui='" + getDevEui() + "'" +
            ", appEui='" + getAppEui() + "'" +
            ", appKey='" + getAppKey() + "'" +
            ", imei='" + getImei() + "'" +
            ", connectionType='" + getConnectionType() + "'" +
            ", type='" + getType() + "'" +
            ", lastSeenDate='" + getLastSeenDate() + "'" +
            ", lastMessage='" + getLastMessage() + "'" +
            ", lastImage='" + getLastImage() + "'" +
            ", lastImageContentType='" + getLastImageContentType() + "'" +
            "}";
    }
}
