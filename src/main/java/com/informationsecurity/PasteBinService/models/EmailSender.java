package com.informationsecurity.PasteBinService.models;

public interface EmailSender {
    void sendVerificationEmail(Email email);
    void sendEmailToModerator(Email message);
}
