package com.csipon.crm.controller.message;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by bpogo on 5/6/2017.
 */
@Component
public class ResponseGenerator<T> {
    private static final String MULTIPLE_REPLACE_WILD_CARD = "%param_?%";

    @Resource
    private Environment env;

    public ResponseEntity<T> getHttpResponse(HttpStatus httpStatus) {
        return new ResponseEntity<>(httpStatus);
    }

    public ResponseEntity<T> getHttpResponse(T obj, HttpStatus httpStatus) {
        return new ResponseEntity<>(obj, httpStatus);
    }


    public ResponseEntity<T> getHttpResponse(MessageHeader header, String messageProperty, HttpStatus httpStatus) {
        if (env.containsProperty(messageProperty)) {
            HttpHeaders headers = new HttpHeaders();
            headers.set(header.getHeaderName(), env.getProperty(messageProperty));
            return new ResponseEntity<>(headers, httpStatus);
        }
        return getHttpResponse(httpStatus);
    }

    public ResponseEntity<T> getHttpResponse(MessageHeader header, String messageProperty, String[] values, HttpStatus httpStatus) {
        if (env.containsProperty(messageProperty)) {
            HttpHeaders headers = new HttpHeaders();
            String message = getMessage(messageProperty, values);
            headers.set(header.getHeaderName(), message);
            return new ResponseEntity<>(headers, httpStatus);
        }
        return getHttpResponse(httpStatus);
    }

    private String getMessage(String messageProperty, String[] values) {
        String messageTemplate = env.getProperty(messageProperty);
        if (values != null) {
            String formattedMessage = messageTemplate;
            System.out.println(formattedMessage);
            for (int i = 0; i < values.length; i++) {
                String param = MULTIPLE_REPLACE_WILD_CARD.replace("?", String.valueOf(i));
                formattedMessage = formattedMessage.replace(param, values[i]);
            }
            System.out.println(formattedMessage);
            return formattedMessage;
        } else {
            return messageTemplate;
        }
    }

    public ResponseEntity<T> getHttpResponse(T obj, MessageHeader header, String messageProperty, HttpStatus httpStatus) {
        if (env.containsProperty(messageProperty)) {
            HttpHeaders headers = new HttpHeaders();
            headers.set(header.getHeaderName(), env.getProperty(messageProperty));
            return new ResponseEntity<>(obj, headers, httpStatus);
        }
        return getHttpResponse(obj, httpStatus);
    }

    public ResponseEntity<Long> getHttpResponse(Long objectId, MessageHeader header, String messageProperty, HttpStatus httpStatus) {
        if (env.containsProperty(messageProperty)) {
            HttpHeaders headers = new HttpHeaders();
            headers.set(header.getHeaderName(), env.getProperty(messageProperty));
            return new ResponseEntity<>(objectId, headers, httpStatus);
        }
        return new ResponseEntity<>(objectId, httpStatus);
    }
}
