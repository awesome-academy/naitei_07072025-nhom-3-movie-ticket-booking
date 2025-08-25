package com.naitei.group3.movie_ticket_booking_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class RevenueDTO {
        private String label;     // day or month or year
        private BigDecimal revenue;
}
