package com.csipon.crm.domain.proxy;

import com.csipon.crm.dao.AddressDao;
import com.csipon.crm.domain.model.Address;
import com.csipon.crm.domain.model.Region;

/**
 * @author Karpunets
 * @since 14.05.2017
 */
public class AddressProxy implements Address {
    private long id;
    private Address address;
    private AddressDao addressDao;

    public AddressProxy(AddressDao addressDao) {
        this.addressDao = addressDao;
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
        return getAddress().getLatitude();
    }

    @Override
    public void setLatitude(Double latitude) {
        getAddress().setLatitude(latitude);
    }

    @Override
    public Double getLongitude() {
        return getAddress().getLongitude();
    }

    @Override
    public void setLongitude(Double longitude) {
        getAddress().setLongitude(longitude);
    }

    @Override
    public Region getRegion() {
        return getAddress().getRegion();
    }

    @Override
    public void setRegion(Region region) {
        getAddress().setRegion(region);
    }

    @Override
    public String getDetails() {
        return getAddress().getDetails();
    }

    @Override
    public void setDetails(String details) {
        getAddress().setDetails(details);
    }

    @Override
    public void setFormattedAddress(String formattedAddress) {
        getAddress().setFormattedAddress(formattedAddress);
    }

    @Override
    public String getFormattedAddress() {
        return getAddress().getFormattedAddress();
    }

    private Address getAddress() {
        if (address == null) {
            address = addressDao.findById(id);
        }
        return address;
    }
}