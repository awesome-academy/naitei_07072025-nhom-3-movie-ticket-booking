package com.naitei.group3.movie_ticket_booking_system.controller.api;

import com.naitei.group3.movie_ticket_booking_system.dto.request.BookingRequestDTO;
import com.naitei.group3.movie_ticket_booking_system.dto.response.BaseApiResponse;
import com.naitei.group3.movie_ticket_booking_system.dto.response.BookingDTO;
import com.naitei.group3.movie_ticket_booking_system.dto.response.BookingHistoryDTO;
import com.naitei.group3.movie_ticket_booking_system.security.CustomUserPrincipal;
import com.naitei.group3.movie_ticket_booking_system.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BaseApiResponse<BookingDTO>> createBooking(
            @RequestBody BookingRequestDTO request,
            Authentication authentication) {

        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        Long userId = principal.getId();

        BaseApiResponse<BookingDTO> response = bookingService.createBooking(userId, request);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseApiResponse<BookingDTO>> getBookingById(@PathVariable Long id) {
        BaseApiResponse<BookingDTO> response = bookingService.getBookingById(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/my-history")
    public ResponseEntity<BaseApiResponse<List<BookingHistoryDTO>>> getMyBookingHistory(Authentication authentication) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        Long userId = principal.getId();

        BaseApiResponse<List<BookingHistoryDTO>> response = bookingService.getBookingHistory(userId);
        return ResponseEntity.status(response.getCode()).body(response);
    }
    
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<BaseApiResponse<Void>> cancelBooking(
            @PathVariable Long id,
            Authentication authentication) {

        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        Long userId = principal.getId();

        BaseApiResponse<Void> response = bookingService.cancelBooking(id, userId);
        return ResponseEntity.status(response.getCode()).body(response);
    }
    
}
