package com.naitei.group3.movie_ticket_booking_system.converter;

import com.naitei.group3.movie_ticket_booking_system.dto.response.CinemaDTO;
import com.naitei.group3.movie_ticket_booking_system.entity.Cinema;

public class RequestMapper {
    public static void updateCinemaEntity(Cinema cinema, CinemaDTO request) {
        cinema.setName(request.name());
        cinema.setAddress(request.address());
        cinema.setCity(request.city());
        cinema.setMapUrl(request.mapUrl());
    }
}
