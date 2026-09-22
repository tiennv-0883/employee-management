package employee_management.dto;

public record LoginResponse(
    String token,
    String tokenType,
    long expiresInMs,
    String username,
    String role) {
}
