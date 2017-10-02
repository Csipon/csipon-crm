package com.csipon.crm.controller.rest;

import com.csipon.crm.dto.OrderViewDto;
import com.csipon.crm.security.UserDetailsImpl;
import com.csipon.crm.service.entity.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Pasha on 13.05.2017.
 */
@RestController
public class MessageRestController {

    private final OrderService orderService;

    @Autowired
    public MessageRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/messages/activate")
    @PreAuthorize("hasAnyRole('ROLE_CSR')")
    public List<OrderViewDto> fetchActivateMessages(Authentication authentication) {
        return orderService.getCsrActivateOrder(getPrincipalId(authentication));
    }

    @GetMapping("/messages/pause")
    @PreAuthorize("hasAnyRole('ROLE_CSR')")
    public List<OrderViewDto> fetchPauseMessages(Authentication authentication) {
        return orderService.getCsrPauseOrder(getPrincipalId(authentication));
    }

    @GetMapping("/messages/resume")
    @PreAuthorize("hasAnyRole('ROLE_CSR')")
    public List<OrderViewDto> fetchResumeMessages(Authentication authentication) {
        return orderService.getCsrResumeOrder(getPrincipalId(authentication));
    }

    @GetMapping("/messages/disable")
    @PreAuthorize("hasAnyRole('ROLE_CSR')")
    public List<OrderViewDto> fetchDisableMessages(Authentication authentication) {
        return orderService.getCsrDisableOrder(getPrincipalId(authentication));
    }

    @GetMapping("/messages/count")
    @PreAuthorize("hasAnyRole('ROLE_CSR')")
    public Integer checkCountOrders(Authentication authentication) {
        return orderService.getCsrOrderCount(getPrincipalId(authentication));
    }

    private Long getPrincipalId(Authentication authentication){
        Long csrId = null;
        if (authentication.getPrincipal() instanceof UserDetailsImpl) {
            csrId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        }
        return csrId;
    }
}
