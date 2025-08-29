package com.naitei.group3.movie_ticket_booking_system.repository;

import com.naitei.group3.movie_ticket_booking_system.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByHallId(Long id);

    @Query("""
            SELECT s FROM Seat s
            WHERE s.hall.id = :hallId
            """)
    List<Seat> findAllByHallId(Long hallId);

    boolean existsByHallIdAndSeatRowAndSeatColumn(Long hallId, String seatRow, String seatColumn);
    @Query("SELECT s.seatColumn FROM Seat s WHERE s.hall.id = :hallId AND s.seatRow = :row")
    List<String> findColumnsByHallIdAndRow(Long hallId, String row);

    @Query("""
        SELECT s
        FROM Seat s
        JOIN FETCH s.seatType st
        WHERE s.hall.id = (
            SELECT sh.hall.id FROM Showtime sh WHERE sh.id = :showtimeId
        )
        ORDER BY s.seatRow ASC, s.seatColumn ASC
    """)
    List<Seat> findSeatsByShowtimeId(@Param("showtimeId") Long showtimeId);
}
