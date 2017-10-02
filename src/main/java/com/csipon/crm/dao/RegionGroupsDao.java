package com.csipon.crm.dao;

import com.csipon.crm.domain.model.Group;
import com.csipon.crm.domain.model.Region;

import java.util.List;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 28.04.2017
 */
public interface RegionGroupsDao {

    Long create(Region region, Group group);

    Long create(Long idRegion, Long idGroup);

    Long delete(Region region, Group group);

    Long delete(Long idRegion, Long idGroup);

    List<Group> findGroupsByRegion(Region region);

    List<Group> findGroupsByRegionId(Long region);

    List<Region> findRegionsByGroup(Group group);

}
