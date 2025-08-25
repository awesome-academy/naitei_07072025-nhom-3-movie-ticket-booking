package com.naitei.group3.movie_ticket_booking_system.service;

public interface EmailService {
    void sendVerificationEmail(String to, String link);
}
