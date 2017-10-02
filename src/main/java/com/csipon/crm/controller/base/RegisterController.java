package com.csipon.crm.controller.base;

import com.csipon.crm.service.entity.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Pasha on 02.05.2017.
 */
@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user/registration/confirm", method = RequestMethod.GET)
    public String confirmRegistration(String token, Model model) {
        if (userService.activateUser(token)) {
            model.addAttribute("msg", "You are successful activate you account!");
            return "login";
        }
        return "error";
    }
}
