package com.naitei.group3.movie_ticket_booking_system.repository;

import com.naitei.group3.movie_ticket_booking_system.entity.Showtime;
import com.naitei.group3.movie_ticket_booking_system.enums.ShowtimeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

        @Query("""
                        SELECT s FROM Showtime s
                        JOIN s.movie m
                        JOIN s.hall h
                        JOIN h.cinema c
                        WHERE (:keyword IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
                          AND (:cinemaName IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :cinemaName, '%')))
                          AND (:status IS NULL OR s.status = :status)
                          AND (:showDate IS NULL OR FUNCTION('DATE', s.startTime) = :showDate)
                        ORDER BY s.startTime desc
                        """)
        Page<Showtime> filterShowtimes(
                        @Param("keyword") String keyword,
                        @Param("cinemaName") String cinemaName,
                        @Param("status") ShowtimeStatus status,
                        @Param("showDate") LocalDate showDate,
                        Pageable pageable);

        @Query("""
                    SELECT s FROM Showtime s
                    JOIN s.movie m
                    JOIN s.hall h
                    JOIN h.cinema c
                    WHERE (:cinemaId IS NULL OR c.id = :cinemaId)
                      AND (:movieId IS NULL OR m.id = :movieId)
                      AND s.status = com.naitei.group3.movie_ticket_booking_system.enums.ShowtimeStatus.AVAILABLE
                    ORDER BY s.startTime
                """)
        Page<Showtime> findShowtimesByCinemaAndMovie(@Param("movieId") Long movieId,
                                                     @Param("cinemaId") Long  cinemaId,
                                                     Pageable pageable);
}
