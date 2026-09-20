package employee_management.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import employee_management.entity.Department;
import employee_management.entity.Employee;
import employee_management.repository.DepartmentRepository;
import employee_management.repository.EmployeeRepository;

@Controller
@RequestMapping("/employees")
public class EmployeeViewController {

  private final EmployeeRepository employeeRepository;
  private final DepartmentRepository departmentRepository;

  public EmployeeViewController(
      EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {

    this.employeeRepository = employeeRepository;
    this.departmentRepository = departmentRepository;
  }

  @GetMapping("/list")
  public String employeeList(Model model) {

    List<Employee> employees = employeeRepository.findAll();

    model.addAttribute("employees", employees);

    return "employees/list";
  }

  @GetMapping("/add")
  public String showAddForm(Model model) {

    model.addAttribute(
        "employee",
        new Employee());

    model.addAttribute(
        "departments",
        departmentRepository.findAll());

    return "employees/add";
  }

  @PostMapping("/add")
  public String addEmployee(
      @ModelAttribute Employee employee,
      @RequestParam Long departmentId) {

    Department department = departmentRepository
        .findById(departmentId)
        .orElseThrow();

    employee.setDepartment(department);

    employeeRepository.save(employee);

    return "redirect:/employees/list";
  }

  @GetMapping("/search")
  public String searchEmployees(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String department,
      Model model) {

    List<Employee> employees;

    if (name != null && !name.isBlank()) {

      employees = employeeRepository
          .findByNameContainingIgnoreCase(name);

    } else if (department != null && !department.isBlank()) {

      employees = employeeRepository
          .findByDepartmentNameContainingIgnoreCase(
              department);

    } else {

      employees = employeeRepository.findAll();
    }

    model.addAttribute("employees", employees);

    return "employees/search";
  }
}