package com.naitei.group3.movie_ticket_booking_system.service.impl;

import com.naitei.group3.movie_ticket_booking_system.dto.request.HallRequest;
import com.naitei.group3.movie_ticket_booking_system.entity.Cinema;
import com.naitei.group3.movie_ticket_booking_system.entity.Hall;
import com.naitei.group3.movie_ticket_booking_system.exception.HallAlreadyExistsException;
import com.naitei.group3.movie_ticket_booking_system.exception.ResourceNotFoundException;
import com.naitei.group3.movie_ticket_booking_system.exception.UserAlreadyExistsException;
import com.naitei.group3.movie_ticket_booking_system.repository.CinemaRepository;
import com.naitei.group3.movie_ticket_booking_system.repository.HallRepository;
import com.naitei.group3.movie_ticket_booking_system.repository.SeatRepository;
import com.naitei.group3.movie_ticket_booking_system.service.HallService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HallServiceImpl implements HallService {

    private final HallRepository hallRepository;
    private final CinemaRepository cinemaRepository;
    private final MessageSource messageSource;

    @Override
    public List<Hall> getHallsByCinemaId(Long id) {
        return hallRepository.findByCinemaIdOrderByNameAsc(id);
    }


    @Override
    @Transactional
    public void addHallWithSeats(HallRequest request) {
        Cinema cinema = cinemaRepository.findById(request.getCinemaId())
            .orElseThrow(() -> new ResourceNotFoundException(
            messageSource.getMessage("cinema.notfound", null, LocaleContextHolder.getLocale())
        ));

        if (hallRepository.existsByCinemaAndName(cinema, request.getHallName())) {
            throw new HallAlreadyExistsException(request.getHallName());
        }

        Hall hall = Hall.builder()
            .name(request.getHallName())
            .totalSeats(request.getTotalSeats())
            .cinema(cinema)
            .build();

        hallRepository.save(hall);
    }


}
