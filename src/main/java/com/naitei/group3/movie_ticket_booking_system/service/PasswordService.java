package com.naitei.group3.movie_ticket_booking_system.service;

public interface PasswordService {
    void sendPasswordResetEmail(String email, String newPassword);
    void changePassword(Long userId, String oldPassword, String newPassword);
    void confirmPasswordChange(String token);
}
