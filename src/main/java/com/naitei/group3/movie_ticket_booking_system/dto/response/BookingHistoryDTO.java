package com.naitei.group3.movie_ticket_booking_system.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingHistoryDTO {

    private Long id;
    private String movieTitle;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime showtime;

    private BigDecimal totalPrice;
    private Integer status;
    private String paymentStatus;

}
