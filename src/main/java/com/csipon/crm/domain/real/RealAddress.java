package com.csipon.crm.domain.real;

import com.csipon.crm.domain.model.Address;
import com.csipon.crm.domain.model.Region;

import javax.persistence.*;

/**
 * @author Karpunets
 * @since 21.05.2017
 */
@Entity
@Table(name = "address")
public class RealAddress implements Address {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private Double latitude;
    @Column
    private Double longitude;
    @Column
    private String details;
    @JoinColumn(name = "region_id" , table = "region", referencedColumnName = "id")
    private Region region;
    @Column(name = "formatted_address")
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