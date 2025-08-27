package com.naitei.group3.movie_ticket_booking_system.service;

import java.util.List;

import com.naitei.group3.movie_ticket_booking_system.dto.request.BookingRequestDTO;
import com.naitei.group3.movie_ticket_booking_system.dto.response.BaseApiResponse;
import com.naitei.group3.movie_ticket_booking_system.dto.response.BookingDTO;
import com.naitei.group3.movie_ticket_booking_system.dto.response.BookingHistoryDTO;
import com.naitei.group3.movie_ticket_booking_system.entity.Booking;

public interface BookingService {
    BaseApiResponse<BookingDTO> createBooking(Long userId, BookingRequestDTO request);

    void releaseExpiredBookings();

    BaseApiResponse<BookingDTO> getBookingById(Long id);
    
    BaseApiResponse<List<BookingHistoryDTO>> getBookingHistory(Long userId);



}
