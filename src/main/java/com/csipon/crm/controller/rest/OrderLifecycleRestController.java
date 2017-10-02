package com.csipon.crm.controller.rest;

import com.csipon.crm.controller.message.MessageHeader;
import com.csipon.crm.controller.message.ResponseGenerator;
import com.csipon.crm.domain.model.User;
import com.csipon.crm.security.UserDetailsImpl;
import com.csipon.crm.service.entity.OrderLifecycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.csipon.crm.controller.message.MessageProperty.*;

/**
 * Created by Pasha on 11.05.2017.
 */
@RestController
public class OrderLifecycleRestController {

    private final OrderLifecycleService lifecycleService;
    private final ResponseGenerator<String> generator;

    @Autowired
    public OrderLifecycleRestController(OrderLifecycleService lifecycleService, ResponseGenerator<String> generator) {
        this.lifecycleService = lifecycleService;
        this.generator = generator;
    }

    @PutMapping("/orders/{id}/accept")
    @PreAuthorize("hasAnyRole('ROLE_CSR', 'ROLE_ADMIN')")
    public ResponseEntity<String> acceptOrder(@PathVariable Long id, Authentication authentication) {
        User user = (UserDetailsImpl) authentication.getPrincipal();
        boolean success = lifecycleService.processOrder(id, user.getId());
        if (success) {
            return generator.getHttpResponse(MessageHeader.SUCCESS_MESSAGE, SUCCESS_ORDER_ACCEPT, HttpStatus.OK);
        }
        return generator.getHttpResponse(MessageHeader.SUCCESS_MESSAGE, SUCCESS_ORDER_NOT_ACCEPT, HttpStatus.OK);
    }

    @PutMapping("/orders/{id}/activate")
    @PreAuthorize("hasAnyRole('ROLE_CSR', 'ROLE_ADMIN')")
    public ResponseEntity<String> activeOrder(@PathVariable Long id) {

        boolean success = lifecycleService.activateOrder(id);
        if (success) {
            return generator.getHttpResponse(MessageHeader.SUCCESS_MESSAGE, SUCCESS_ORDER_ACTIVATE, HttpStatus.OK);
        }
        return generator.getHttpResponse(MessageHeader.SUCCESS_MESSAGE, SUCCESS_ORDER_NOT_ACTIVATE, HttpStatus.OK);
    }

    @PutMapping("/orders/{id}/resume")
    @PreAuthorize("hasAnyRole('ROLE_CSR', 'ROLE_ADMIN')")
    public ResponseEntity<String> resumeOrder(@PathVariable Long id) {

        boolean success = lifecycleService.resumeOrder(id);
        if (success) {
            return generator.getHttpResponse(MessageHeader.SUCCESS_MESSAGE, SUCCESS_ORDER_RESUME, HttpStatus.OK);
        }
        return generator.getHttpResponse(MessageHeader.SUCCESS_MESSAGE, SUCCESS_ORDER_NOT_RESUME, HttpStatus.OK);
    }

    @PutMapping("/orders/{id}/pause")
    @PreAuthorize("hasAnyRole('ROLE_CSR', 'ROLE_ADMIN')")
    public ResponseEntity<String> pauseOrder(@PathVariable Long id) {

        boolean success = lifecycleService.pauseOrder(id);
        if (success) {
            return generator.getHttpResponse(MessageHeader.SUCCESS_MESSAGE, SUCCESS_ORDER_PAUSE, HttpStatus.OK);
        }
        return generator.getHttpResponse(MessageHeader.SUCCESS_MESSAGE, SUCCESS_ORDER_NOT_PAUSE, HttpStatus.OK);
    }

    @PutMapping("/orders/{id}/disable")
    @PreAuthorize("hasAnyRole('ROLE_CSR', 'ROLE_ADMIN')")
    public ResponseEntity<String> disableOrder(@PathVariable Long id) {

        boolean success = lifecycleService.disableOrder(id);
        if (success) {
            return generator.getHttpResponse(MessageHeader.SUCCESS_MESSAGE, SUCCESS_ORDER_DISABLE, HttpStatus.OK);
        }
        return generator.getHttpResponse(MessageHeader.SUCCESS_MESSAGE, SUCCESS_ORDER_NOT_DISABLE, HttpStatus.OK);
    }

    @PutMapping("/orders/{id}/request-disable")
    @PreAuthorize("hasAnyRole('ROLE_CSR', 'ROLE_ADMIN', 'ROLE_CUSTOMER')")
    public ResponseEntity<String> requestDisableOrder(@PathVariable Long id) {

        boolean success = lifecycleService.requestToDisableOrder(id);
        if (success) {
            return generator.getHttpResponse(MessageHeader.SUCCESS_MESSAGE, SUCCESS_ORDER_REQUEST_DISABLE, HttpStatus.OK);
        }
        return generator.getHttpResponse(MessageHeader.SUCCESS_MESSAGE, SUCCESS_ORDER_ERROR_REQUEST_DISABLE, HttpStatus.OK);
    }

    @PutMapping("/orders/{id}/request-pause")
    @PreAuthorize("hasAnyRole('ROLE_CSR', 'ROLE_ADMIN', 'ROLE_CUSTOMER')")
    public ResponseEntity<String> requestPauseOrder(@PathVariable Long id) {

        boolean success = lifecycleService.requestToPauseOrder(id);
        if (success) {
            return generator.getHttpResponse(MessageHeader.SUCCESS_MESSAGE, SUCCESS_ORDER_REQUEST_PAUSE, HttpStatus.OK);
        }
        return generator.getHttpResponse(MessageHeader.SUCCESS_MESSAGE, SUCCESS_ORDER_ERROR_REQUEST_PAUSE, HttpStatus.OK);
    }

    @PutMapping("/orders/{id}/request-resume")
    @PreAuthorize("hasAnyRole('ROLE_CSR', 'ROLE_ADMIN', 'ROLE_CUSTOMER')")
    public ResponseEntity<String> requestActivateOrder(@PathVariable Long id) {

        boolean success = lifecycleService.requestToResumeOrder(id);
        if (success) {
            return generator.getHttpResponse(MessageHeader.SUCCESS_MESSAGE, SUCCESS_ORDER_REQUEST_RESUME, HttpStatus.OK);
        }
        return generator.getHttpResponse(MessageHeader.SUCCESS_MESSAGE, SUCCESS_ORDER_ERROR_REQUEST_RESUME, HttpStatus.OK);
    }
}
