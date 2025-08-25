package com.naitei.group3.movie_ticket_booking_system.service.impl;

import com.naitei.group3.movie_ticket_booking_system.converter.DtoConverter;
import com.naitei.group3.movie_ticket_booking_system.dto.response.RatingDTO;
import com.naitei.group3.movie_ticket_booking_system.enums.RatingStatus;
import com.naitei.group3.movie_ticket_booking_system.repository.RatingRepository;
import com.naitei.group3.movie_ticket_booking_system.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    @Override
    public List<RatingDTO> getRatingsByStatus(RatingStatus status) {
        return ratingRepository.findAllByStatus(status).stream()
                .map(DtoConverter::covertRatingToDTO)
                .toList();
    }
}
