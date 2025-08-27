package com.naitei.group3.movie_ticket_booking_system.exception;

public class CannotRateMovieException extends RuntimeException {

    public CannotRateMovieException() {
        super();
    }

    public CannotRateMovieException(String message) {
        super(message);
    }
}
