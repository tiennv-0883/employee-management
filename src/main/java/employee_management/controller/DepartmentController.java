package employee_management.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import employee_management.entity.Department;
import employee_management.repository.DepartmentRepository;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

  private final DepartmentRepository departmentRepository;

  public DepartmentController(
      DepartmentRepository departmentRepository) {

    this.departmentRepository = departmentRepository;
  }

  @GetMapping
  public ResponseEntity<List<Department>> getAllDepartments() {

    return ResponseEntity.ok(
        departmentRepository.findAll());
  }

  @PostMapping
  public ResponseEntity<Department> createDepartment(
      @RequestBody Department department) {

    Department savedDepartment = departmentRepository.save(department);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(savedDepartment);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Department> getDepartmentById(
      @PathVariable Long id) {

    return departmentRepository
        .findById(id)
        .map(ResponseEntity::ok)
        .orElseGet(
            () -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteDepartment(
      @PathVariable Long id) {

    if (!departmentRepository.existsById(id)) {
      return ResponseEntity.notFound().build();
    }

    departmentRepository.deleteById(id);

    return ResponseEntity.noContent().build();
  }
}