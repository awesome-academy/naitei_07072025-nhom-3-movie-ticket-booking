package com.naitei.group3.movie_ticket_booking_system.dto.response;

import com.naitei.group3.movie_ticket_booking_system.entity.User;

import java.time.LocalDate;

public record UserDTO(
        Long id,
        String name,
        String email,
        String phone,
        String address,
        LocalDate dateOfBirth,
        Integer gender
) {
    public static UserDTO fromEntity(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getDateOfBirth(),
                user.getGender()
        );
    }
}
