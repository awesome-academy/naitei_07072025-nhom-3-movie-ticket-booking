package com.naitei.group3.movie_ticket_booking_system.repository;

import com.naitei.group3.movie_ticket_booking_system.entity.Cinema;
import com.naitei.group3.movie_ticket_booking_system.enums.ShowtimeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CinemaRepository extends JpaRepository<Cinema, Long> {

    @Query("""
                SELECT c FROM Cinema c
                WHERE (:keyword IS NULL OR :keyword = ''
                       OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                       OR LOWER(c.address) LIKE LOWER(CONCAT('%', :keyword, '%')))
                  AND (:city IS NULL OR :city = '' OR LOWER(c.city) = LOWER(:city))
            """)
    Page<Cinema> searchCinema(@Param("keyword") String keyword,
            @Param("city") String city,
            Pageable pageable);

    @Query("SELECT DISTINCT c.city FROM Cinema c WHERE c.city IS NOT NULL AND c.city <> '' ORDER BY c.city ASC")
    List<String> findDistinctCities();

    Page<Cinema> findDistinctByHalls_Showtimes_MovieIdAndHalls_Showtimes_Status(
            Long movieId,
            ShowtimeStatus status,
            Pageable pageable
    );
}
