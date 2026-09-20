package employee_management.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import employee_management.entity.Department;
import employee_management.entity.Employee;
import employee_management.exception.EmployeeNotFoundException;
import employee_management.repository.DepartmentRepository;
import employee_management.repository.EmployeeRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
  private final EmployeeRepository employeeRepository;
  private final DepartmentRepository departmentRepository;
  private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

  public EmployeeController(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
    this.employeeRepository = employeeRepository;
    this.departmentRepository = departmentRepository;
  }

  @GetMapping
  public ResponseEntity<List<Employee>> getAllEmployees() {
    return ResponseEntity.ok(
        employeeRepository.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Employee> getEmployeeById(
      @PathVariable Long id) {

    Employee employee = employeeRepository
        .findById(id)
        .orElseThrow(() -> new EmployeeNotFoundException(id));

    return ResponseEntity.ok(employee);
  }

  @PostMapping
  public ResponseEntity<Employee> createEmployee(
      @Valid @RequestBody Employee employee) {

    Long departmentId = employee.getDepartment() != null
        ? employee.getDepartment().getId()
        : null;

    if (departmentId != null) {

      Department department = departmentRepository
          .findById(departmentId)
          .orElse(null);

      if (department == null) {
        return ResponseEntity.notFound().build();
      }

      employee.setDepartment(department);
    }

    logger.info(
        "Creating employee: name={}, email={}, departmentId={}",
        employee.getName(),
        employee.getEmail(),
        departmentId);

    Employee savedEmployee = employeeRepository.save(employee);

    logger.info(
        "Employee created successfully: id={}",
        savedEmployee.getId());

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(savedEmployee);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Employee> updateEmployee(
      @PathVariable Long id,
      @RequestBody Employee newEmployee) {

    logger.info("Updating employee: id={}", id);

    return employeeRepository.findById(id)
        .map(employee -> applyUpdate(employee, newEmployee))
        .orElseGet(() -> {
          logger.warn("Update failed, employee not found: id={}", id);
          return ResponseEntity.notFound().build();
        });
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteEmployee(
      @PathVariable Long id) {

    logger.info("Deleting employee: id={}", id);

    if (!employeeRepository.existsById(id)) {
      logger.warn("Delete failed, employee not found: id={}", id);
      return ResponseEntity.notFound().build();
    }

    employeeRepository.deleteById(id);

    logger.info("Employee deleted successfully: id={}", id);

    return ResponseEntity.noContent().build();
  }

  @GetMapping("/search")
  public ResponseEntity<List<Employee>> searchByName(
      @RequestParam String name) {

    return ResponseEntity.ok(
        employeeRepository
            .findByNameContainingIgnoreCase(name));
  }

  @GetMapping("/search/department")
  public ResponseEntity<List<Employee>> searchByDepartment(
      @RequestParam String name) {

    return ResponseEntity.ok(
        employeeRepository
            .findByDepartmentNameContainingIgnoreCase(name));
  }

  // Áp dụng dữ liệu mới lên nhân viên đã tồn tại rồi lưu lại
  private ResponseEntity<Employee> applyUpdate(
      Employee employee,
      Employee newEmployee) {

    employee.setName(newEmployee.getName());
    employee.setEmail(newEmployee.getEmail());

    // Chỉ đổi phòng ban khi client có gửi department.id lên
    if (newEmployee.getDepartment() != null
        && newEmployee.getDepartment().getId() != null) {

      Long depId = newEmployee.getDepartment().getId();
      Department dep = departmentRepository.findById(depId).orElse(null);

      // Id phòng ban không tồn tại trong DB
      if (dep == null) {
        logger.warn(
            "Update failed, department not found: employeeId={}, departmentId={}",
            employee.getId(),
            depId);
        return ResponseEntity.badRequest().build();
      }

      employee.setDepartment(dep);
    }

    Employee saved = employeeRepository.save(employee);

    logger.info(
        "Employee updated successfully: id={}, name={}, email={}",
        saved.getId(),
        saved.getName(),
        saved.getEmail());

    return ResponseEntity.ok(saved);
  }
}