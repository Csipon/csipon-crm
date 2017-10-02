package com.csipon.crm.service.email.senders;

import com.csipon.crm.service.email.AbstractEmailSender;
import com.csipon.crm.service.email.EmailType;
import com.csipon.crm.domain.model.User;
import com.csipon.crm.exception.IncorrectEmailElementException;
import com.csipon.crm.service.email.EmailParam;
import com.csipon.crm.service.email.EmailParamKeys;
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


@Service("registrationSender")
public class RegSuccessEmailSender extends AbstractEmailSender {

    private static final Logger log = LoggerFactory.getLogger(RegSuccessEmailSender.class);

    //Name of template html file for email letter
    private String regSuccessTempl;
    //Subject for email letter
    private String regSuccessSubj;

    @Qualifier("emailProps")
    @Autowired
    private Properties properties;

    @Autowired
    private JavaMailSenderImpl sender;

    public RegSuccessEmailSender() {
    }

    public String getRegSuccessTempl() {
        return regSuccessTempl;
    }

    public void setRegSuccessTempl(String regSuccessTempl) {
        this.regSuccessTempl = regSuccessTempl;
    }

    public String getRegSuccessSubj() {
        return regSuccessSubj;
    }

    public void setRegSuccessSubj(String regSuccessSubj) {
        this.regSuccessSubj = regSuccessSubj;
    }


    @Async
    public void send(EmailParam emailParam) throws MessagingException {
        checkEmailMap(emailParam);
        User user = getUser(emailParam);
        String reference = getReference(emailParam);
        String password = getPassword(emailParam);

        String template = replace(user,reference, password, getTemplate(regSuccessTempl));
        EmailBuilder emailBuilder = new EmailBuilder();
        emailBuilder.setProperties(properties);
        log.debug("Start building email letter");
        emailBuilder.setSubject(regSuccessSubj);
        emailBuilder.setAddress(user.getEmail());
        emailBuilder.setContent(template);
        log.debug("Sending email");
        sender.send(emailBuilder.generateMessage());
    }

    private User getUser(EmailParam emailParam) {
        Object o = emailParam.get(EmailParamKeys.USER);
        if (o instanceof User){
            return (User) o;
        }else {
            throw new IncorrectEmailElementException("Email param type : " + o.getClass() + ", need type : " + EmailParamKeys.USER);
        }
    }

    private String getPassword(EmailParam emailParam) {
        String password = (String) emailParam.get(EmailParamKeys.USER_PASSWORD);
        if (password == null) {
            log.error("Password can't be null");
            throw new IllegalStateException("password is null");
        }
        return password;
    }

    private String getReference(EmailParam emailParam) {
        String reference = (String) emailParam.get(EmailParamKeys.USER_REFERENCE);
        if (reference == null) {
            log.error("Reference can't be null");
            throw new IllegalStateException("Reference is null");
        }
        return reference;
    }



    private String replace(User user, String reference, String password, String templ) {
        log.debug("Start replacing values in email template file");
        return templ.replaceAll("%email%", user.getEmail())
                .replaceAll("%name%", user.getFirstName())
                .replaceAll("%surname%", user.getLastName())
                .replace("%password%", password)
                .replaceAll("%reference%", reference);
    }

    @Override
    protected void checkEmailMap(EmailParam emailParam) {
        if (EmailType.REGISTRATION != emailParam.getEmailType()){
            throw new IncorrectEmailElementException("Expected email type REGISTRATION but type " + emailParam.getEmailType());
        }
    }
}
