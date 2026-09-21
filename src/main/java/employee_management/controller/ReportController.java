package employee_management.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import employee_management.dto.EmployeeReport;
import employee_management.service.ReportService;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

  private final ReportService reportService;

  public ReportController(ReportService reportService) {
    this.reportService = reportService;
  }

  @GetMapping("/employees")
  public ResponseEntity<EmployeeReport> employeeReport() {

    return ResponseEntity.ok(
        reportService.getEmployeeReport());
  }

  @DeleteMapping("/employees/cache")
  public ResponseEntity<Void> clearCache() {

    reportService.clearEmployeeReportCache();

    return ResponseEntity.noContent().build();
  }
}
