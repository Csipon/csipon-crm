package com.csipon.crm.dao;

import com.csipon.crm.domain.model.Region;

import java.util.List;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 26.04.2017
 */
public interface RegionDao extends CrudDao<Region> {

    Region findByName(String name);

    Long getCount();

    List<Region> findAllByPattern(String pattern);
}
