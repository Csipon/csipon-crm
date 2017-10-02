package com.csipon.crm.exception.handler;

import com.csipon.crm.controller.message.MessageHeader;
import com.csipon.crm.exception.UnsupportedChangingStatusException;
import com.csipon.crm.controller.message.ResponseGenerator;
import com.csipon.crm.exception.NoSuchEmailException;
import com.csipon.crm.exception.lifecycle.order.UnsupportedTransitionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;

import static com.csipon.crm.controller.message.MessageProperty.ERROR_LIFECYCLE_ORDER;

/**
 * Created by Pasha on 30.04.2017.
 */

@ControllerAdvice
public class GlobalExceptionHandlingControllerAdvice {

    protected static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandlingControllerAdvice.class);

    private final ResponseGenerator generator;

    public GlobalExceptionHandlingControllerAdvice(ResponseGenerator generator) {
        this.generator = generator;
    }

    @ExceptionHandler({BadCredentialsException.class, LockedException.class})
    public ModelAndView handleError(RuntimeException exception) {
        ModelAndView mav = new ModelAndView();
        if (exception instanceof BadCredentialsException) {
            mav.addObject("error", "Invalid email or password");
        } else if (exception instanceof LockedException) {
            mav.addObject("error", exception.getMessage());
        }

        mav.setViewName("login");
        return mav;
    }

    @ExceptionHandler({NoSuchEmailException.class, MessagingException.class})
    public ModelAndView handleNoSuchEmail(Exception exception) {
        ModelAndView mav = new ModelAndView();
        if (exception instanceof NoSuchEmailException) {
            mav.addObject("error", exception.getMessage());
        } else if (exception instanceof MessagingException) {
            mav.addObject("error", "Something error with send email");
        }
        mav.setViewName("login");
        return mav;
    }

    @ExceptionHandler(UnsupportedTransitionException.class)
    public ResponseEntity<?> handleUnsupportedTransition(UnsupportedTransitionException exception) {
        log.error(exception.getMessage(), exception);
        return generator.getHttpResponse(MessageHeader.ERROR_MESSAGE, ERROR_LIFECYCLE_ORDER,
                new String[]{exception.getFrom()}, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UnsupportedChangingStatusException.class)
    public ResponseEntity<?> handleUnsupportedChangingStatus(UnsupportedChangingStatusException exception) {
        log.error(exception.getMessage(), exception);
        return generator.getHttpResponse(HttpStatus.BAD_REQUEST);
    }

}
