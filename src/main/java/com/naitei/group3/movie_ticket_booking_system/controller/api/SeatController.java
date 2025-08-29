package com.naitei.group3.movie_ticket_booking_system.controller.api;

import com.naitei.group3.movie_ticket_booking_system.dto.response.BaseApiResponse;
import com.naitei.group3.movie_ticket_booking_system.dto.response.SeatDTO;
import com.naitei.group3.movie_ticket_booking_system.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("apiSeatController")
@RequestMapping("/api/v1/showtimes")
@RequiredArgsConstructor
public class SeatController {
    private final SeatService seatService;

    @GetMapping("/{showtimeId}/seats")
    public ResponseEntity<BaseApiResponse<List<SeatDTO>>> getSeats(
            @PathVariable("showtimeId") Long showtimeId
    ) {
        return ResponseEntity.ok(
            new BaseApiResponse<>(HttpStatus.OK.value(), seatService.getSeatsByShowtimeId(showtimeId))
        );
    }
}
