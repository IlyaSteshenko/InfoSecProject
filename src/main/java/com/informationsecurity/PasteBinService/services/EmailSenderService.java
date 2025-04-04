package com.informationsecurity.PasteBinService.services;

import com.informationsecurity.PasteBinService.models.Email;
import com.informationsecurity.PasteBinService.models.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderService implements EmailSender {

    private final JavaMailSender mailSender;

    @Override
    public void sendVerificationEmail(Email email) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email.getRecipient());
        mailMessage.setSubject(email.getSubject());
        mailMessage.setText(email.getMessage());
        mailMessage.setFrom("pobeda.sup@gmail.com");

        mailSender.send(mailMessage);
    }

    @Override
    public void sendEmailToModerator(Email message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo("pobeda.sup@gmail.com");
        mailMessage.setSubject(message.getSubject());
        mailMessage.setText(message.getMessage());
        mailMessage.setFrom(message.getRecipient());

        mailSender.send(mailMessage);
    }
}
