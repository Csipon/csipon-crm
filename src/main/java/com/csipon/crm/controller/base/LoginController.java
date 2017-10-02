package com.csipon.crm.controller.base;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@RequestParam(value = "logout", required = false) String logout,
                        HttpServletRequest request, Model model) throws Throwable {
        if (logout != null) {
            model.addAttribute("msg", "You've been logged out successfully.");
        }else if (request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION") != null){
            throw (Throwable) request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        }
        return "login";
    }

}
