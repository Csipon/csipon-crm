package com.csipon.crm.domain.converter;

import com.csipon.crm.domain.model.ProductStatus;
import com.csipon.crm.domain.model.Status;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ProductStatusConverter implements AttributeConverter<ProductStatus, Long> {
    @Override
    public Long convertToDatabaseColumn(ProductStatus attribute) {
        return attribute.getId();
    }

    @Override
    public ProductStatus convertToEntityAttribute(Long dbData) {
        return (ProductStatus) Status.getStatusByID(dbData);
    }
}
