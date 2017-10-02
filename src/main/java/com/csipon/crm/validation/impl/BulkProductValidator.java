package com.csipon.crm.validation.impl;

import com.csipon.crm.dao.ProductDao;
import com.csipon.crm.dto.bulk.ProductBulkDto;
import com.csipon.crm.validation.AbstractValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import static com.csipon.crm.controller.message.MessageProperty.*;
import static com.csipon.crm.validation.field.ProductDtoField.DEFAULT_PRICE;
import static com.csipon.crm.validation.field.ProductDtoField.DESCRIPTION;


/**
 * Created by Pasha on 06.05.2017.
 */
@Component
@PropertySource(value = "classpath:message.properties")
public class BulkProductValidator extends AbstractValidator {
    private final ProductDao bulkDao;


    @Autowired
    public BulkProductValidator(ProductDao bulkDao) {
        this.bulkDao = bulkDao;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ProductBulkDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductBulkDto bulkDto = (ProductBulkDto) target;

        if (bulkDto.isDefaultPriceChanged()) checkPrice(errors, bulkDto);
        if (bulkDto.isDescriptionChanged())
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, DESCRIPTION.getName(), ERROR_CODE_REQUIRED, getErrorMessage(DESCRIPTION, ERROR_CODE_REQUIRED));
        if (bulkDto.isDefaultPriceChanged())
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, DEFAULT_PRICE.getName(), ERROR_CODE_REQUIRED, getErrorMessage(DEFAULT_PRICE, ERROR_CODE_REQUIRED));
        if (bulkDto.isStatusNameChanged()) checkStatus(errors, bulkDto);
    }


    private void checkStatus(Errors errors, ProductBulkDto bulkDto) {
        if (!bulkDao.hasSameStatus(bulkDto.getItemIds())) {
            errors.rejectValue(DEFAULT_PRICE.getName(), ERROR_CODE_DIFFERENT_STATUS,
                    getErrorMessage(DEFAULT_PRICE, ERROR_CODE_DIFFERENT_STATUS));
        }
    }

    private void checkPrice(Errors errors, ProductBulkDto bulkDto) {
        Double price = bulkDto.getDefaultPrice();

        if (price != null && price <= 0) {
            errors.rejectValue(DEFAULT_PRICE.getName(), ERROR_CODE_WRONG_FORMAT,
                    getErrorMessage(DEFAULT_PRICE, ERROR_CODE_WRONG_FORMAT));
        }
    }
}
