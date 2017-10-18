package com.csipon.crm.domain.converter;

import com.csipon.crm.domain.model.OrderStatus;
import com.csipon.crm.domain.model.Status;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class OrderStatusConverter implements AttributeConverter<OrderStatus, Long> {
    @Override
    public Long convertToDatabaseColumn(OrderStatus attribute) {
        return attribute.getId();
    }

    @Override
    public OrderStatus convertToEntityAttribute(Long dbData) {
        return (OrderStatus) Status.getStatusByID(dbData);
    }
}
