package com.csipon.crm.dto.mapper.impl;

import com.csipon.crm.dto.mapper.Mapper;
import com.csipon.crm.domain.model.Region;
import com.csipon.crm.dto.AutocompleteDto;
import org.springframework.stereotype.Component;

/**
 * @author Karpunets
 * @since 21.05.2017
 */
@Component
public class RegionMapper {

    public Mapper<Region, AutocompleteDto> modelToAutocomplete() {
        return (model, autocompleteDto) -> {
            autocompleteDto.setId(model.getId());
            autocompleteDto.setValue(model.getName());
        };
    }
}
