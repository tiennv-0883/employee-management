package employee_management.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import employee_management.dto.DepartmentStatistic;
import employee_management.dto.EmployeeStatistics;
import employee_management.repository.DepartmentRepository;
import employee_management.repository.EmployeeRepository;

@Service
public class StatisticsService {

  private static final Logger logger = LoggerFactory.getLogger(StatisticsService.class);

  private final EmployeeRepository employeeRepository;
  private final DepartmentRepository departmentRepository;

  public StatisticsService(
      EmployeeRepository employeeRepository,
      DepartmentRepository departmentRepository) {

    this.employeeRepository = employeeRepository;
    this.departmentRepository = departmentRepository;
  }

  public List<DepartmentStatistic> countByDepartment() {
    return employeeRepository.countEmployeesByDepartment();
  }

  public long countAllEmployees() {
    return employeeRepository.count();
  }

  public EmployeeStatistics collect() {

    EmployeeStatistics statistics = new EmployeeStatistics(
        employeeRepository.count(),
        departmentRepository.count(),
        employeeRepository.countEmployeesWithoutDepartment(),
        employeeRepository.countEmployeesByDepartment());

    logger.debug(
        "Statistics collected: totalEmployees={}, totalDepartments={}",
        statistics.totalEmployees(),
        statistics.totalDepartments());

    return statistics;
  }
}
