package com.naitei.group3.movie_ticket_booking_system.service.impl;

import com.naitei.group3.movie_ticket_booking_system.converter.DtoConverter;
import com.naitei.group3.movie_ticket_booking_system.dto.request.HallRequestDTO;
import com.naitei.group3.movie_ticket_booking_system.entity.Cinema;
import com.naitei.group3.movie_ticket_booking_system.entity.Hall;
import com.naitei.group3.movie_ticket_booking_system.exception.HallAlreadyExistsException;
import com.naitei.group3.movie_ticket_booking_system.exception.ResourceNotFoundException;
import com.naitei.group3.movie_ticket_booking_system.repository.CinemaRepository;
import com.naitei.group3.movie_ticket_booking_system.repository.HallRepository;
import com.naitei.group3.movie_ticket_booking_system.service.HallService;
import lombok.RequiredArgsConstructor;
import com.naitei.group3.movie_ticket_booking_system.exception.BadRequestException;
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
    public void addHallWithSeats(HallRequestDTO request) {
        Cinema cinema = cinemaRepository.findById(request.getCinemaId())
            .orElseThrow(() -> new ResourceNotFoundException(
            messageSource.getMessage("cinema.notfound", null, LocaleContextHolder.getLocale())
        ));

        if (hallRepository.existsByCinemaAndName(cinema, request.getHallName())) {
            throw new HallAlreadyExistsException(request.getHallName());
        }

        Hall hall = DtoConverter.convertToHall(request, cinema);

        hallRepository.save(hall);
    }

    @Override
    public Hall getHallById(Long hallId) {
        return hallRepository.findById(hallId)
                .orElseThrow(() -> new ResourceNotFoundException(
                                messageSource.getMessage(
                                        "hall.notfound",
                                        new Object[]{hallId},
                                        LocaleContextHolder.getLocale()
                                )
                        )
                );
    }

    @Override
    @Transactional
    public Hall updateHallName(Long hallId, String newName) {
        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("hall.notfound", new Object[]{hallId}, LocaleContextHolder.getLocale())
                ));

        // Phải điền "Hall + số phòng"
        if (!newName.matches("^Hall\\s*\\d+$")) {
            throw new BadRequestException(
                    messageSource.getMessage("hall.invalidName", new Object[]{newName}, LocaleContextHolder.getLocale())
            );
        }

        Cinema cinema = hall.getCinema();

        // Kiểm tra trùng tên phòng khác trong cùng 1 cinema
        if (hallRepository.existsByCinemaAndName(cinema, newName) && !hall.getName().equals(newName)) {
            throw new BadRequestException(
                    messageSource.getMessage("hall.duplicateName",
                            new Object[]{newName, cinema.getId()},
                            LocaleContextHolder.getLocale())
            );
        }

        hall.setName(newName);
        return hallRepository.save(hall);
    }
}
