package com.csipon.crm.domain.real;

import com.csipon.crm.domain.model.Address;
import com.csipon.crm.domain.model.Region;

/**
 * @author Karpunets
 * @since 21.05.2017
 */
public class RealAddress implements Address {
    private Long id;
    private Double latitude;
    private Double longitude;
    private String details;
    private Region region;
    private String formattedAddress;

    public RealAddress() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Double getLatitude() {
        return latitude;
    }

    @Override
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Override
    public Double getLongitude() {
        return longitude;
    }

    @Override
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public Region getRegion() {
        return region;
    }

    @Override
    public void setRegion(Region region) {
        this.region = region;
    }

    @Override
    public String getDetails() {
        return details;
    }

    @Override
    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    @Override
    public String getFormattedAddress() {
        return formattedAddress;
    }
}