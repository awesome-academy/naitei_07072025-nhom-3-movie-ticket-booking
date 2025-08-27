package com.naitei.group3.movie_ticket_booking_system.repository;

import com.naitei.group3.movie_ticket_booking_system.entity.Booking;
import com.naitei.group3.movie_ticket_booking_system.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByStatusAndExpiresAtBefore(int i, LocalDateTime time);

	List<Booking> findByUser_IdOrderByShowtime_StartTimeDesc(Long userId);
    boolean existsByUser_IdAndShowtime_Movie_IdAndStatusAndShowtime_EndTimeBefore(
            Long userId,
            Long movieId,
            Integer status,
            LocalDateTime now);
    }

