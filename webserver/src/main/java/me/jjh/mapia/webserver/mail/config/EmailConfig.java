package me.jjh.mapia.webserver.mail.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jjh.mapia.webserver.properties.MailProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 *packageName    : me.jjh.mapia.mail.config
 * fileName       : EmailConfig
 * author         : JJH
 * date           : 2025-02-24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-24        JJH       최초 생성
 */

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailConfig {

   private final MailProperties mailProperties;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailProperties.getHost());
        mailSender.setPort(mailProperties.getPort());
        mailSender.setUsername(mailProperties.getUsername());
        mailSender.setPassword(mailProperties.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", mailProperties.getProperties().get("mail.smtp.auth"));
        props.put("mail.smtp.ssl.enable", mailProperties.getProperties().get("mail.smtp.ssl.enable"));
        props.put("mail.smtp.starttls.enable", mailProperties.getProperties().get("mail.smtp.starttls.enable"));
        props.put("mail.smtp.starttls.required", mailProperties.getProperties().get("mail.smtp.starttls.required"));
        props.put("mail.debug", mailProperties.getProperties().get("mail.debug"));

        System.out.println("mailSender : " + mailSender);
        System.out.println("props : " + props);

        return mailSender;
    }
}
