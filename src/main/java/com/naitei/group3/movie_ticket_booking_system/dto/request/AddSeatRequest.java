package com.naitei.group3.movie_ticket_booking_system.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddSeatRequest {

    private String row;
    private int quantity;
    private Long seatTypeId;
    private boolean newRow;
}
