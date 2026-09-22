package employee_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import employee_management.dto.DepartmentStatistic;
import employee_management.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  List<Employee> findByNameContainingIgnoreCase(String name);

  List<Employee> findByDepartmentNameContainingIgnoreCase(String departmentName);

  /**
   * Đếm số nhân viên của từng phòng ban.
   *
   * <p>Truy vấn đi từ {@code Department} và dùng {@code LEFT JOIN} để phòng ban
   * chưa có ai vẫn xuất hiện với số đếm 0. Nếu đi từ {@code Employee} rồi join
   * ngược lại, những phòng ban rỗng sẽ biến mất khỏi báo cáo.
   *
   * <p>{@code SELECT new ...} dựng thẳng record DepartmentStatistic, nhờ đó
   * không phải tự bóc tách mảng Object[].
   */
  @Query("""
      SELECT new employee_management.dto.DepartmentStatistic(d.name, COUNT(e))
      FROM Department d
      LEFT JOIN Employee e ON e.department = d
      GROUP BY d.id, d.name
      ORDER BY COUNT(e) DESC, d.name ASC
      """)
  List<DepartmentStatistic> countEmployeesByDepartment();

  /**
   * Đếm nhân viên chưa được gán phòng ban.
   *
   * <p>Những người này không nằm trong kết quả của
   * {@link #countEmployeesByDepartment()}, nên phải đếm riêng thì tổng mới khớp.
   */
  @Query("SELECT COUNT(e) FROM Employee e WHERE e.department IS NULL")
  long countEmployeesWithoutDepartment();
}
