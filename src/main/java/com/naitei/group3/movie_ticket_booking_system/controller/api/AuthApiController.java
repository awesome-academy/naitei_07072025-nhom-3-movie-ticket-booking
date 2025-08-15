package com.naitei.group3.movie_ticket_booking_system.controller.api;

import com.naitei.group3.movie_ticket_booking_system.dto.request.RegisterRequestDTO;
import com.naitei.group3.movie_ticket_booking_system.enums.RoleType;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.naitei.group3.movie_ticket_booking_system.entity.User;
import com.naitei.group3.movie_ticket_booking_system.service.impl.UserService;

@RestController
public class AuthApiController {
  private final UserService userService;

  public AuthApiController(UserService userService) {
    this.userService = userService;
  }


  @PostMapping("/registerApi")
  public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO request) {
    try {
      User savedUser = userService.registerUser(request, RoleType.USER);
      return ResponseEntity.ok(savedUser);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error: " + e.getMessage());
    }
  }
}
