package employee_management.dto;

import java.util.List;

public record EmployeeStatistics(

    long totalEmployees,

    long totalDepartments,

    long employeesWithoutDepartment,

    List<DepartmentStatistic> byDepartment) {
}
