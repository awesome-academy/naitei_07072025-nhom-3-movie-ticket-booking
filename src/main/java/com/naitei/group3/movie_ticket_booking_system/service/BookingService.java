package com.naitei.group3.movie_ticket_booking_system.service;

import java.util.List;

import com.naitei.group3.movie_ticket_booking_system.dto.request.BookingRequestDTO;
import com.naitei.group3.movie_ticket_booking_system.dto.response.BaseApiResponse;
import com.naitei.group3.movie_ticket_booking_system.dto.response.BookingDTO;
import com.naitei.group3.movie_ticket_booking_system.dto.response.BookingHistoryDTO;
import com.naitei.group3.movie_ticket_booking_system.entity.Booking;

public interface BookingService {
    BaseApiResponse<BookingDTO> createBooking(Long userId, BookingRequestDTO request);

    BaseApiResponse<BookingDTO> getBookingById(Long bookingId);

    
    BaseApiResponse<List<BookingHistoryDTO>> getBookingHistory(Long userId);



    BaseApiResponse<Void> cancelBooking(Long bookingId, Long userId);

    void releaseExpiredBookings();
}
