package com.netcracker.crm.dto.mapper.impl;

import com.netcracker.crm.dao.ProductDao;
import com.netcracker.crm.domain.model.Product;
import com.netcracker.crm.domain.model.ProductParam;
import com.netcracker.crm.domain.real.RealProductParam;
import com.netcracker.crm.dto.AutocompleteDto;
import com.netcracker.crm.dto.ProductParamDto;
import com.netcracker.crm.dto.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author YARUS
 */
@Component
public class ProductParamMapper {
    private final ProductDao productDao;

    @Autowired
    public ProductParamMapper(ProductDao productDao) {
        this.productDao = productDao;
    }

    public Mapper<ProductParamDto, RealProductParam> dtoToModel() {
        return (dto, model) -> {
            model.setId(dto.getId());
            model.setParamName(dto.getParamName());
            model.setValue(dto.getValue());

            if (dto.getProductId() != null) {
                Product product = dto.getProductId() > 0 ? productDao.findById(dto.getProductId()) : null;
                model.setProduct(product);
            }

        };
    }

    public Mapper<ProductParam, AutocompleteDto> modelToAutocomplete() {
        return (model, autocompleteDto) -> {
            autocompleteDto.setId(model.getId());
            autocompleteDto.setValue(model.getParamName());
        };
    }

}
