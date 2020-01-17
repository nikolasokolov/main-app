package com.graduation.mainapp.service;

public interface EmailService {
    void sendMailWithAttachment(String to, String subject, String body, String fileToAttach);
}
