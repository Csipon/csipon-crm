package com.csipon.crm.validation.impl;

import com.csipon.crm.dao.ProductDao;
import com.csipon.crm.dto.ProductDto;
import com.csipon.crm.validation.AbstractValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import static com.csipon.crm.controller.message.MessageProperty.ERROR_CODE_REQUIRED;
import static com.csipon.crm.controller.message.MessageProperty.ERROR_CODE_WRONG_FORMAT;
import static com.csipon.crm.validation.field.ProductDtoField.*;

/**
 * Created by Pasha on 06.05.2017.
 */
@Component
@PropertySource(value = "classpath:message.properties")
public class ProductValidator extends AbstractValidator {
    private final ProductDao productDao;


    @Autowired
    public ProductValidator(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ProductDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductDto product = (ProductDto) target;
        checkPrice(errors, product);

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, DESCRIPTION.getName(), ERROR_CODE_REQUIRED, getErrorMessage(DESCRIPTION, ERROR_CODE_REQUIRED));
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, DEFAULT_PRICE.getName(), ERROR_CODE_REQUIRED, getErrorMessage(DEFAULT_PRICE, ERROR_CODE_REQUIRED));
        ValidationUtils.rejectIfEmpty(errors, TITLE.getName(), ERROR_CODE_REQUIRED, getErrorMessage(TITLE, ERROR_CODE_REQUIRED));
    }


    private void checkPrice(Errors errors, ProductDto productDto) {
        Double price = productDto.getDefaultPrice();

        if (price == null || price <= 0) {
            errors.rejectValue(DEFAULT_PRICE.getName(), ERROR_CODE_WRONG_FORMAT,
                    getErrorMessage(DEFAULT_PRICE, ERROR_CODE_WRONG_FORMAT));
        }
    }
}
