package com.globallogic.futbol.strategies.ion.example.entities;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Facundo Mengoni on 8/6/2015.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
public class Device implements Serializable{
    private String id;
    private Date createdAt;
    private Date updatedAt;
    private String name;
    private String resolution;

    public Device() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", resolution='" + resolution + '\'' +
                '}';
    }
}
