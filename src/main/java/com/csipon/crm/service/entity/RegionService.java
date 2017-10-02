package com.csipon.crm.service.entity;

import com.csipon.crm.dto.AutocompleteDto;
import com.csipon.crm.dto.RegionDto;
import com.csipon.crm.domain.model.Region;

import java.util.List;

/**
 * @author Karpunets
 * @since 21.05.2017
 */
public interface RegionService {
    Region getRegionById(Long id);

    List<AutocompleteDto> getAutocompleteDto(String pattern);

    boolean update(RegionDto regionDto);
}
