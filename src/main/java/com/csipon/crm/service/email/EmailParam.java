package com.csipon.crm.service.email;

import com.csipon.crm.exception.IncorrectEmailElementException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pasha on 26.04.2017.
 */
public class EmailParam {
    private Map<EmailParamKeys, Object> emailMap;
    private EmailType emailType;


    public EmailParam(EmailType emailType) {
        if (emailType == null){
            throw new IncorrectEmailElementException("Email type == null");
        }
        this.emailType = emailType;
        emailMap = new HashMap<>();
    }


    public void put(EmailParamKeys key, Object value){
        emailMap.put(key, value);
    }


    public void remove(EmailParamKeys key){
        emailMap.remove(key);
    }

    public Object get(EmailParamKeys key){
        return emailMap.get(key);
    }

    public EmailType getEmailType() {
        return emailType;
    }

    public Map<EmailParamKeys, Object> getEmailMap() {
        return emailMap;
    }
}
