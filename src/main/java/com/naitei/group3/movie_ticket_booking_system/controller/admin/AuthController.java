package com.naitei.group3.movie_ticket_booking_system.controller.admin;

import com.naitei.group3.movie_ticket_booking_system.dto.request.RegisterRequestDTO;
import com.naitei.group3.movie_ticket_booking_system.dto.response.UserResponseDTO;
import com.naitei.group3.movie_ticket_booking_system.enums.RoleType;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.naitei.group3.movie_ticket_booking_system.entity.User;
import com.naitei.group3.movie_ticket_booking_system.service.impl.UserService;
import org.springframework.web.bind.annotation.RequestMapping;

// Chỉ admin moi có giao diện đăng ký
@Controller
public class AuthController {
  private final UserService userService;

  public AuthController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/register")
  public String showRegister(Model model) {
    model.addAttribute("pageTitle", "Đăng ký");
    return "admin/register";
  }

  @PostMapping("/registerAdmin")
  public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO request) {
    try {
      User savedUser = userService.registerUser(request, RoleType.ADMIN);
      UserResponseDTO responseDTO = new UserResponseDTO(
          savedUser.getId(),
          savedUser.getName(),
          savedUser.getRole().getName()
      );

      return ResponseEntity.ok(responseDTO);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error: " + e.getMessage());
    }
  }

}
