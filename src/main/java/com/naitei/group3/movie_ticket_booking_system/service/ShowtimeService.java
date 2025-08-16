package com.naitei.group3.movie_ticket_booking_system.service;

import com.naitei.group3.movie_ticket_booking_system.dto.response.ShowtimeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ShowtimeService {
    Long getBookedSeats(Long showId);
    Page<ShowtimeDTO> filterShowtime(String keyword, String cinemaName, Integer status, LocalDate showDate, Pageable pageable);

}
