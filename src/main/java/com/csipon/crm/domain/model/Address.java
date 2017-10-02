package com.csipon.crm.domain.model;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 24.04.2017
 */
public interface Address {

    Long getId();

    void  setId(Long id);

    Double getLatitude();

    void setLatitude(Double latitude);

    Double getLongitude();

    void setLongitude(Double longitude);

    Region getRegion();

    void setRegion(Region region);

    String getDetails();

    void setDetails(String details);

    void setFormattedAddress(String formattedAddress);

    String getFormattedAddress();
}
