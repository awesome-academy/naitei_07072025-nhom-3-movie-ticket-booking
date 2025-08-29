package com.naitei.group3.movie_ticket_booking_system.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UserUpdateRequest(
        String name,

        @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "{error.user.phone.invalid}")
        String phone,

        String address,

        @Past(message = "{error.user.dob.past}")
        LocalDate dateOfBirth,

        @Min(value = 0, message = "{error.user.gender.range}")
        @Max(value = 2, message = "{error.user.gender.range}")
        Integer gender
) {}
