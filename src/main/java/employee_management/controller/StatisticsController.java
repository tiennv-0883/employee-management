package employee_management.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import employee_management.dto.DepartmentStatistic;
import employee_management.dto.EmployeeStatistics;
import employee_management.service.StatisticsService;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

  private final StatisticsService statisticsService;

  public StatisticsController(StatisticsService statisticsService) {
    this.statisticsService = statisticsService;
  }

  @GetMapping
  public ResponseEntity<EmployeeStatistics> all() {

    return ResponseEntity.ok(
        statisticsService.collect());
  }

  @GetMapping("/by-department")
  public ResponseEntity<List<DepartmentStatistic>> byDepartment() {

    return ResponseEntity.ok(
        statisticsService.countByDepartment());
  }

  @GetMapping("/total-employees")
  public ResponseEntity<Map<String, Long>> totalEmployees() {

    return ResponseEntity.ok(
        Map.of("totalEmployees", statisticsService.countAllEmployees()));
  }
}
