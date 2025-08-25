package com.naitei.group3.movie_ticket_booking_system.service.impl;

import com.naitei.group3.movie_ticket_booking_system.converter.DtoConverter;
import com.naitei.group3.movie_ticket_booking_system.converter.RatingDTOConverter;
import com.naitei.group3.movie_ticket_booking_system.dto.request.RatingRequest;
import com.naitei.group3.movie_ticket_booking_system.dto.response.RatingDTO;
import com.naitei.group3.movie_ticket_booking_system.dto.response.RatingResponse;
import com.naitei.group3.movie_ticket_booking_system.entity.Movie;
import com.naitei.group3.movie_ticket_booking_system.entity.Rating;
import com.naitei.group3.movie_ticket_booking_system.entity.RatingId;
import com.naitei.group3.movie_ticket_booking_system.entity.User;
import com.naitei.group3.movie_ticket_booking_system.enums.BookingStatus;
import com.naitei.group3.movie_ticket_booking_system.enums.RatingStatus;
import com.naitei.group3.movie_ticket_booking_system.exception.CannotRateMovieException;
import com.naitei.group3.movie_ticket_booking_system.exception.ResourceNotFoundException;
import com.naitei.group3.movie_ticket_booking_system.repository.BookingRepository;
import com.naitei.group3.movie_ticket_booking_system.repository.MovieRepository;
import com.naitei.group3.movie_ticket_booking_system.repository.RatingRepository;
import com.naitei.group3.movie_ticket_booking_system.repository.UserRepository;
import com.naitei.group3.movie_ticket_booking_system.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Override
    public RatingResponse createOrUpdateRating(RatingRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        // Kiểm tra user đã mua vé và suất chiếu đã kết thúc
        boolean hasWatched = bookingRepository
                .existsByUser_IdAndShowtime_Movie_IdAndStatusAndShowtime_EndTimeBefore(
                        user.getId(),
                        movie.getId(),
                        BookingStatus.PAID.getValue(),
                        LocalDateTime.now()
                );

        if (!hasWatched) {
            throw new CannotRateMovieException("You can only rate a movie after watching the showtime.");
        }

        RatingId ratingId = new RatingId(user.getId(), movie.getId());
        Rating rating = ratingRepository.findById(ratingId)
                .orElse(Rating.builder()
                        .id(ratingId)
                        .user(user)
                        .movie(movie)
                        .build());

        rating.setStars(request.getStars());
        rating.setComment(request.getComment());
        rating.setStatus(RatingStatus.PENDING);

        ratingRepository.save(rating);

        return RatingDTOConverter.toDTO(rating);
    }

    @Override
    public List<RatingResponse> getRatingsByMovie(Long movieId) {
        return ratingRepository.findByMovie_Id(movieId)
                .stream()
                .map(RatingDTOConverter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RatingResponse> getRatingsByUser(Long userId) {
        return ratingRepository.findByUser_Id(userId)
                .stream()
                .map(RatingDTOConverter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RatingDTO> getRatingsByStatus(RatingStatus status) {
        return ratingRepository.findAllByStatus(status).stream()
                .map(DtoConverter::covertRatingToDTO)
                .toList();
    }
}
