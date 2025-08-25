package com.naitei.group3.movie_ticket_booking_system.service.impl;

import com.naitei.group3.movie_ticket_booking_system.exception.EmailSendException;
import com.naitei.group3.movie_ticket_booking_system.service.EmailService;
import com.naitei.group3.movie_ticket_booking_system.utils.MessageUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final MessageUtil messageUtil;

    @Override
    public void sendVerificationEmail(String to, String link) {
        try {
            // context để inject biến vào template
            Context context = new Context();
            context.setVariable("VERIFICATION_LINK", link);
            context.setVariable("USER_EMAIL", to);

            Locale locale = LocaleContextHolder.getLocale();
            String lang = locale.getLanguage().equalsIgnoreCase("vi") ? "VI" : "EN";

            String templateFile = "email/verification-email_" + lang;

            String htmlContent = templateEngine.process(templateFile, context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(messageUtil.getMessage("email.verification.subject"));
            helper.setText(htmlContent, true);

            FileSystemResource res = new FileSystemResource(new File("src/main/resources/static/images/logo.png"));
            helper.addInline("logoImage", res);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new EmailSendException(messageUtil.getMessage("email.send.fail"));
        }
    }
}
