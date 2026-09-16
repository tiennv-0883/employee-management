package employee_management.service;

import org.springframework.stereotype.Service;

@Service 
public class UtilityService {
  public String formatName(String name) {
    return name.trim().toUpperCase();
  }

  public String generateEmployeeCode() {
    return "CODE-" + System.currentTimeMillis();
  }
}
