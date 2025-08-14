package com.naitei.group3.movie_ticket_booking_system.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.naitei.group3.movie_ticket_booking_system.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {}
