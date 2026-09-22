package employee_management.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import employee_management.dto.LoginRequest;
import employee_management.dto.LoginResponse;
import employee_management.dto.RegisterRequest;
import employee_management.entity.User;
import employee_management.service.AuthService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  public ResponseEntity<User> register(
      @Valid @RequestBody RegisterRequest request) {

    User created = authService.register(request);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(created);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(
      @Valid @RequestBody LoginRequest request) {

    return ResponseEntity.ok(
        authService.login(request));
  }

  @GetMapping("/me")
  public ResponseEntity<Map<String, Object>> me(Authentication authentication) {

    return ResponseEntity.ok(Map.of(
        "username", authentication.getName(),
        "authorities", authentication.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .toList()));
  }
}
