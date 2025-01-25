package com.masterteknoloji.net.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A LorawanMessage.
 */
@Entity
@Table(name = "lorawan_message")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LorawanMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "base_64_message")
    private String base64Message;

    @Column(name = "hex_message")
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

    public LorawanMessage base64Message(String base64Message) {
        this.base64Message = base64Message;
        return this;
    }

    public void setBase64Message(String base64Message) {
        this.base64Message = base64Message;
    }

    public String getHexMessage() {
        return hexMessage;
    }

    public LorawanMessage hexMessage(String hexMessage) {
        this.hexMessage = hexMessage;
        return this;
    }

    public void setHexMessage(String hexMessage) {
        this.hexMessage = hexMessage;
    }

    public Long getIndex() {
        return index;
    }

    public LorawanMessage index(Long index) {
        this.index = index;
        return this;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Long getTotalMessageCount() {
        return totalMessageCount;
    }

    public LorawanMessage totalMessageCount(Long totalMessageCount) {
        this.totalMessageCount = totalMessageCount;
        return this;
    }

    public void setTotalMessageCount(Long totalMessageCount) {
        this.totalMessageCount = totalMessageCount;
    }

    public ZonedDateTime getInsertDate() {
        return insertDate;
    }

    public LorawanMessage insertDate(ZonedDateTime insertDate) {
        this.insertDate = insertDate;
        return this;
    }

    public void setInsertDate(ZonedDateTime insertDate) {
        this.insertDate = insertDate;
    }

    public byte[] getImage() {
        return image;
    }

    public LorawanMessage image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public LorawanMessage imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public LorawanMessage sensor(Sensor sensor) {
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
        LorawanMessage lorawanMessage = (LorawanMessage) o;
        if (lorawanMessage.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), lorawanMessage.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LorawanMessage{" +
            "id=" + getId() +
            ", base64Message='" + getBase64Message() + "'" +
            ", hexMessage='" + getHexMessage() + "'" +
            ", index=" + getIndex() +
            ", totalMessageCount=" + getTotalMessageCount() +
            ", insertDate='" + getInsertDate() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            "}";
    }
}
