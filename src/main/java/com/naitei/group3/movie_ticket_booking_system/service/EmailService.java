package com.naitei.group3.movie_ticket_booking_system.service;

import com.naitei.group3.movie_ticket_booking_system.entity.Booking;
import org.thymeleaf.context.Context;

public interface EmailService {
    void sendVerificationEmail(String to, String link);
    void sendPasswordResetEmail(String to, String link);
    void sendBookingConfirmationEmail(Booking booking);
}
