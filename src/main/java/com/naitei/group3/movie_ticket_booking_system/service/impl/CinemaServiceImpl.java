package com.naitei.group3.movie_ticket_booking_system.service.impl;

import com.naitei.group3.movie_ticket_booking_system.converter.DtoConverter;
import com.naitei.group3.movie_ticket_booking_system.dto.response.CinemaDTO;
import com.naitei.group3.movie_ticket_booking_system.enums.ShowtimeStatus;
import com.naitei.group3.movie_ticket_booking_system.exception.ResourceNotFoundException;
import com.naitei.group3.movie_ticket_booking_system.repository.CinemaRepository;
import com.naitei.group3.movie_ticket_booking_system.repository.MovieRepository;
import com.naitei.group3.movie_ticket_booking_system.service.CinemaService;
import com.naitei.group3.movie_ticket_booking_system.service.MovieService;
import com.naitei.group3.movie_ticket_booking_system.utils.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CinemaServiceImpl implements CinemaService {

    private final CinemaRepository cinemaRepository;
    private final MessageUtil messageUtil;
    private final MovieService movieService;

    @Override
    public Page<CinemaDTO> searchCinema(String keyword, String city, Pageable pageable) {
        return cinemaRepository.searchCinema(keyword, city, pageable)
                .map(DtoConverter::convertCinemaToDTO);
    }

    @Override
    public List<String> getAllCities() {
        return cinemaRepository.findDistinctCities();
    }

    @Override
    public CinemaDTO getCinemaById(Long id) {
        return cinemaRepository.findById(id)
                .map(DtoConverter::convertCinemaToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("error.cinema.notfound: " + id));
    }

    @Override
    public Page<CinemaDTO> getCinemasByMovie(Long movieId, Pageable pageable) {
        boolean exists = movieService.existsById(movieId);
        if (!exists) {
            throw new ResourceNotFoundException(messageUtil.getMessage("error.movie.notfound"));
        }
        return cinemaRepository.findDistinctByHalls_Showtimes_MovieIdAndHalls_Showtimes_Status(movieId, ShowtimeStatus.AVAILABLE, pageable)
                .map(DtoConverter::convertCinemaToDTO);
    }
}
