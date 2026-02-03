package com.peluqueria.api.adapter.in.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * The Class MailConfig.
 */
@Configuration
public class MailConfig {

    /**
     * Java mail sender.
     *
     * @return the java mail sender
     */
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        
        // Configuración de Gmail
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("raulrrf03@gmail.com");
        mailSender.setPassword("naqr ozyy dqus sodb");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true"); // para ver logs de SMTP

        return mailSender;
    }
}
