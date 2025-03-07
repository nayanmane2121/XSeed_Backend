package com.tcognition.emailservice.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
public class EmailConfig {

    @Autowired
    private JavaMailSender javaMailSender;

    public JavaMailSender configureMailSender() {
        if (javaMailSender instanceof JavaMailSenderImpl) {
            JavaMailSenderImpl mailSender = (JavaMailSenderImpl) javaMailSender;

            
            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.debug", "true"); 
        }

        return javaMailSender;
    }
}
