package com.csipon.crm.domain.proxy;

import com.csipon.crm.dao.RegionDao;
import com.csipon.crm.domain.model.Region;

/**
 * @author Karpunets
 * @since 14.05.2017
 */
public class RegionProxy implements Region {
    private long id;
    private Region region;
    private RegionDao regionDao;

    public RegionProxy(RegionDao regionDao) {
        this.regionDao = regionDao;
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
    public String getName() {
        return getRegion().getName();
    }

    @Override
    public void setName(String name) {
        getRegion().setName(name);
    }

    private Region getRegion() {
        if (region == null) {
            region = regionDao.findById(id);
        }
        return region;
    }
}