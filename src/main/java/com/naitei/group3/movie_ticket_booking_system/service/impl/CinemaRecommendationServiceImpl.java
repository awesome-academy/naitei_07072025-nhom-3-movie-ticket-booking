package com.naitei.group3.movie_ticket_booking_system.service.impl;

import com.naitei.group3.movie_ticket_booking_system.dto.response.CinemaRecommendationDTO;
import com.naitei.group3.movie_ticket_booking_system.entity.Cinema;
import com.naitei.group3.movie_ticket_booking_system.entity.Showtime;
import com.naitei.group3.movie_ticket_booking_system.exception.InvalidMapUrlException;
import com.naitei.group3.movie_ticket_booking_system.exception.ResourceNotFoundException;
import com.naitei.group3.movie_ticket_booking_system.repository.CinemaRepository;
import com.naitei.group3.movie_ticket_booking_system.repository.RatingRepository;
import com.naitei.group3.movie_ticket_booking_system.repository.ShowtimeRepository;
import com.naitei.group3.movie_ticket_booking_system.service.CinemaRecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CinemaRecommendationServiceImpl implements CinemaRecommendationService {

    @Autowired
    private CinemaRepository cinemaRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Override
    public List<CinemaRecommendationDTO> findNearbyCinemasWithMovies(double userLat, double userLon, double radiusKm) {
        List<Cinema> allCinemas = cinemaRepository.findAll();

        if (allCinemas == null || allCinemas.isEmpty()) {
            throw new ResourceNotFoundException("No cinemas found in the database.");
        }

        LocalDateTime now = LocalDateTime.now();

        return allCinemas.stream()
                // Lọc các rạp trong bán kính radiusKm
                .filter(cinema -> {
                    double distance = calculateDistance(userLat, userLon,
                            parseLatitude(cinema.getMapUrl()),
                            parseLongitude(cinema.getMapUrl()));
                    return distance <= radiusKm;
                })
                // Map sang DTO
                .map(cinema -> {
                    List<CinemaRecommendationDTO.MovieDTO> movies = showtimeRepository
                            .findByHall_CinemaAndStartTimeAfter(cinema, now)
                            .stream()
                            .map(Showtime::getMovie)
                            .distinct()
                            .map(movie -> CinemaRecommendationDTO.MovieDTO.builder()
                                    .movieId(movie.getId())
                                    .title(movie.getName())
                                    .averageRating(
                                            ratingRepository.findAverageRatingByMovieId(movie.getId()).orElse(0.0))
                                    .build())
                            .sorted(Comparator.comparingDouble(CinemaRecommendationDTO.MovieDTO::getAverageRating)
                                    .reversed())
                            .collect(Collectors.toList());

                    return CinemaRecommendationDTO.builder()
                            .cinemaId(cinema.getId())
                            .cinemaName(cinema.getName())
                            .address(cinema.getAddress())
                            .city(cinema.getCity())
                            .mapUrl(cinema.getMapUrl())
                            .distanceKm(calculateDistance(userLat, userLon,
                                    parseLatitude(cinema.getMapUrl()),
                                    parseLongitude(cinema.getMapUrl())))
                            .movies(movies)
                            .build();
                })
                // Chỉ giữ các rạp có ít nhất 1 phim
                .filter(cinemaDTO -> !cinemaDTO.getMovies().isEmpty())
                // Sắp xếp theo khoảng cách gần nhất
                .sorted(Comparator.comparingDouble(CinemaRecommendationDTO::getDistanceKm))
                .collect(Collectors.toList());
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS_KM = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    private double parseLatitude(String mapUrl) {
        try {
            String[] coords = mapUrl.split("q=")[1].split(",");
            return Double.parseDouble(coords[0]);
        } catch (Exception e) {
            throw new InvalidMapUrlException("Invalid map URL: " + mapUrl);
        }
    }

    private double parseLongitude(String mapUrl) {
        try {
            String[] coords = mapUrl.split("q=")[1].split(",");
            return Double.parseDouble(coords[1]);
        } catch (Exception e) {
            throw new InvalidMapUrlException("Invalid map URL: " + mapUrl);
        }
    }
}
