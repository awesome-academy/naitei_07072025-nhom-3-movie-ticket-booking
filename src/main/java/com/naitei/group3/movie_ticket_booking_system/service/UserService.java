package com.naitei.group3.movie_ticket_booking_system.service;

import com.naitei.group3.movie_ticket_booking_system.dto.request.RegisterRequestDTO;
import com.naitei.group3.movie_ticket_booking_system.dto.request.UserUpdateRequest;
import com.naitei.group3.movie_ticket_booking_system.entity.User;
import com.naitei.group3.movie_ticket_booking_system.enums.RoleType;

public interface UserService {
  User registerUser(RegisterRequestDTO dto, RoleType roleType);
  void verifyEmail(String token);
  User getProfile(Long id);
  User updateProfile(Long userId, UserUpdateRequest req);
}
