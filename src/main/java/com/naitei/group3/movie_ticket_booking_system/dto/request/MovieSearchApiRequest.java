package com.naitei.group3.movie_ticket_booking_system.dto.request;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MovieSearchApiRequest(
        String keyword,
        String genreName,
        LocalDate showDate
) {
    public static MovieSearchApiRequest of(
            String keyword,
            String genreName,
            LocalDate showDate
    ) {
        return new MovieSearchApiRequest(keyword, genreName, showDate);
    }
}
