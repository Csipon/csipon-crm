package com.csipon.crm.controller.base.role;

import com.csipon.crm.domain.model.Order;
import com.csipon.crm.domain.model.User;
import com.csipon.crm.domain.model.UserRole;
import com.csipon.crm.security.UserDetailsImpl;
import com.csipon.crm.service.entity.ComplaintService;
import com.csipon.crm.service.entity.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * @author Karpunets
 * @since 25.04.2017
 */

@RequestMapping(value = "/ROLE_CUSTOMER")
@Controller
public class CustomerController {

    @Autowired
    private ComplaintService complaintService;
    @Autowired
    private OrderService orderService;

    @GetMapping("/")
    public String main(Map<String, Object> model) {
        return "home";
    }

    @GetMapping("/organization")
    public String users(Map<String, Object> model, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        User user;
        if (principal instanceof UserDetailsImpl) {
            user = (UserDetailsImpl) principal;
            if (user.getUserRole() == UserRole.ROLE_CUSTOMER && user.isContactPerson()) {
                return "users";
            }
        }
        return "error/403";
    }

    @GetMapping("/orders")
    public String orders(Map<String, Object> model) {
        return "orders";
    }

    @GetMapping("/products")
    public String products(Map<String, Object> model) {
        return "products";
    }

    @GetMapping("/complaints")
    public String complaints(Map<String, Object> model, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        User customer = (UserDetailsImpl) principal;
        List<Order> orders = orderService.findByCustomer(customer);
        model.put("orders", orders);
        return "complaints";
    }

    @GetMapping("/settings")
    public String settings(Map<String, Object> model) {
        return "settings";
    }

}
