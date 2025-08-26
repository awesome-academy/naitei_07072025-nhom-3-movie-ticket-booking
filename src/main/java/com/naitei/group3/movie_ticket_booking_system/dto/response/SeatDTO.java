package com.naitei.group3.movie_ticket_booking_system.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SeatDTO(
        Long id,
        String seatRow,
        String seatColumn,
        String seatTypeName,
        BigDecimal priceMultiplier,
        boolean status
) {
}
