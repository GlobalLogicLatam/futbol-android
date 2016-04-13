package com.globallogic.futbol.example.domain.models;

import com.globallogic.futbol.example.data.entities.DeviceEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Facundo Mengoni on 8/6/2015.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
public class Device implements Serializable {
    private Integer id;
    private Date createdAt;
    private Date updatedAt;
    private String name;
    private String resolution;

    public Device() {
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

        Device device = (Device) o;

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
        return "Device {" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", resolution='" + resolution + '\'' +
                '}';
    }

    public static Device fromDeviceEntity(DeviceEntity deviceEntity) {
        Device device = new Device();
        device.setId(deviceEntity.getId());
        device.setCreatedAt(deviceEntity.getCreatedAt());
        device.setUpdatedAt(deviceEntity.getUpdatedAt());
        device.setName(deviceEntity.getName());
        device.setResolution(deviceEntity.getResolution());
        return device;
    }
}