package com.csipon.crm.controller.base;

import com.csipon.crm.domain.model.User;
import com.csipon.crm.exception.NoSuchEmailException;
import com.csipon.crm.service.impl.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.mail.MessagingException;

/**
 * Created by Pasha on 26.04.2017.
 */
@Controller
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    @Autowired
    public ForgotPasswordController(ForgotPasswordService forgotPasswordService) {
        this.forgotPasswordService = forgotPasswordService;
    }


    @RequestMapping(value = "/forgot", method = RequestMethod.POST)
    public String checkEmailAndPhone(String email, String phone, Model model) throws NoSuchEmailException, MessagingException {
        User user = forgotPasswordService.checkEmailAndPhone(email, phone);
        forgotPasswordService.changePassword(user);
        model.addAttribute("msg", "You are successful recovery password");

        return "login";
    }
}
