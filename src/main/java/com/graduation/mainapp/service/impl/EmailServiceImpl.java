package com.graduation.mainapp.service.impl;

import com.graduation.mainapp.service.EmailService;
import com.graduation.mainapp.service.ExportService;
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
    private final ExportService exportService;

    @Override
    public void sendMailWithAttachment(String to, String subject, String body, String fileToAttach) {
        mailSender.send(mimeMessage -> {
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mimeMessage.setFrom(new InternetAddress(email));
            mimeMessage.setSubject(subject);
            byte[] dailyOrdersBytes = exportService.exportDailyOrders(3L);
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.addAttachment("daily-orders.pdf", new ByteArrayResource(dailyOrdersBytes));
            helper.setText("", true);
        });
    }

}
