package com.naitei.group3.movie_ticket_booking_system.dto.response;

import com.naitei.group3.movie_ticket_booking_system.enums.RatingStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RatingDTO(
        Long userId,
        String userName,
        Long movieId,
        String movieTitle,
        Integer stars,
        String comment,
        RatingStatus status,
        LocalDateTime createdAt
) {
}
