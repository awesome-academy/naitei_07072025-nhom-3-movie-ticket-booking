package com.naitei.group3.movie_ticket_booking_system.service;

import java.util.List;

import com.naitei.group3.movie_ticket_booking_system.dto.request.RatingRequest;
import com.naitei.group3.movie_ticket_booking_system.dto.response.RatingDTO;
import com.naitei.group3.movie_ticket_booking_system.dto.response.RatingResponse;
import com.naitei.group3.movie_ticket_booking_system.enums.RatingStatus;

public interface RatingService {

    RatingResponse createOrUpdateRating(RatingRequest request);
    List<RatingResponse> getRatingsByMovie(Long movieId);
    List<RatingResponse> getRatingsByUser(Long userId);
    List<RatingDTO> getRatingsByStatus(RatingStatus status);
    RatingDTO updateStatus(Long userId, Long movieId, RatingStatus newStatus);
}
