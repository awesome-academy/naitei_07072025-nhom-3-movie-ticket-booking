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
        Context context = new Context();
        context.setVariable("VERIFICATION_LINK", link);
        context.setVariable("USER_EMAIL", to);

        sendEmail(
                to,
                messageUtil.getMessage("email.verification.subject"),
                "email/verification-email",
                context
        );
    }

    @Override
    public void sendPasswordResetEmail(String to, String link) {
        Context context = new Context();
        context.setVariable("RESET_PASSWORD_LINK", link);
        context.setVariable("USER_EMAIL", to);

        sendEmail(
                to,
                messageUtil.getMessage("email.change.password.subject"),
                "email/reset-password-email",
                context
        );
    }

    private void sendEmail(String to, String subject, String templateBase, Context context) {
        try {
            // pick language
            Locale locale = LocaleContextHolder.getLocale();
            String lang = locale.getLanguage().equalsIgnoreCase("vi") ? "VI" : "EN";

            // template file
            String templateFile = templateBase + "_" + lang;

            // render template
            String htmlContent = templateEngine.process(templateFile, context);

            // build message
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            FileSystemResource res = new FileSystemResource(
                    new File("src/main/resources/static/images/logo.png")
            );
            helper.addInline("logoImage", res);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new EmailSendException(messageUtil.getMessage("email.send.fail"));
        }
    }
}
