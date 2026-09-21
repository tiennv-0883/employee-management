package employee_management.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import employee_management.dto.RegisterRequest;
import employee_management.entity.Role;
import employee_management.entity.User;
import employee_management.exception.UsernameAlreadyExistsException;
import employee_management.repository.UserRepository;

@Service
public class AuthService {

  private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public AuthService(
      UserRepository userRepository,
      PasswordEncoder passwordEncoder) {

    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public User register(RegisterRequest request) {

    if (userRepository.existsByUsername(request.username())) {
      logger.warn("Register failed, username already exists: {}", request.username());
      throw new UsernameAlreadyExistsException(request.username());
    }

    Role role = request.role() != null ? request.role() : Role.USER;

    User user = new User(
        request.username(),
        passwordEncoder.encode(request.password()),
        role);

    User saved = userRepository.save(user);

    logger.info("User registered: username={}, role={}", saved.getUsername(), saved.getRole());

    return saved;
  }
}
