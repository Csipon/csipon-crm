package com.csipon.crm.service.entity.impl;

import com.csipon.crm.dao.RegionDao;
import com.csipon.crm.dao.RegionGroupsDao;
import com.csipon.crm.domain.model.Group;
import com.csipon.crm.domain.model.Region;
import com.csipon.crm.dto.AutocompleteDto;
import com.csipon.crm.dto.RegionDto;
import com.csipon.crm.dto.mapper.ModelMapper;
import com.csipon.crm.dto.mapper.impl.RegionMapper;
import com.csipon.crm.service.entity.RegionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Karpunets
 * @since 21.05.2017
 */
@Service
public class RegionServiceImpl implements RegionService {
    private static final Logger log = LoggerFactory.getLogger(RegionServiceImpl.class);

    private final RegionDao regionDao;
    private final RegionGroupsDao regionGroupsDao;
    private final RegionMapper regionMapper;

    @Autowired
    public RegionServiceImpl(RegionDao regionDao, RegionGroupsDao regionGroupsDao, RegionMapper regionMapper) {
        this.regionDao = regionDao;
        this.regionGroupsDao = regionGroupsDao;
        this.regionMapper = regionMapper;
    }

    @Override
    public Region getRegionById(Long id) {
        return regionDao.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AutocompleteDto> getAutocompleteDto(String pattern) {
        List<Region> regions = regionDao.findAllByPattern(pattern);
        return ModelMapper.mapList(regionMapper.modelToAutocomplete(), regions, AutocompleteDto.class);
    }

    @Override
    public boolean update(RegionDto regionDto) {
        List<Group> oldGroups = regionGroupsDao.findGroupsByRegionId(regionDto.getId());
        for (Group oldGroup : oldGroups) {
            if (regionDto.getGroupIds().contains(oldGroup.getId())) {
                regionDto.getGroupIds().remove(oldGroup.getId());
            } else {
                regionGroupsDao.delete(regionDto.getId(), oldGroup.getId());
            }
        }
        for (Long newGroupId : regionDto.getGroupIds()) {
            regionGroupsDao.create(regionDto.getId(), newGroupId);
        }
        return true;
    }

}
