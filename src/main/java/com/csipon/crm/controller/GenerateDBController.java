package com.csipon.crm.controller;

import com.csipon.crm.datagenerator.GeneratorDbData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by Pasha on 10/4/2017.
 */

@Controller
public class GenerateDBController {
    @Autowired
    private GeneratorDbData generator;

    @GetMapping("/generate/{number}")
    public String generateDb(@PathVariable("number") int number){
        if (number == 65535){
            generator.generateDataForDB(1);
        }
        return "login";
    }
}
