package com.csipon.crm.controller.base.role;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @author Karpunets
 * @since 25.04.2017
 */

@RequestMapping(value = "/ROLE_CSR")
@Controller
public class CSRController {

    @GetMapping("/")
    public String main(Map<String, Object> model) {
        return "home";
    }

    @GetMapping("/dashboard")
    public String dashboard(Map<String, Object> model) {
        return "dashboard/statistics";
    }

    @GetMapping("/orders")
    public String orders(Map<String, Object> model) {
        return "orders";
    }

    @GetMapping("/products")
    public String products(Map<String, Object> model) {
        return "products";
    }

    @GetMapping("/users")
    public String users(Map<String, Object> model) {
        return "users";
    }

    @GetMapping("/discounts")
    public String discounts(Map<String, Object> model) {
        return "discounts";
    }

    @GetMapping("/groups")
    public String groups(Map<String, Object> model) {
        return "groups";
    }

    @GetMapping("/regions")
    public String regions(Map<String, Object> model) {
        return "regions";
    }

    @GetMapping("/messages")
    public String message(Map<String, Object> model) {
        return "messages";
    }

    @GetMapping("/report")
    public String excel(Map<String, Object> model) {
        return "report";
    }
    @GetMapping("/settings")
    public String settings(Map<String, Object> model) {
        return "settings";
    }

}
