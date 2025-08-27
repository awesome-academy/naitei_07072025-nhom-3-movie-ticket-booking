package com.naitei.group3.movie_ticket_booking_system.service;

import com.naitei.group3.movie_ticket_booking_system.dto.request.SeatEditRequest;
import com.naitei.group3.movie_ticket_booking_system.dto.response.SeatDTO;
import com.naitei.group3.movie_ticket_booking_system.entity.Seat;

import java.util.List;
import java.util.Map;

public interface SeatService {

//    List<SeatDTO> getSeatsByHallId(Long id);
    Map<String, List<SeatDTO>> getSeatsByHallId(Long id);

    Seat getSeatById(Long seatId);
    Seat editSeat(Long seatId, SeatEditRequest request);

    void deleteSeat(Long seatId);
}
