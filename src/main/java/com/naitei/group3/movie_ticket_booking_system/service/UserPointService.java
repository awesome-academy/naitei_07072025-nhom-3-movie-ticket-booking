package com.naitei.group3.movie_ticket_booking_system.service;

import com.naitei.group3.movie_ticket_booking_system.entity.User;
import com.naitei.group3.movie_ticket_booking_system.exception.NotEnoughPointsException;

public interface UserPointService {

    // Trừ điểm
    void deductPoints(User user, int pointsToUse) throws NotEnoughPointsException;

    // Cộng điểm
    void addPoints(User user, int pointsToAdd);
}
