package employee_management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth

            // ---- Không cần đăng nhập ----
            .requestMatchers(
                "/api/auth/register",
                "/hello",
                "/employee-code",
                "/password",
                "/api/departments/**",
                "/api/reports/**",
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
        .httpBasic(Customizer.withDefaults());

    return http.build();
  }
}
