package employee_management.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import employee_management.dto.LoginRequest;
import employee_management.dto.LoginResponse;
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
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  public AuthService(
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      AuthenticationManager authenticationManager,
      JwtService jwtService) {

    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
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

  public LoginResponse login(LoginRequest request) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.username(),
            request.password()));

    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    String token = jwtService.generateToken(userDetails);

    String role = userDetails.getAuthorities()
        .stream()
        .map(authority -> authority.getAuthority())
        .filter(authority -> authority.startsWith("ROLE_"))
        .findFirst()
        .orElse("ROLE_USER");

    logger.info("User logged in: username={}", userDetails.getUsername());

    return new LoginResponse(
        token,
        "Bearer",
        jwtService.getExpirationMs(),
        userDetails.getUsername(),
        role);
  }
}
