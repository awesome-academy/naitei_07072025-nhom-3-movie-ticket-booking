package com.naitei.group3.movie_ticket_booking_system.converter;

import com.naitei.group3.movie_ticket_booking_system.dto.request.HallRequestDTO;
import com.naitei.group3.movie_ticket_booking_system.dto.response.*;
import com.naitei.group3.movie_ticket_booking_system.dto.response.BookingHistoryDTO;
import com.naitei.group3.movie_ticket_booking_system.dto.response.CinemaDTO;
import com.naitei.group3.movie_ticket_booking_system.dto.response.MovieDTO;
import com.naitei.group3.movie_ticket_booking_system.dto.response.SeatDTO;
import com.naitei.group3.movie_ticket_booking_system.dto.response.ShowtimeDTO;
import com.naitei.group3.movie_ticket_booking_system.entity.Booking;
import com.naitei.group3.movie_ticket_booking_system.entity.Cinema;
import com.naitei.group3.movie_ticket_booking_system.entity.Hall;
import com.naitei.group3.movie_ticket_booking_system.entity.Movie;
import com.naitei.group3.movie_ticket_booking_system.entity.Seat;
import com.naitei.group3.movie_ticket_booking_system.entity.Showtime;
import com.naitei.group3.movie_ticket_booking_system.enums.PaymentStatus;

import java.util.stream.Collectors;

public class DtoConverter {

        public static MovieDTO convertMovieToDTO(Movie movie) {
                if (movie == null)
                        return null;

                return MovieDTO.builder()
                                .id(movie.getId())
                                .name(movie.getName())
                                .description(movie.getDescription())
                                .duration(movie.getDuration())
                                .poster(movie.getPoster())
                                .releaseDate(movie.getReleaseDate())
                                .isActive(movie.getIsActive())
                                .genres(movie.getGenres().stream()
                                                .map(g -> g.getName())
                                                .collect(Collectors.toSet()))
                                .build();
        }

        public static CinemaDTO convertCinemaToDTO(Cinema cinema) {
                if (cinema == null)
                        return null;

                return CinemaDTO.builder()
                                .id(cinema.getId())
                                .name(cinema.getName())
                                .address(cinema.getAddress())
                                .city(cinema.getCity())
                                .mapUrl(cinema.getMapUrl())
                                .build();
        }

        public static ShowtimeDTO convertShowtimeToDTO(Showtime s, Long paidSeats) {
                if (s == null)
                        return null;
                return ShowtimeDTO.builder()
                                .id(s.getId())
                                .date(s.getStartTime().toLocalDate())
                                .startTime(s.getStartTime().toLocalTime())
                                .endTime(s.getEndTime().toLocalTime())
                                .price(s.getPrice())
                                .paidSeats(paidSeats)
                                .status(s.getStatus())
                                .movie(s.getMovie())
                                .hall(s.getHall())
                                .build();
        }

    public static SimpleShowtimeDTO convertShowtimeToDTO(Showtime s) {
        if (s == null) return null;
        return SimpleShowtimeDTO.builder()
                .id(s.getId())
                .date(s.getStartTime().toLocalDate())
                .startTime(s.getStartTime().toLocalTime())
                .endTime(s.getEndTime().toLocalTime())
                .price(s.getPrice())
                .status(s.getStatus())
                .movieName(s.getMovie().getName())
                .hallId(s.getHall().getId())
                .hallName(s.getHall().getName())
                .build();
    }

    public static SeatDTO convertSeatToDTO(Seat seat) {
        if (seat == null) return null;


                return SeatDTO.builder()
                                .id(seat.getId())
                                .seatRow(seat.getSeatRow())
                                .seatColumn(seat.getSeatColumn())
                                .seatTypeName(seat.getSeatType().getName())
                                .priceMultiplier(seat.getSeatType().getPriceMultiplier())
                                .build();
        }

        public static BookingHistoryDTO convertBookingToHistoryDTO(Booking booking) {
                if (booking == null)
                        return null;

                return BookingHistoryDTO.builder()
                                .id(booking.getId())
                                .movieTitle(booking.getShowtime() != null && booking.getShowtime().getMovie() != null
                                                ? booking.getShowtime().getMovie().getName()
                                                : null)
                                .showtime(booking.getShowtime() != null ? booking.getShowtime().getStartTime() : null)
                                .totalPrice(booking.getTotalPrice())
                                .status(booking.getStatus())
                                .paymentStatus(
                                                booking.getPaymentTransactions().stream()
                                                                .findFirst()
                                                                .map(pt -> pt.getStatus() == PaymentStatus.SUCCESS
                                                                                ? "PAID"
                                                                                : "UNPAID")
                                                                .orElse("UNPAID"))
                                .build();
        }

       
    

    public static Hall convertToHall(HallRequestDTO request, Cinema cinema) {
        return Hall.builder()
            .name(request.getHallName())
            .totalSeats(request.getTotalSeats())
            .cinema(cinema)
            .build();
    }
}
