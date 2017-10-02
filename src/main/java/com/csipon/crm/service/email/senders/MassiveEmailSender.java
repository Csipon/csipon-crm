package com.csipon.crm.service.email.senders;

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
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by Pasha on 14.04.2017.
 */
@Service("massiveSender")
public class MassiveEmailSender extends AbstractEmailSender {

    private static final Logger log = LoggerFactory.getLogger(MassiveEmailSender.class);

    private String informationAll;

    @Autowired
    private JavaMailSenderImpl mailSender;
    @Qualifier("emailProps")
    @Autowired
    private Properties properties;

    @Async
    public void send(EmailParam emailParam) throws MessagingException {
        checkEmailMap(emailParam);
        String[] receivers = getReceivers(emailParam);
        String subject = getSubject(emailParam);
        String body = getBody(emailParam);
        String bodyText = replace(getTemplate(informationAll), body);
        sendMails(createMessage(receivers, subject, bodyText));
    }

    private String[] getReceivers(EmailParam emailParam){
        String[] receivers = (String[]) emailParam.get(EmailParamKeys.RECEIVERS);
        if (receivers == null || receivers.length == 0){
            log.error("Receivers can't be null or have zero length");
            throw new IllegalStateException("receivers is null or no recipient");
        }
        return receivers;
    }

    private String getSubject(EmailParam emailParam){
        String subject = (String) emailParam.get(EmailParamKeys.SUBJECT);
        if (subject == null){
            log.error("Subject can't be null");
            throw new IllegalStateException("subject is null");
        }
        return subject;
    }

    private String getBody(EmailParam emailParam){
        String body = (String) emailParam.get(EmailParamKeys.BODY);
        if (body == null){
            log.error("Body can't be null");
            throw new IllegalStateException("body is null");
        }
        return body;
    }


    private MimeMessage createMessage(String[] to, String subject, String bodyText) throws MessagingException {
        EmailBuilder emailBuilder = new EmailBuilder();
        emailBuilder.setProperties(properties);
        log.debug("Start building email letter");
        emailBuilder.setAllAddress(to);
        emailBuilder.setSubject(subject);
        emailBuilder.setContent(bodyText);
        return emailBuilder.generateMessage();
    }

    private void sendMails(MimeMessage message) throws MessagingException {
        log.debug("Sending emails");
        mailSender.send(message);
    }

    String replace(String html, String body) {
        log.debug("Start replacing values in email template file");
        return html.replaceAll("%information%", body);
    }

    public String getInformationAll() {
        return informationAll;
    }

    public void setInformationAll(String informationAll) {
        this.informationAll = informationAll;
    }

    @Override
    protected void checkEmailMap(EmailParam emailParam) {
        if (EmailType.MASSIVE != emailParam.getEmailType()){
            throw new IncorrectEmailElementException("Expected email type MASSIVE but type " + emailParam.getEmailType());
        }
    }
}
