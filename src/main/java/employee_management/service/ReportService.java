package employee_management.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import employee_management.dto.EmployeeReport;
import employee_management.repository.EmployeeRepository;

@Service
public class ReportService {

  public static final String EMPLOYEE_REPORT_CACHE = "employeeReport";

  private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

  private final EmployeeRepository employeeRepository;

  public ReportService(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  @Cacheable(EMPLOYEE_REPORT_CACHE)
  public EmployeeReport getEmployeeReport() {

    logger.info("Cache miss - dem lai so nhan vien tu database");

    long total = employeeRepository.count();

    return new EmployeeReport(total, LocalDateTime.now());
  }

  @CacheEvict(value = EMPLOYEE_REPORT_CACHE, allEntries = true)
  public void clearEmployeeReportCache() {
    logger.info("Da xoa cache bao cao nhan vien");
  }
}
