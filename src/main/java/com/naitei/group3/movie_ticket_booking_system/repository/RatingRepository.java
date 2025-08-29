package com.naitei.group3.movie_ticket_booking_system.repository;

import com.naitei.group3.movie_ticket_booking_system.entity.Rating;
import com.naitei.group3.movie_ticket_booking_system.entity.RatingId;
import com.naitei.group3.movie_ticket_booking_system.enums.RatingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, RatingId> {

    List<Rating> findByMovie_Id(Long movieId);

    List<Rating> findByUser_Id(Long userId);
    List<Rating> findAllByStatus(RatingStatus status);

    // Method lấy điểm trung bình của movie
    @Query("SELECT AVG(r.stars) FROM Rating r WHERE r.movie.id = :movieId")
    Optional<Double> findAverageRatingByMovieId(@Param("movieId") Long movieId);

}
