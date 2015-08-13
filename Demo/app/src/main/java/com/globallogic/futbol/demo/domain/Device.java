package com.globallogic.futbol.demo.domain;

import java.io.Serializable;

/**
 * Created by Facundo Mengoni on 6/12/2015.
 * GlobalLogic | facundo.mengoni@globallogic.com
 */
public class Device implements Serializable {
    private Long id;
    private String name;
    private String resolution;

    public Device() {
    }

    public Device(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}