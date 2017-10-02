package com.csipon.crm.validation.impl;

import com.csipon.crm.dto.GroupDto;
import com.csipon.crm.validation.AbstractValidator;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import static com.csipon.crm.controller.message.MessageProperty.ERROR_CODE_REQUIRED;
import static com.csipon.crm.validation.field.GroupDtoField.NAME;


/**
 * Created by Pasha on 06.05.2017.
 */
@Component
@PropertySource(value = "classpath:message.properties")
public class GroupValidator extends AbstractValidator {
    @Override
    public boolean supports(Class<?> clazz) {
        return GroupDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        GroupDto groupDto = (GroupDto) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, NAME.getName(), ERROR_CODE_REQUIRED, getErrorMessage(NAME, ERROR_CODE_REQUIRED));
    }
}
