package com.naitei.group3.movie_ticket_booking_system.converter;

import com.naitei.group3.movie_ticket_booking_system.dto.response.RatingResponse;
import com.naitei.group3.movie_ticket_booking_system.entity.Rating;

public class RatingDTOConverter {

    public static RatingResponse toDTO(Rating rating) {
        if (rating == null) return null;

        return RatingResponse.builder()
                .movieId(rating.getMovie().getId())
                .userId(rating.getUser().getId())
                .stars(rating.getStars())
                .comment(rating.getComment())
                .status(rating.getStatus())
                .build();
    }
}
