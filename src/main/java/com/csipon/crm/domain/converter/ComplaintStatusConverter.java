package com.csipon.crm.domain.converter;

import com.csipon.crm.domain.model.ComplaintStatus;
import com.csipon.crm.domain.model.Status;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ComplaintStatusConverter implements AttributeConverter<ComplaintStatus, Long> {
    @Override
    public Long convertToDatabaseColumn(ComplaintStatus attribute) {
        return attribute.getId();
    }

    @Override
    public ComplaintStatus convertToEntityAttribute(Long dbData) {
        return (ComplaintStatus) Status.getStatusByID(dbData);
    }
}

