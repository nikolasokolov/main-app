package com.graduation.mainapp.service;

public interface EmailService {
    void sendMail(String to, String subject, String body);
    void sendMailWithAttachment(String to, String subject, String body, String fileToAttach);
}
