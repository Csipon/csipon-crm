package com.csipon.crm.service.email;

import com.csipon.crm.exception.IncorrectEmailElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.mail.MessagingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Melnyk_Dmytro
 * @version 1.0
 * @since 17.04.2017
 */

@ConfigurationProperties("mail.templates")
@PropertySource("classpath:email.properties")
public abstract class AbstractEmailSender {

    private static final Logger log = LoggerFactory.getLogger(AbstractEmailSender.class);

    private static final String TEMPLATE_PACKAGE = "email/concreteTemplate";

    public abstract void send(EmailParam emailParam) throws MessagingException, IncorrectEmailElementException;

    protected abstract void checkEmailMap(EmailParam emailParam) throws IncorrectEmailElementException;

    public String getTemplate(String template) {
        String concreteTemplate = TEMPLATE_PACKAGE.replace("concreteTemplate", template);
        log.debug("Getting email template " + concreteTemplate);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Resource resource = new ClassPathResource(concreteTemplate);
            BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()), 1024);
            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

//    for local profile
//    public String getTemplate(String template) {
//        String concreteTemplate = TEMPLATE_PACKAGE.replace("concreteTemplate", template);
//        log.debug("Getting email template " + concreteTemplate);
//        StringBuilder stringBuilder = new StringBuilder();
//        File file = new File(getClass().getClassLoader().getResource(concreteTemplate).getFile());
//        if (file.exists()) {
//            try {
//                List<String> lines = Files.readAllLines(Paths.get(file.getCanonicalPath()));
//                for (String line : lines) {
//                    stringBuilder.append(line);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            log.error("File " + concreteTemplate + " not found");
//        }
//        return stringBuilder.toString();
//    }
}
