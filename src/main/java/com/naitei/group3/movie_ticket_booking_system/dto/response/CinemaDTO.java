package com.naitei.group3.movie_ticket_booking_system.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CinemaDTO(
        Long id,

        String name,
        String address,
        String city,
        String mapUrl,
        List<HallDTO> halls
) {
}
