package com.naitei.group3.movie_ticket_booking_system.service.impl;

import com.naitei.group3.movie_ticket_booking_system.converter.DtoConverter;
import com.naitei.group3.movie_ticket_booking_system.dto.request.BookingRequestDTO;
import com.naitei.group3.movie_ticket_booking_system.dto.response.BaseApiResponse;
import com.naitei.group3.movie_ticket_booking_system.dto.response.BookingDTO;
import com.naitei.group3.movie_ticket_booking_system.dto.response.BookingHistoryDTO;
import com.naitei.group3.movie_ticket_booking_system.entity.*;
import com.naitei.group3.movie_ticket_booking_system.enums.BookingStatus;
import com.naitei.group3.movie_ticket_booking_system.exception.*;
import com.naitei.group3.movie_ticket_booking_system.repository.*;
import com.naitei.group3.movie_ticket_booking_system.service.BookingService;
import com.naitei.group3.movie_ticket_booking_system.service.UserPointService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ShowtimeRepository showtimeRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;
    private final UserPointService userPointService;

    @Transactional
    @Override
    public BaseApiResponse<BookingDTO> createBooking(Long userId, BookingRequestDTO request) {
        Showtime showtime = showtimeRepository.findById(request.showtimeId())
                .orElseThrow(() -> new ResourceNotFoundException("Showtime not found"));

        List<Seat> seats = seatRepository.findAllById(request.seatIds());

        boolean isTaken = bookingSeatRepository.existsBookedOrHeldSeats(
                request.showtimeId(),
                request.seatIds(),
                List.of(BookingStatus.PENDING.getValue(), BookingStatus.PAID.getValue())
        );
        if (isTaken) {
            throw new SeatAlreadyBookedException("One or more seats are already booked or held.");
        }

        BigDecimal totalPrice = seats.stream()
                .map(seat -> showtime.getPrice().multiply(seat.getSeatType().getPriceMultiplier()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal pointsToUseBD = BigDecimal.valueOf(request.pointsToUse());
        totalPrice = totalPrice.subtract(pointsToUseBD);
        if (totalPrice.compareTo(BigDecimal.ZERO) < 0) totalPrice = BigDecimal.ZERO;

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (request.pointsToUse() > user.getPoint()) {
            throw new NotEnoughPointsException("Not enough points");
        }
        userPointService.deductPoints(user, request.pointsToUse());

        Booking booking = Booking.builder()
                .user(user)
                .showtime(showtime)
                .totalPrice(totalPrice)
                .status(BookingStatus.PENDING.getValue())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .build();
        Booking savedBooking = bookingRepository.saveAndFlush(booking);

        Set<BookingSeat> bookingSeats = new HashSet<>();
        for (Seat seat : seats) {
            BookingSeatId id = new BookingSeatId(savedBooking.getId(), seat.getId());
            BookingSeat bookingSeat = new BookingSeat();
            bookingSeat.setId(id);
            bookingSeat.setBooking(savedBooking);
            bookingSeat.setSeat(seat);
            bookingSeats.add(bookingSeat);
        }
        savedBooking.setBookingSeats(bookingSeats);
        bookingRepository.save(savedBooking);

        BookingDTO dto = new BookingDTO(
                savedBooking.getId(),
                savedBooking.getUser().getId(),
                savedBooking.getShowtime().getId(),
                savedBooking.getTotalPrice(),
                savedBooking.getStatus()
        );

        return new BaseApiResponse<>(HttpStatus.OK.value(), "Booking created successfully", dto);
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    @Override
    public void releaseExpiredBookings() {
        List<Booking> expiredBookings = bookingRepository.findByStatusAndExpiresAtBefore(
                BookingStatus.PENDING.getValue(), LocalDateTime.now()
        );
        for (Booking booking : expiredBookings) {
            booking.setStatus(BookingStatus.CANCELLED.getValue());
        }
        bookingRepository.saveAll(expiredBookings);
    }

    @Override
    public BaseApiResponse<List<BookingHistoryDTO>> getBookingHistory(Long userId) {
        List<Booking> bookings = bookingRepository.findByUser_IdOrderByShowtime_StartTimeDesc(userId);

        List<BookingHistoryDTO> bookingHistory = bookings.stream()
                .map(DtoConverter::convertBookingToHistoryDTO)
                .collect(Collectors.toList());

        return new BaseApiResponse<>(
                HttpStatus.OK.value(),
                "Booking history fetched successfully",
                bookingHistory
        );
    }

    @Override
    public BaseApiResponse<BookingDTO> getBookingById(Long id) {
        return bookingRepository.findById(id)
                .map(booking -> new BaseApiResponse<>(
                        HttpStatus.OK.value(),
                        "Success",
                        new BookingDTO(
                                booking.getId(),
                                booking.getUser().getId(),
                                booking.getShowtime().getId(),
                                booking.getTotalPrice(),
                                booking.getStatus()
                        )
                ))
                .orElseGet(() -> new BaseApiResponse<>(HttpStatus.NOT_FOUND.value(), "Booking not found"));
    }

   
}
