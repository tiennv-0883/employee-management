package employee_management.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

  private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

  private final SecretKey secretKey;
  private final long expirationMs;

  public JwtService(
      @Value("${app.jwt.secret}") String secret,
      @Value("${app.jwt.expiration-ms}") long expirationMs) {

    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.expirationMs = expirationMs;
  }

  public String generateToken(UserDetails userDetails) {

    Date now = new Date();
    Date expiry = new Date(now.getTime() + expirationMs);

    String role = userDetails.getAuthorities()
        .stream()
        .map(authority -> authority.getAuthority())
        .filter(authority -> authority.startsWith("ROLE_"))
        .findFirst()
        .orElse("ROLE_USER");

    return Jwts.builder()
        .subject(userDetails.getUsername())
        .claim("role", role)
        .issuedAt(now)
        .expiration(expiry)
        .signWith(secretKey)
        .compact();
  }

  public String extractUsername(String token) {

    try {
      Claims claims = Jwts.parser()
          .verifyWith(secretKey)
          .build()
          .parseSignedClaims(token)
          .getPayload();

      return claims.getSubject();

    } catch (JwtException | IllegalArgumentException ex) {
      logger.debug("Token khong hop le: {}", ex.getMessage());
      return null;
    }
  }

  public long getExpirationMs() {
    return expirationMs;
  }
}
