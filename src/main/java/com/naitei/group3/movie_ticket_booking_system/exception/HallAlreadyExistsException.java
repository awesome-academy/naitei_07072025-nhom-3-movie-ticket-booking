package com.naitei.group3.movie_ticket_booking_system.exception;

public class HallAlreadyExistsException extends RuntimeException {
  private final String hallName;

  public HallAlreadyExistsException(String hallName) {
    this.hallName = hallName;
  }

  public String getHallName() {
    return hallName;
  }
}
