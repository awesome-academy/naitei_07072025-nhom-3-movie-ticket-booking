package com.naitei.group3.movie_ticket_booking_system.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordRequest {
    @NotBlank(message = "{valid.email.notblank}")
    @Email
    private String email;

    @NotBlank(message = "{valid.password.notblank}")
    @Size(min = 6, max = 100, message = "{valid.password.length}")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
            message = "{valid.password.strong}"
    )
    private String newPassword;
}
