package com.csipon.crm.service.email.builder;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.annotation.Resource;
import java.util.Properties;

/**
 * Created by Pasha on 14.04.2017.
 */
@Configuration
@PropertySource("classpath:email.properties")
public class EmailConfig {
    private static final String PROPERTY_EMAIL_USERNAME = "mail.username" ;
    private static final String PROPERTY_EMAIL_PASSWORD = "mail.password";
    private static final String PROPERTY_EMAIL_AUTH = "mail.smtp.auth";
    private static final String PROPERTY_EMAIL_HOST = "mail.smtp.host";
    private static final String PROPERTY_EMAIL_PORT = "mail.smtp.port";
    private static final String PROPERTY_EMAIL_SOCKET_FACTORY_CLASS = "mail.smtp.socketFactory.class";

    @Resource
    private Environment env;

    @Bean
    public JavaMailSenderImpl mailSender(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(env.getProperty(PROPERTY_EMAIL_HOST));
        javaMailSender.setUsername(env.getProperty(PROPERTY_EMAIL_USERNAME));
        javaMailSender.setPassword(env.getProperty(PROPERTY_EMAIL_PASSWORD));
        javaMailSender.setJavaMailProperties(getProperties());

        return javaMailSender;
    }

    @Qualifier("emailProps")
    @Bean
    public Properties getProperties(){
        Properties props = new Properties();
        props.put(PROPERTY_EMAIL_USERNAME, env.getProperty(PROPERTY_EMAIL_USERNAME));
        props.put(PROPERTY_EMAIL_PASSWORD, env.getProperty(PROPERTY_EMAIL_PASSWORD));
        props.put(PROPERTY_EMAIL_AUTH, env.getProperty(PROPERTY_EMAIL_AUTH));
        props.put(PROPERTY_EMAIL_HOST, env.getProperty(PROPERTY_EMAIL_HOST));
        props.put(PROPERTY_EMAIL_PORT, env.getProperty(PROPERTY_EMAIL_PORT));
        props.put(PROPERTY_EMAIL_PORT, env.getProperty(PROPERTY_EMAIL_PORT));
        props.put(PROPERTY_EMAIL_SOCKET_FACTORY_CLASS, env.getProperty(PROPERTY_EMAIL_SOCKET_FACTORY_CLASS));
        return props;
    }
}
