package employee_management.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import employee_management.repository.EmployeeRepository;

@Service
public class UtilityService {

  private final EmployeeRepository employeeRepository;

  public UtilityService(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  public String formatName(String name) {
    return name.trim().toUpperCase();
  }

  public String generateEmployeeCode() {
    return "CODE-" + System.currentTimeMillis();
  }

  @Cacheable("employeeCount")
  public long countEmployees() {

    return employeeRepository.count();
  }
}
