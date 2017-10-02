package com.csipon.crm.service.email.senders;


import com.csipon.crm.domain.model.Order;
import com.csipon.crm.exception.IncorrectEmailElementException;
import com.csipon.crm.service.email.AbstractEmailSender;
import com.csipon.crm.service.email.EmailParam;
import com.csipon.crm.service.email.EmailParamKeys;
import com.csipon.crm.service.email.EmailType;
import com.csipon.crm.service.email.builder.EmailBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Properties;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 14.04.2017
 */

@Service("orderSender")
public class OrderStatusEmailSender extends AbstractEmailSender {

    private static final Logger log = LoggerFactory.getLogger(OrderStatusEmailSender.class);

    //Name of template html file for email letter
    private String orderStatusTempl;
    //Subject for email letter
    private String orderStatusSubj;

    @Qualifier("emailProps")
    @Autowired
    private Properties properties;

    @Autowired
    private JavaMailSenderImpl sender;

    public String getOrderStatusTempl() {
        return orderStatusTempl;
    }

    public void setOrderStatusTempl(String orderStatusTempl) {
        this.orderStatusTempl = orderStatusTempl;
    }

    public String getOrderStatusSubj() {
        return orderStatusSubj;
    }

    public void setOrderStatusSubj(String orderStatusSubj) {
        this.orderStatusSubj = orderStatusSubj;
    }

    public OrderStatusEmailSender() {
    }

    @Async
    public void send(EmailParam emailParam) throws MessagingException {
        checkEmailMap(emailParam);
        Order order = getOrder(emailParam);

        String template = replace(getTemplate(orderStatusTempl), order);
        EmailBuilder emailBuilder = new EmailBuilder();
        emailBuilder.setProperties(properties);
        log.debug("Start building  email letter");
        emailBuilder.setSubject(orderStatusSubj);
        emailBuilder.setAddress(order.getCustomer().getEmail());
        emailBuilder.setContent(template);
        log.debug("Sending email");
        sender.send(emailBuilder.generateMessage());
    }

    private Order getOrder(EmailParam emailParam) {
        Object o = emailParam.get(EmailParamKeys.ORDER);
        if (o instanceof Order){
            return (Order) o;
        }else {
            throw new IncorrectEmailElementException("Expected by key 'order' in map will be order");
        }
    }

    private String replace(String templ, Order order) {
        log.debug("Start replacing values in email template file");
        return templ.replaceAll("%name%", order.getCustomer().getFirstName())
                .replaceAll("%surname%", order.getCustomer().getLastName())
                .replaceAll("%service%", order.getProduct().getTitle())
                .replaceAll("%status%", order.getStatus().name());
    }

    @Override
    protected void checkEmailMap(EmailParam emailParam) {
        if (EmailType.ORDER_STATUS != emailParam.getEmailType()){
            throw new IncorrectEmailElementException("Expected email type ORDER_STATUS but type " + emailParam.getEmailType());
        }
    }
}
