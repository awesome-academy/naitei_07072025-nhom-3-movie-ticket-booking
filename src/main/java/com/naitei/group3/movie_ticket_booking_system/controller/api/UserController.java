package com.naitei.group3.movie_ticket_booking_system.controller.api;

import com.naitei.group3.movie_ticket_booking_system.dto.request.ChangePasswordRequest;
import com.naitei.group3.movie_ticket_booking_system.dto.response.BaseApiResponse;
import com.naitei.group3.movie_ticket_booking_system.security.CustomUserPrincipal;
import com.naitei.group3.movie_ticket_booking_system.service.PasswordService;
import com.naitei.group3.movie_ticket_booking_system.dto.request.UserUpdateRequest;
import com.naitei.group3.movie_ticket_booking_system.dto.response.UserDTO;
import com.naitei.group3.movie_ticket_booking_system.entity.User;
import com.naitei.group3.movie_ticket_booking_system.security.CustomUserPrincipal;
import com.naitei.group3.movie_ticket_booking_system.service.UserService;
import com.naitei.group3.movie_ticket_booking_system.utils.MessageUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController("apiUserController")
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final MessageUtil messageUtil;
    private final PasswordService passwordService;

    @PostMapping("/change-password")
    public ResponseEntity<BaseApiResponse<String>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        passwordService.changePassword(principal.getId(), request.getOldPassword(), request.getNewPassword());

        return ResponseEntity.ok(
                new BaseApiResponse<>(HttpStatus.OK.value(),
                        messageUtil.getMessage("email.change.password.success")
                )
        );
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal CustomUserPrincipal principal) {
        User user = userService.getProfile(principal.getId());
        return ResponseEntity.ok(UserDTO.fromEntity(user));
    }

    @PutMapping("/me")
    public ResponseEntity<UserDTO> updateProfile(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @Valid @RequestBody UserUpdateRequest req
    ) {
        User updated = userService.updateProfile(principal.getId(), req);
        return ResponseEntity.ok(UserDTO.fromEntity(updated));
    }
}
