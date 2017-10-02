package com.csipon.crm.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import static com.csipon.crm.controller.message.MessageHeader.VALIDATION_MESSAGE;

/**
 * Created by bpogo on 5/6/2017.
 */
@Component
public class BindingResultHandler {
    private static final Logger log = LoggerFactory.getLogger(BindingResultHandler.class);

    public ResponseEntity<?> handle(BindingResult bindingResult) {
        HttpHeaders errors = new HttpHeaders();
        for (ObjectError objectError : bindingResult.getAllErrors()) {
            errors.set(VALIDATION_MESSAGE.getHeaderName(), objectError.getDefaultMessage());
            log.error("Validation error was found, error code - " + objectError.getCode());
        }
        return new ResponseEntity<>(errors, HttpStatus.EXPECTATION_FAILED);
    }

}
