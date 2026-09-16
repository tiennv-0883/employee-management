package employee_management.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import employee_management.model.Employee;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

  private final List<Employee> employees = new ArrayList<>();

  public EmployeeController() {
    employees.add(
        new Employee(1L, "Nguyen Van A", "a@gmail.com", "IT"));

    employees.add(
        new Employee(2L, "Tran Thi B", "b@gmail.com", "HR"));
  }

  @GetMapping
  public List<Employee> getAllEmployees() {
    return employees;
  }

  @PostMapping
  public Employee addEmployee(@RequestBody Employee employee) {

    employees.add(employee);

    return employee;
  }

  @GetMapping("/{id}")
  public ResponseEntity<Employee> getEmployeeById(
      @PathVariable Long id) {

    for (Employee employee : employees) {

      if (employee.getId().equals(id)) {
        return ResponseEntity.ok(employee);
      }
    }

    return ResponseEntity.notFound().build();
  }

  @GetMapping("/search")
  public ResponseEntity<List<Employee>> searchEmployees(
      @RequestParam String name) {

    List<Employee> result = new ArrayList<>();

    for (Employee employee : employees) {

      if (employee.getName()
          .toLowerCase()
          .contains(name.toLowerCase())) {

        result.add(employee);
      }
    }

    return ResponseEntity.ok(result);
  }
}