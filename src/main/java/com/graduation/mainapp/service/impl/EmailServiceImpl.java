package com.graduation.mainapp.service.impl;

import com.graduation.mainapp.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class EmailServiceImpl implements EmailService {
    @Value("${email}")
    private String email;

    private final JavaMailSender mailSender;

    @Override
    public void sendInvoiceViaMail(String to, String subject, String body, byte[] pdfReport, String fileName) {
        mailSender.send(mimeMessage -> {
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mimeMessage.setFrom(new InternetAddress(email));
            mimeMessage.setSubject(subject);
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.addAttachment(fileName, new ByteArrayResource(pdfReport));
            helper.setText(body, true);
        });
    }

}
