package com.informationsecurity.PasteBinService.services;

import com.informationsecurity.PasteBinService.models.Email;
import com.informationsecurity.PasteBinService.models.EmailSender;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderService implements EmailSender {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void send(Email email) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email.getRecipient());
        mailMessage.setSubject(email.getSubject());
        mailMessage.setText(email.getMessage());
        mailMessage.setFrom(from);

        mailSender.send(mailMessage);
    }
}
