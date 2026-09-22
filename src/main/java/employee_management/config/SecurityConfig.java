package employee_management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration configuration) throws Exception {

    return configuration.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth

            // ---- Không cần đăng nhập ----
            .requestMatchers(
                "/api/auth/register",
                "/api/auth/login",
                "/hello",
                "/employee-code",
                "/password",
                "/api/departments/**",
                "/api/reports/**",
                "/api/statistics/**",
                // Các trang giao diện Thymeleaf
                "/employees/**",
                // Tài nguyên tĩnh (css, js, ảnh) trong thư mục static
                "/css/**",
                "/js/**",
                "/images/**",
                "/actuator/**",
                // Spring forward sang /error để dựng body lỗi; không mở thì mọi
                // lỗi 400/404/500 đều bị Security nuốt và biến thành 403
                "/error")
            .permitAll()
            .requestMatchers(HttpMethod.GET, "/api/employees/**")
            .hasAnyRole("USER", "ADMIN")

            .requestMatchers("/api/employees/**")
            .hasRole("ADMIN")
            .anyRequest()
            .authenticated())
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(
            jwtAuthenticationFilter,
            UsernamePasswordAuthenticationFilter.class)
        .httpBasic(Customizer.withDefaults());

    return http.build();
  }
}
