package com.naitei.group3.movie_ticket_booking_system.dto.request;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HallRequestDTO {
  private Long cinemaId;
  private String hallName;
  private Integer totalSeats;
}
