package employee_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import employee_management.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  List<Employee> findByNameContainingIgnoreCase(String name);

  List<Employee> findByDepartmentNameContainingIgnoreCase(String departmentName);
}