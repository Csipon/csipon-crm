package com.csipon.crm.validation.impl;

import com.csipon.crm.dao.UserDao;
import com.csipon.crm.domain.model.UserRole;
import com.csipon.crm.dto.UserDto;
import com.csipon.crm.validation.AbstractValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import static com.csipon.crm.controller.message.MessageProperty.*;
import static com.csipon.crm.validation.field.UserDtoField.*;

/**
 * Created by bpogo on 5/6/2017.
 */
@Component
@PropertySource(value = "classpath:message.properties")
public class UserValidator extends AbstractValidator {
    //match: 123456790
    private static final String REGEX_PATTERN_PHONE = "\\(?([0-9]{3})\\)?([ .-]?)([0-9]{3})\\2([0-9]{4})";
    ///match: admin@gmail.com
    private static final String REGEX_PATTERN_EMAIL = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])";

    private final UserDao userDao;

    @Autowired
    public UserValidator(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDto.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        UserDto userDto = (UserDto) obj;

        if (userDto.getUserRole().equals(UserRole.ROLE_CUSTOMER.getName())) {
            checkAddress(errors);
        }

        checkPhone(errors, userDto);
        checkEmail(errors, userDto);

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, PHONE.getName(), ERROR_CODE_REQUIRED, getErrorMessage(PHONE, ERROR_CODE_REQUIRED));
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, EMAIL.getName(), ERROR_CODE_REQUIRED, getErrorMessage(EMAIL, ERROR_CODE_REQUIRED));
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, LAST_NAME.getName(), ERROR_CODE_REQUIRED, getErrorMessage(LAST_NAME, ERROR_CODE_REQUIRED));
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, FIRST_NAME.getName(), ERROR_CODE_REQUIRED, getErrorMessage(FIRST_NAME, ERROR_CODE_REQUIRED));
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, USER_ROLE.getName(), ERROR_CODE_REQUIRED, getErrorMessage(USER_ROLE, ERROR_CODE_REQUIRED));
    }

    private void checkAddress(Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, ORGANIZATION_NAME.getName(), ERROR_CODE_REQUIRED, getErrorMessage(ORGANIZATION_NAME, ERROR_CODE_REQUIRED));
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, ADDRESS_DETAILS.getName(), ERROR_CODE_REQUIRED, getErrorMessage(ADDRESS_DETAILS, ERROR_CODE_REQUIRED));
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, ADDRESS_LATITUDE.getName(), ERROR_CODE_REQUIRED, getErrorMessage(ADDRESS_LATITUDE, ERROR_CODE_REQUIRED));
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, ADDRESS_LONGITUDE.getName(), ERROR_CODE_REQUIRED, getErrorMessage(ADDRESS_LONGITUDE, ERROR_CODE_REQUIRED));
    }

    private void checkEmail(Errors errors, UserDto userDto) {
        String email = userDto.getEmail();
        if (email != null) {
            if (!email.matches(REGEX_PATTERN_EMAIL)) {
                errors.rejectValue(EMAIL.getName(), ERROR_CODE_WRONG_FORMAT,
                        getErrorMessage(EMAIL, ERROR_CODE_WRONG_FORMAT));
            } else if (userDao.findByEmail(email) != null) {
                errors.rejectValue(EMAIL.getName(), ERROR_CODE_USER_ALREADY_EXIST,
                        getErrorMessage(EMAIL, ERROR_CODE_USER_ALREADY_EXIST));
            }
        }
    }

    private void checkPhone(Errors errors, UserDto userDto) {
        String phone = userDto.getPhone();
        if (phone != null) {
            if (!phone.matches(REGEX_PATTERN_PHONE)) {
                errors.rejectValue(PHONE.getName(), ERROR_CODE_WRONG_FORMAT,
                        getErrorMessage(PHONE, ERROR_CODE_WRONG_FORMAT));
            }
        }
    }
}
