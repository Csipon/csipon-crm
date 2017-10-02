package com.csipon.crm.dto.mapper.impl;

import com.csipon.crm.domain.model.Group;
import com.csipon.crm.dto.mapper.Mapper;
import com.csipon.crm.dao.DiscountDao;
import com.csipon.crm.dao.GroupDao;
import com.csipon.crm.domain.model.Discount;
import com.csipon.crm.domain.model.Product;
import com.csipon.crm.domain.model.ProductStatus;
import com.csipon.crm.domain.real.RealProduct;
import com.csipon.crm.dto.AutocompleteDto;
import com.csipon.crm.dto.ProductDto;
import com.csipon.crm.dto.row.ProductRowDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Karpunets
 * @since 21.05.2017
 */
@Component
public class ProductMapper {

    private final DiscountDao discountDao;
    private final GroupDao groupDao;

    @Autowired
    public ProductMapper(DiscountDao discountDao, GroupDao groupDao) {
        this.discountDao = discountDao;
        this.groupDao = groupDao;
    }

    public Mapper<ProductDto, RealProduct> dtoToModel() {
        return (dto, model) -> {
            model.setId(dto.getId());
            model.setTitle(dto.getTitle());
            model.setDescription(dto.getDescription());
            model.setDefaultPrice(dto.getDefaultPrice());
            if (dto.getStatusName() != null) {
                model.setStatus(ProductStatus.valueOf(dto.getStatusName()));
            }

            Discount discount = dto.getDiscountId() > 0 ? discountDao.findById(dto.getDiscountId()) : null;
            model.setDiscount(discount);
            Group group = dto.getGroupId() > 0 ? groupDao.findById(dto.getGroupId()) : null;
            model.setGroup(group);
        };
    }

    public Mapper<Product, ProductRowDto> modelToRowDto() {
        return (model, rowDto) -> {
            rowDto.setId(model.getId());
            rowDto.setTitle(model.getTitle());
            rowDto.setPrice(model.getDefaultPrice());
            rowDto.setStatus(model.getStatus().getName());
            if (model.getDiscount() != null) {
                rowDto.setDiscount(model.getDiscount().getId());
                rowDto.setDiscountTitle(model.getDiscount().getTitle());
                rowDto.setDiscountPercentage(model.getDiscount().getPercentage());
                rowDto.setDiscountActive(model.getDiscount().isActive());
            }
            if (model.getGroup() != null) {
                rowDto.setGroup(model.getGroup().getId());
                rowDto.setGroupName(model.getGroup().getName());
                if (model.getGroup().getDiscount() != null) {
                    rowDto.setGroupDiscount(model.getGroup().getDiscount().getId());
                    rowDto.setGroupDiscountTitle(model.getGroup().getDiscount().getTitle());
                    rowDto.setGroupDiscountPercentage(model.getGroup().getDiscount().getPercentage());
                    rowDto.setGroupDiscountActive(model.getGroup().getDiscount().isActive());
                }
            }
        };
    }

    public Mapper<Product, AutocompleteDto> modelToAutocomplete() {
        return (model, autocompleteDto) -> {
            autocompleteDto.setId(model.getId());
            autocompleteDto.setValue(model.getTitle());
        };
    }
}
