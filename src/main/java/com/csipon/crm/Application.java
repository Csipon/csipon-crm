package com.csipon.crm;

import com.csipon.crm.datagenerator.GeneratorDbData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Application {

//    @Autowired
//    private static GeneratorDbData generator;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
//        generator.generateDataForDB(1);
    }
}
