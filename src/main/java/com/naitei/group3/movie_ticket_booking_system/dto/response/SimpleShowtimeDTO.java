package com.naitei.group3.movie_ticket_booking_system.dto.response;

import com.naitei.group3.movie_ticket_booking_system.entity.Hall;
import com.naitei.group3.movie_ticket_booking_system.entity.Movie;
import com.naitei.group3.movie_ticket_booking_system.enums.ShowtimeStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record SimpleShowtimeDTO(
        Long id,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        BigDecimal price,
        ShowtimeStatus status,
        String movieName,
        Long hallId,
        String hallName
) {}
