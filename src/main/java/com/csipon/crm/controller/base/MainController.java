package com.csipon.crm.controller.base;

import com.csipon.crm.domain.model.User;
import com.csipon.crm.security.UserDetailsImpl;
import com.csipon.crm.service.entity.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

/**
 * @author Karpunets
 * @since 25.04.2017
 */

@Controller
public class MainController {

    private final UserService userService;

    @Autowired
    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String main(Map<String, Object> model, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        User user = (UserDetailsImpl) principal;
        model.put("user", user);
        model.put("avatar", userService.getAvatar(user.getId()));
        return "main";
    }

    @GetMapping("/400")
    public String badRequest() {
        return "error/400";
    }

    @GetMapping("/404")
    public String notFound() {
        return "error/404";
    }

    @GetMapping("/403")
    public String forbidden() {
        return "error/403";
    }

}