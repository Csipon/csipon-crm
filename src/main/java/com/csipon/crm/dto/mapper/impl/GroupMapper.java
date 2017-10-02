package com.csipon.crm.dto.mapper.impl;

import com.csipon.crm.domain.model.Group;
import com.csipon.crm.dto.mapper.Mapper;
import com.csipon.crm.dao.DiscountDao;
import com.csipon.crm.domain.model.Discount;
import com.csipon.crm.domain.real.RealGroup;
import com.csipon.crm.dto.AutocompleteDto;
import com.csipon.crm.dto.GroupDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Karpunets
 * @since 21.05.2017
 */
@Component
public class GroupMapper {

    private final DiscountDao discountDao;

    @Autowired
    public GroupMapper(DiscountDao discountDao) {
        this.discountDao = discountDao;
    }

    public Mapper<GroupDto, RealGroup> dtoToModel() {
        return (dto, model) -> {
            model.setId(dto.getId());
            model.setName(dto.getName());
            Discount discount = dto.getDiscountId() > 0 ? discountDao.findById(dto.getDiscountId()) : null;
            model.setDiscount(discount);
        };
    }

    public Mapper<Group, AutocompleteDto> modelToAutocomplete() {
        return (model, autocompleteDto) -> {
            autocompleteDto.setId(model.getId());
            autocompleteDto.setValue(model.getName());
        };
    }
}
