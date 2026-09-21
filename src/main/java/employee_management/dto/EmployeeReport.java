package employee_management.dto;

import java.time.LocalDateTime;

public record EmployeeReport(
    long totalEmployees,
    LocalDateTime generatedAt) {
}
