package com.csipon.crm.controller.rest;

import com.csipon.crm.controller.message.MessageHeader;
import com.csipon.crm.controller.message.MessageProperty;
import com.csipon.crm.controller.message.ResponseGenerator;
import com.csipon.crm.domain.model.Order;
import com.csipon.crm.domain.model.User;
import com.csipon.crm.domain.model.UserRole;
import com.csipon.crm.domain.request.OrderRowRequest;
import com.csipon.crm.dto.AutocompleteDto;
import com.csipon.crm.dto.GraphDto;
import com.csipon.crm.dto.OrderDto;
import com.csipon.crm.dto.OrderHistoryDto;
import com.csipon.crm.security.UserDetailsImpl;
import com.csipon.crm.service.entity.OrderService;
import com.csipon.crm.validation.BindingResultHandler;
import com.csipon.crm.validation.impl.OrderValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Pasha on 07.05.2017.
 */
@RestController
@RequestMapping(value = "/orders")
public class OrderRestController {
    private final ResponseGenerator<Order> generator;
    private final OrderService orderService;
    private final OrderValidator orderValidator;
    private final BindingResultHandler bindingResultHandler;

    @Autowired
    public OrderRestController(OrderService orderService, OrderValidator orderValidator,
                               ResponseGenerator<Order> generator, BindingResultHandler bindingResultHandler) {
        this.orderService = orderService;
        this.orderValidator = orderValidator;
        this.generator = generator;
        this.bindingResultHandler = bindingResultHandler;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<?> create(@Valid OrderDto orderDto, BindingResult bindingResult
            , Authentication authentication) {
        Object principal = authentication.getPrincipal();
        User user = (UserDetailsImpl) principal;
        orderDto.setCustomerId(user.getId());
        orderValidator.validate(orderDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return bindingResultHandler.handle(bindingResult);
        }
        Order order = orderService.create(orderDto);
        if (order.getId() > 0) {
            return generator.getHttpResponse(order.getId(), MessageHeader.SUCCESS_MESSAGE, MessageProperty.SUCCESS_ORDER_CREATED, HttpStatus.CREATED);
        }
        return generator.getHttpResponse(MessageHeader.ERROR_MESSAGE, MessageProperty.ERROR_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getOrderRows(OrderRowRequest orderRowRequest, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        User user = (UserDetailsImpl) principal;
        Map<String, Object> result;
        if (user.getUserRole() == UserRole.ROLE_CUSTOMER) {
            orderRowRequest.setCustomerId(user.getId());
            if (user.isContactPerson()) {
                orderRowRequest.setIsContactPerson(true);
            }
        }
        result = orderService.getOrdersRow(orderRowRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER')")
    public List<AutocompleteDto> getAutocompleteDto(String pattern, Authentication authentication,
                                                    @PathVariable(value = "userId") Long userId) {
        User customer = (UserDetailsImpl) authentication.getPrincipal();
        return orderService.getAutocompleteOrder(pattern, customer);
    }

    @GetMapping("/graph")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CSR')")
    public GraphDto getGraph(GraphDto graphDto) {
        return orderService.getStatisticalGraph(graphDto);
    }

    @GetMapping("/{id}/history")
    @PreAuthorize("hasAnyRole('ROLE_CSR', 'ROLE_CUSTOMER', 'ROLE_ADMIN')")
    public Set<OrderHistoryDto> orderHistory(@PathVariable Long id) {
        return orderService.getOrderHistory(id);
    }

}
