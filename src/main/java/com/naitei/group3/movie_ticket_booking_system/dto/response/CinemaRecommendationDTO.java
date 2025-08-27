package com.naitei.group3.movie_ticket_booking_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CinemaRecommendationDTO {

    private Long cinemaId;
    private String cinemaName;
    private String address;
    private String city;
    private String mapUrl;
    private double distanceKm;
    private List<MovieDTO> movies;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MovieDTO {
        private Long movieId;
        private String title;
        private double averageRating;
    }
}
