package com.naitei.group3.movie_ticket_booking_system.service.impl;

import com.naitei.group3.movie_ticket_booking_system.dto.request.RegisterRequestDTO;
import com.naitei.group3.movie_ticket_booking_system.enums.RoleType;
import com.naitei.group3.movie_ticket_booking_system.exception.RoleNotFoundException;
import com.naitei.group3.movie_ticket_booking_system.exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.naitei.group3.movie_ticket_booking_system.entity.Role;
import com.naitei.group3.movie_ticket_booking_system.entity.User;
import com.naitei.group3.movie_ticket_booking_system.repository.RoleRepository;
import com.naitei.group3.movie_ticket_booking_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  @Value("${app.role-id.admin}")
  private Long adminRoleId;

  @Value("${app.role-id.user}")
  private Long userRoleId;

  @Autowired
  private MessageSource messageSource;


  public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  @Transactional
  public User registerUser(RegisterRequestDTO dto, RoleType roleType) {
    if (userRepository.existsByEmail(dto.getEmail())) {
      throw new UserAlreadyExistsException(
          messageSource.getMessage("error.user.exists", null, LocaleContextHolder.getLocale())
      );
    }

    User user = new User();
    user.setName(dto.getName());
    user.setEmail(dto.getEmail());
    user.setPassword(passwordEncoder.encode(dto.getPassword()));
    user.setAddress(dto.getAddress());
    user.setDateOfBirth(dto.getDateOfBirth());
    user.setGender(dto.getGender());
    user.setPhone(dto.getPhone());

    Long roleId = switch (roleType) {
      case ADMIN -> adminRoleId;
      case USER -> userRoleId;
    };

    Role role = roleRepository.findById(roleId)
        .orElseThrow(() -> new RoleNotFoundException(
            messageSource.getMessage("error.role.notfound", null, LocaleContextHolder.getLocale())
            ));
    user.setRole(role);

    user.setIsVerified(false);
    user.setPoint(0);

    return userRepository.save(user);
  }

}
