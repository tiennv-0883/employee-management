package employee_management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import employee_management.service.PasswordService;
import employee_management.service.UtilityService;

@RestController 
public class HelloController {
  private final UtilityService utilityService;
  private final PasswordService passwordService;

  public HelloController(
    UtilityService unUtilityService, 
    PasswordService passwordService) {
      this.utilityService = unUtilityService;
      this.passwordService = passwordService;
  }

  @GetMapping("/hello")
  public String hello() {
    String name = utilityService.formatName(" Ngo Van Tien ");
    

    return "Hello " + name;
  }

  @GetMapping("/employee-code")
  public String employeeCode() {
    return utilityService.generateEmployeeCode();
  }

  @GetMapping("/password")
  public String password() {
    return passwordService.encode("123456");
  }
}
