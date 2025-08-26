package com.naitei.group3.movie_ticket_booking_system.service.impl;

import com.naitei.group3.movie_ticket_booking_system.dto.request.RegisterRequestDTO;
import com.naitei.group3.movie_ticket_booking_system.entity.VerificationToken;
import com.naitei.group3.movie_ticket_booking_system.enums.RoleType;
import com.naitei.group3.movie_ticket_booking_system.exception.RoleNotFoundException;
import com.naitei.group3.movie_ticket_booking_system.exception.UserAlreadyExistsException;
import com.naitei.group3.movie_ticket_booking_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import com.naitei.group3.movie_ticket_booking_system.exception.*;
import com.naitei.group3.movie_ticket_booking_system.repository.VerificationTokenRepository;
import com.naitei.group3.movie_ticket_booking_system.service.EmailService;
import com.naitei.group3.movie_ticket_booking_system.utils.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.naitei.group3.movie_ticket_booking_system.entity.Role;
import com.naitei.group3.movie_ticket_booking_system.entity.User;
import com.naitei.group3.movie_ticket_booking_system.repository.RoleRepository;
import com.naitei.group3.movie_ticket_booking_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository tokenRepository;
    private final EmailService emailService;
    private final MessageUtil messageUtil;

    @Value("${app.role-id.admin}")
    private Long adminRoleId;

    @Value("${app.role-id.user}")
    private Long userRoleId;

    @Value("${app.verification.expire-hours:24}")
    private int verificationExpireHour;

    @Value("${app.verification.url:http://localhost:8080/api/v1/auth/verify?token=}")
    private String verificationUrl;

    @Override
    @Transactional
    public User registerUser(RegisterRequestDTO dto, RoleType roleType) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new UserAlreadyExistsException(
                    messageUtil.getMessage("error.user.exists")
            );
        }
        Long roleId = switch (roleType) {
            case ADMIN -> adminRoleId;
            case USER -> userRoleId;
        };
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException(
                        messageUtil.getMessage("error.role.notfound")
                ));
        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .address(dto.getAddress())
                .dateOfBirth(dto.getDateOfBirth())
                .gender(dto.getGender())
                .phone(dto.getPhone())
                .role(role)
                .isVerified(false)
                .point(0)
                .build();

        User savedUser = userRepository.save(user);

        // create token
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(savedUser);
        verificationToken.setExpiresAt(LocalDateTime.now().plusHours(verificationExpireHour));
        verificationToken.setUsed(false);
        tokenRepository.save(verificationToken);

        // send mail
        String link = verificationUrl + token;
        emailService.sendVerificationEmail(savedUser.getEmail(), link);

        return savedUser;
    }

    @Override
    @Transactional
    public void verifyEmail(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException(
                        messageUtil.getMessage("error.token.invalid")
                ));

        if (verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ExpiredTokenException(
                    messageUtil.getMessage("error.token.expired")
            );
        }
        if (Boolean.TRUE.equals(verificationToken.getUsed())) {
            throw new UsedTokenException(
                    messageUtil.getMessage("error.token.used")
            );
        }

        User user = verificationToken.getUser();
        user.setIsVerified(true);
        userRepository.save(user);

        verificationToken.setUsed(true);
        tokenRepository.save(verificationToken);
    }
}
