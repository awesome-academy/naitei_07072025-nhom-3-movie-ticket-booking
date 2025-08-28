package com.naitei.group3.movie_ticket_booking_system.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeatEditRequest {

    private String seatRow;
    private String seatColumn;
    private Long seatTypeId;
}
