package com.csipon.crm.validation.impl;

import com.csipon.crm.dto.OrderExcelDto;
import com.csipon.crm.validation.AbstractValidator;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import static com.csipon.crm.controller.message.MessageProperty.ERROR_CODE_WRONG_FORMAT;
import static com.csipon.crm.validation.field.OrderExcelDtoField.*;

/**
 * Created by Pasha on 25.05.2017.
 */
@Component
@PropertySource(value = "classpath:message.properties")
public class OrderExcelValidator extends AbstractValidator {
    private static final String REGEX_PATTERN_DATE = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$";

    @Override
    public boolean supports(Class<?> clazz) {
        return OrderExcelDto.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        OrderExcelDto orderExcelDto = (OrderExcelDto) o;
        checkDateFrom(errors, orderExcelDto);
        checkDateTo(errors, orderExcelDto);
        checkOrderByIndex(errors, orderExcelDto);
        checkIdCustomers(errors, orderExcelDto);
    }


    private void checkIdCustomers(Errors errors, OrderExcelDto orderDto) {
        if (orderDto.getIdCustomer().length == 0) {
            errors.rejectValue(ID_CUSTOMERS.getName(), ERROR_CODE_WRONG_FORMAT,
                    getErrorMessage(ID_CUSTOMERS, ERROR_CODE_WRONG_FORMAT));
        }
    }

    private void checkOrderByIndex(Errors errors, OrderExcelDto orderDto) {
        if (orderDto.getOrderByIndex() < 0 && orderDto.getOrderByIndex() > 6) {
            errors.rejectValue(ORDER_BY_INDEX.getName(), ERROR_CODE_WRONG_FORMAT,
                    getErrorMessage(ORDER_BY_INDEX, ERROR_CODE_WRONG_FORMAT));
        }
    }

    private void checkDateFrom(Errors errors, OrderExcelDto orderDto) {
        if (!orderDto.getDateFrom().isEmpty()) {
            if (!orderDto.getDateFrom().matches(REGEX_PATTERN_DATE)) {
                errors.rejectValue(DATE_FROM.getName(), ERROR_CODE_WRONG_FORMAT,
                        getErrorMessage(DATE_FROM, ERROR_CODE_WRONG_FORMAT));
            }
        }
    }

    private void checkDateTo(Errors errors, OrderExcelDto orderDto) {
        if (!orderDto.getDateTo().isEmpty()) {
            if (!orderDto.getDateTo().matches(REGEX_PATTERN_DATE)) {
                errors.rejectValue(DATE_TO.getName(), ERROR_CODE_WRONG_FORMAT,
                        getErrorMessage(DATE_TO, ERROR_CODE_WRONG_FORMAT));
            }
        }
    }
}
