package com.naitei.group3.movie_ticket_booking_system.service;

import com.naitei.group3.movie_ticket_booking_system.dto.request.ShowtimeFilterReq;
import com.naitei.group3.movie_ticket_booking_system.dto.response.ShowtimeDTO;
import com.naitei.group3.movie_ticket_booking_system.dto.response.SimpleShowtimeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface ShowtimeService {
    Long getNumOfBookedSeats(Long showtimeId);
    Page<ShowtimeDTO> filterShowtime(ShowtimeFilterReq filter, Pageable pageable);
    ShowtimeDTO getShowtimeById(Long id);
    Page<SimpleShowtimeDTO> getShowtimes(Long cinemaId, Long movieId, Pageable pageable);
}
