package employee_management.exception;

public class EmployeeNotFoundException extends RuntimeException {

  public EmployeeNotFoundException(Long id) {
    super("Employee không tồn tại với id: " + id);
  }
}