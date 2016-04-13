package com.globallogic.futbol.example.data.entities;

import java.io.Serializable;
import java.util.Date;

/**
 * @author facundo.mengoni
 * @since 0.3
 */
public class DeviceEntity implements Serializable {
    private Integer id;
    private Date createdAt;
    private Date updatedAt;
    private String name;
    private String resolution;

    public DeviceEntity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceEntity device = (DeviceEntity) o;

        if (id != null && device.id != null) return id.equals(device.id);
        return name != null && device.name != null && name.equals(device.name);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DeviceEntity {" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", resolution='" + resolution + '\'' +
                '}';
    }
}