package com.ourMenu.backend.domain.user.application;

import com.ourMenu.backend.domain.user.dao.EmailRedisRepository;
import com.ourMenu.backend.domain.user.domain.AuthEmailEntity;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Autowired
    private EmailRedisRepository emailRepository;

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

    private MimeMessage createAuthEmail(String address) {
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

    public void sendPasswordEmail(String address, String password) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, address);
            message.setSubject("[Ourmenu]임시 비밀번호");
            String body = "";
            body += "<h3>" + "임시 비밀번호" + "</h3>";
            body += "<h1>" + password + "</h1>";
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        javaMailSender.send(message);
    }

    private void saveCode(String address, String code) {
        AuthEmailEntity emailEntity = new AuthEmailEntity(address, code);
        emailRepository.save(emailEntity);
    }

    public String sendMail(String address) {
        MimeMessage message = createAuthEmail(address);
        javaMailSender.send(message);
        saveCode(address, code);

        return code;
    }

    public boolean confirmCode(String address, String code) {
        Optional<AuthEmailEntity> emailEntity = emailRepository.findById(address);
        return emailEntity.map(authEmailEntity -> authEmailEntity.getCode().equals(code)).orElse(false);
    }

}
