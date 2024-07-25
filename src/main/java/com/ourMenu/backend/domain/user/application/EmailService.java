package com.ourMenu.backend.domain.user.application;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    @Value("spring.mail.username")
    private String senderEmail;
    private String code;

    private String generateRandomCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int digit = random.nextInt(10);
            code.append(digit);
        }

        return code.toString();
    }

    public MimeMessage createAuthEmail(String address) {
        MimeMessage message = javaMailSender.createMimeMessage();
        code = generateRandomCode();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, address);
            message.setSubject("[Ourmenu]이메일 인증");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + code + "</h1>";
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return message;
    }

    public String sendMail(String address) {
        MimeMessage message = createAuthEmail(address);
        javaMailSender.send(message);

        return code;
    }

}
