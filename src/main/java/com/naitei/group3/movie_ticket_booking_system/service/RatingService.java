package com.naitei.group3.movie_ticket_booking_system.service;

import com.naitei.group3.movie_ticket_booking_system.dto.response.RatingDTO;
import com.naitei.group3.movie_ticket_booking_system.enums.RatingStatus;

import java.util.List;

public interface RatingService {

    List<RatingDTO> getRatingsByStatus(RatingStatus status);
}
