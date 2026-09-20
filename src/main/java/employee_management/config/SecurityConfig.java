package employee_management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/hello",
                "/employee-code",
                "/password",
                "/api/employees/**",
                "/api/departments/**",
                // Spring forward sang /error để dựng body lỗi; không mở thì mọi
                // lỗi 400/404/500 đều bị Security nuốt và biến thành 403
                "/error")
            .permitAll()
            .anyRequest()
            .authenticated());
    return http.build();
  }
}
