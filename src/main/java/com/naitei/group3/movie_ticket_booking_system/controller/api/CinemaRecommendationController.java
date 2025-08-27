package com.naitei.group3.movie_ticket_booking_system.controller.api;

import com.naitei.group3.movie_ticket_booking_system.dto.response.CinemaRecommendationDTO;
import com.naitei.group3.movie_ticket_booking_system.service.impl.CinemaRecommendationServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recommendations")
public class CinemaRecommendationController {

    @Autowired
    private CinemaRecommendationServiceImpl recommendationService;

    @GetMapping("/nearby-cinemas")
    public ResponseEntity<List<CinemaRecommendationDTO>> getNearbyCinemas(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "10") double radiusKm) {
        List<CinemaRecommendationDTO> recommendations = recommendationService
                .findNearbyCinemasWithMovies(latitude, longitude, radiusKm);
        return ResponseEntity.ok(recommendations);
    }
}
