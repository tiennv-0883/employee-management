package employee_management.dto;

import employee_management.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

    @NotBlank(message = "Username không được để trống") @Size(min = 3, max = 50, message = "Username phải từ 3 đến 50 ký tự") String username,

    @NotBlank(message = "Password không được để trống") @Size(min = 6, message = "Password phải từ 6 ký tự trở lên") String password,

    Role role) {
}
