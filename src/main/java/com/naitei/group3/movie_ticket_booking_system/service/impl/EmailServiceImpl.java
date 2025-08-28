package com.naitei.group3.movie_ticket_booking_system.service.impl;

import com.naitei.group3.movie_ticket_booking_system.entity.*;
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
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Override
    public void sendBookingConfirmationEmail(Booking booking) {
        User user = booking.getUser();
        Showtime showtime = booking.getShowtime();
        Hall hall = showtime.getHall();
        Cinema cinema = hall.getCinema();

        Set<String> seatCodes = booking.getBookingSeats().stream()
                .map(bs -> bs.getSeat().getSeatColumn())
                .collect(Collectors.toSet());

        Context context = new Context();
        context.setVariable("USER_EMAIL", user.getEmail());
        context.setVariable("BOOKING_ID", booking.getId());
        context.setVariable("MOVIE_TITLE", showtime.getMovie().getName());
        context.setVariable("CINEMA_NAME", cinema.getName());
        context.setVariable("CINEMA_ADDRESS", cinema.getAddress());
        context.setVariable("HALL_NAME", hall.getName());
        context.setVariable("SHOWTIME_START", showtime.getStartTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        context.setVariable("SEATS", String.join(", ", seatCodes));
        context.setVariable("TOTAL_PRICE", booking.getTotalPrice());

        sendEmail(
                user.getEmail(),
                messageUtil.getMessage("email.booking.confirm.subject"),
                "email/booking-confirmation",
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
