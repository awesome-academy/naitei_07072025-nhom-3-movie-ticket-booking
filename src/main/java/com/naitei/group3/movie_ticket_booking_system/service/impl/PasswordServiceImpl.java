package com.naitei.group3.movie_ticket_booking_system.service.impl;

import com.naitei.group3.movie_ticket_booking_system.entity.User;
import com.naitei.group3.movie_ticket_booking_system.entity.VerificationToken;
import com.naitei.group3.movie_ticket_booking_system.exception.*;
import com.naitei.group3.movie_ticket_booking_system.repository.UserRepository;
import com.naitei.group3.movie_ticket_booking_system.repository.VerificationTokenRepository;
import com.naitei.group3.movie_ticket_booking_system.service.EmailService;
import com.naitei.group3.movie_ticket_booking_system.service.PasswordService;
import com.naitei.group3.movie_ticket_booking_system.utils.MessageUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final MessageUtil messageUtil;

    @Value("${app.reset.password.url:http://localhost:8080/api/v1/auth/reset-password?token=}")
    private String resetPasswordUrl;

    @Value("${app.verification.expire-hours:24}")
    private int verificationExpireHour;

    @Transactional
    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageUtil.getMessage("error.user.notfound")
                ));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new InvalidOldPasswordException(messageUtil.getMessage("error.password.old.notmatch"));
        }

        user.setPassword(passwordEncoder.encode(newPassword));
    }

    @Transactional
    @Override
    public void sendPasswordResetEmail(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageUtil.getMessage("error.user.email.notfound")
                ));

        if (!user.getIsVerified()) throw new UserNotVerifiedException(messageUtil.getMessage("error.not.verified"));

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiresAt(LocalDateTime.now().plusHours(verificationExpireHour));
        verificationToken.setUsed(false);
        verificationToken.setType("PASSWORD_RESET");
        verificationToken.setExtraValue(passwordEncoder.encode(newPassword));
        tokenRepository.save(verificationToken);

        String link = resetPasswordUrl + verificationToken.getToken();
        emailService.sendPasswordResetEmail(user.getEmail(), link);
    }

    @Transactional
    public void confirmPasswordChange(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException(
                        messageUtil.getMessage("error.token.invalid")
                ));

        if (verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ExpiredTokenException(messageUtil.getMessage("error.token.expired"));
        }
        if (Boolean.TRUE.equals(verificationToken.getUsed())) {
            throw new UsedTokenException(messageUtil.getMessage("error.token.used"));
        }

        User user = verificationToken.getUser();
        user.setPassword(verificationToken.getExtraValue());
        userRepository.save(user);

        tokenRepository.delete(verificationToken);
    }
}
