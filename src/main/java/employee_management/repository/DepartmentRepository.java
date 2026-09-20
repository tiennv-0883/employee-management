package employee_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import employee_management.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

}