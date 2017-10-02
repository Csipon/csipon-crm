package com.csipon.crm.service.email.senders;

import com.csipon.crm.domain.model.User;
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


@Service("changeSender")
public class ChangeSender extends AbstractEmailSender {
    private static final Logger log = LoggerFactory.getLogger(ChangeSender.class);
    @Qualifier("emailProps")
    @Autowired
    private Properties properties;

    @Autowired
    private JavaMailSenderImpl mailSender;

    private String changeTemplate;
    private String changeSubject;

    @Async
    @Override
    public void send(EmailParam emailParam) throws MessagingException {
        checkEmailMap(emailParam);
        User user = getUser(emailParam);
        String type = getType(emailParam);
        String value = getValue(emailParam);
        prepareAndSendMail(user, type, value);
    }

    private void prepareAndSendMail(User user, String type, String value) throws MessagingException {
        String template = replace(getTemplate(changeTemplate), user, type, value);
        buildAndSend(user, changeSubject, template, type);
    }

    private void buildAndSend(User user, String subject, String body, String type) throws MessagingException {
        EmailBuilder emailBuilder = new EmailBuilder();
        emailBuilder.setProperties(properties);
        emailBuilder.setContent(body);
        emailBuilder.setAddress(user.getEmail());
        emailBuilder.setSubject(subject.replaceAll("%type%", type));
        mailSender.send(emailBuilder.generateMessage());
    }

    private String replace(String html, User user, String type, String value) {
        return html.replaceAll("%name%", user.getFirstName())
                .replaceAll("%surname%", user.getMiddleName())
                .replaceAll("%type%", type)
                .replaceAll("%value%", value);
    }

    @Override
    protected void checkEmailMap(EmailParam emailParam) {
        if (EmailType.CHANGE != emailParam.getEmailType()) {
            throw new IncorrectEmailElementException("Expected email type CHANGE but type is " + emailParam.getEmailType());
        }
    }

    private User getUser(EmailParam emailParam) {
        Object o = emailParam.get(EmailParamKeys.USER);
        if (o instanceof User){
            return (User) o;
        }else {
            throw new IncorrectEmailElementException("Email param type : " + o.getClass() + ", need type : " + EmailParamKeys.USER);
        }
    }

    private String getType(EmailParam emailParam) {
        String password = (String) emailParam.get(EmailParamKeys.CHANGE_TYPE);
        if (password == null) {
            log.error("Type can't be null");
            throw new IllegalStateException("Type is null");
        }
        return password;
    }

    private String getValue(EmailParam emailParam) {
        String password = (String) emailParam.get(EmailParamKeys.CHANGE_VALUE);
        if (password == null) {
            log.error("Value can't be null");
            throw new IllegalStateException("Value is null");
        }
        return password;
    }

    public String getChangeTemplate() {
        return changeTemplate;
    }

    public void setChangeTemplate(String changeTemplate) {
        this.changeTemplate = changeTemplate;
    }

    public String getChangeSubject() {
        return changeSubject;
    }

    public void setChangeSubject(String changeSubject) {
        this.changeSubject = changeSubject;
    }
}
