package com.naitei.group3.movie_ticket_booking_system.controller.api;

import com.naitei.group3.movie_ticket_booking_system.dto.response.BaseApiResponse;
import com.naitei.group3.movie_ticket_booking_system.dto.response.ShowtimeDTO;
import com.naitei.group3.movie_ticket_booking_system.dto.response.SimpleShowtimeDTO;
import com.naitei.group3.movie_ticket_booking_system.service.ShowtimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("apiShowtimeController")
@RequestMapping("/api/v1/showtimes")
@RequiredArgsConstructor
public class ShowtimeController {

    private final ShowtimeService showtimeService;

    @GetMapping
    public ResponseEntity<BaseApiResponse<Page<SimpleShowtimeDTO>>> getShowtimes(
            @RequestParam(value = "movieId") Long movieId,
            @RequestParam(value = "cinemaId") Long cinemaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<SimpleShowtimeDTO> showtimes = showtimeService.getShowtimes(movieId, cinemaId, pageable);
        return ResponseEntity.ok(
                new BaseApiResponse<>(HttpStatus.OK.value(), showtimes)
        );
    }
}
