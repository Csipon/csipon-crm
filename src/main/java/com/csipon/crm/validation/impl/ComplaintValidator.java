package com.csipon.crm.validation.impl;

import com.csipon.crm.dto.ComplaintDto;
import com.csipon.crm.validation.field.ComplaintDtoField;
import com.csipon.crm.dao.OrderDao;
import com.csipon.crm.validation.AbstractValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import static com.csipon.crm.controller.message.MessageProperty.ERROR_CODE_REQUIRED;
import static com.csipon.crm.controller.message.MessageProperty.ERROR_CODE_WRONG_FORMAT;

/**
 * Created by Pasha on 06.05.2017.
 */
@Component
@PropertySource(value = "classpath:message.properties")
public class ComplaintValidator extends AbstractValidator {

    private final OrderDao orderDao;

    @Autowired
    public ComplaintValidator(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ComplaintDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ComplaintDto complaintDto = (ComplaintDto) target;
        checkOrder(errors, complaintDto);

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, ComplaintDtoField.MESSAGE.getName(), ERROR_CODE_REQUIRED, getErrorMessage(ComplaintDtoField.MESSAGE, ERROR_CODE_REQUIRED));
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, ComplaintDtoField.ORDER_ID.getName(), ERROR_CODE_REQUIRED, getErrorMessage(ComplaintDtoField.ORDER_ID, ERROR_CODE_REQUIRED));
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, ComplaintDtoField.TITLE.getName(), ERROR_CODE_REQUIRED, getErrorMessage(ComplaintDtoField.TITLE, ERROR_CODE_REQUIRED));
    }

    private void checkOrder(Errors errors, ComplaintDto complaintDto) {
        Long orderId = complaintDto.getOrderId();
        if (orderId == null || orderDao.findById(orderId) == null) {
            errors.rejectValue(ComplaintDtoField.ORDER_ID.getName(), ERROR_CODE_WRONG_FORMAT,
                    getErrorMessage(ComplaintDtoField.ORDER_ID, ERROR_CODE_WRONG_FORMAT));
        }
    }
}
