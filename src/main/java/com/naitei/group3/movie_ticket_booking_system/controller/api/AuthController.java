package com.naitei.group3.movie_ticket_booking_system.controller.api;

import com.naitei.group3.movie_ticket_booking_system.dto.request.ForgotPasswordRequest;
import com.naitei.group3.movie_ticket_booking_system.dto.request.RegisterRequestDTO;
import com.naitei.group3.movie_ticket_booking_system.dto.response.BaseApiResponse;
import com.naitei.group3.movie_ticket_booking_system.dto.response.UserResponseDTO;
import com.naitei.group3.movie_ticket_booking_system.enums.RoleType;
import com.naitei.group3.movie_ticket_booking_system.service.PasswordService;
import com.naitei.group3.movie_ticket_booking_system.utils.MessageUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.naitei.group3.movie_ticket_booking_system.entity.User;
import com.naitei.group3.movie_ticket_booking_system.service.UserService;

@RestController("apiAuthController")
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final MessageUtil messageUtil;
    private final PasswordService passwordService;

    @PostMapping("/register")
    public ResponseEntity<BaseApiResponse<UserResponseDTO>> register(@Valid @RequestBody RegisterRequestDTO request) {
        User savedUser = userService.registerUser(request, RoleType.USER);

        UserResponseDTO responseDTO = new UserResponseDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getRole().getName()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new BaseApiResponse<>(HttpStatus.CREATED.value(), responseDTO)
        );
    }

    @GetMapping("/verify")
    public ResponseEntity<BaseApiResponse<String>> verifyEmail(@RequestParam("token") String token) {
        userService.verifyEmail(token);
        return ResponseEntity.ok(
                new BaseApiResponse<>(HttpStatus.OK.value(), messageUtil.getMessage("email.verified"))
        );
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<BaseApiResponse<String>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request
    ) {
        passwordService.sendPasswordResetEmail(request.getEmail(), request.getNewPassword());
        return ResponseEntity.ok(
                new BaseApiResponse<>(HttpStatus.OK.value(),
                        messageUtil.getMessage("email.reset.sent")
                )
        );
    }

    @GetMapping("/reset-password")
    public ResponseEntity<BaseApiResponse<String>> resetPassword(@RequestParam("token") String token) {
        passwordService.confirmPasswordChange(token);
        return ResponseEntity.ok(
                new BaseApiResponse<>(HttpStatus.OK.value(),
                        messageUtil.getMessage("email.change.password.success")
                )
        );
    }
}
