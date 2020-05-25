package com.graduation.mainapp.service;

import javax.mail.MessagingException;

public interface EmailService {

    void sendInvoiceViaMail(String to, String subject, String body, byte[] pdfReport, String fileName) throws MessagingException;
}
