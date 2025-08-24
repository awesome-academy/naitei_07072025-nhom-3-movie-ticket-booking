package com.naitei.group3.movie_ticket_booking_system.controller.api;

import com.naitei.group3.movie_ticket_booking_system.dto.request.MovieSearchApiRequest;
import com.naitei.group3.movie_ticket_booking_system.dto.response.BaseApiResponse;
import com.naitei.group3.movie_ticket_booking_system.dto.response.MovieDTO;
import com.naitei.group3.movie_ticket_booking_system.entity.Movie;
import com.naitei.group3.movie_ticket_booking_system.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController("apiMovieController")
@RequestMapping("/api/v1/movies")
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/now-showing")
    public ResponseEntity<BaseApiResponse<Page<Movie>>> getNowShowingMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Movie> movies = movieService.getNowShowingMovies(pageable);
        return ResponseEntity.ok(
                new BaseApiResponse<>(HttpStatus.OK.value(), movies)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<BaseApiResponse<Page<MovieDTO>>> searchMovies(
            @RequestBody MovieSearchApiRequest req,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(
                new BaseApiResponse<>(HttpStatus.OK.value(), movieService.searchMovies(req, pageable)));
    }
}
