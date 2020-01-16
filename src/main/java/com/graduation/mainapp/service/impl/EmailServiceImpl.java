package com.graduation.mainapp.service.impl;

import com.graduation.mainapp.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import java.io.File;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class EmailServiceImpl implements EmailService {
    @Value("${email}")
    private String email;

    private final JavaMailSender mailSender;

    @Override
    public void sendMail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    @Override
    public void sendMailWithAttachment(String to, String subject, String body, String fileToAttach) {
        MimeMessagePreparator preparator = mimeMessage -> {
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mimeMessage.setFrom(new InternetAddress(email));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(body);

            FileSystemResource file = new FileSystemResource(new File(fileToAttach));
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.addAttachment(Objects.requireNonNull(file.getFilename()), file);
        };

        try {
            mailSender.send(preparator);
        } catch (MailException ex) {
            log.error("Could not send mail");
        }
    }

}
