package employee_management.exception;

public class UsernameAlreadyExistsException extends RuntimeException {

  public UsernameAlreadyExistsException(String username) {
    super("Username đã tồn tại: " + username);
  }
}
