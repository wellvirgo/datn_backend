package dangthehao.datn.backend.controller;

import dangthehao.datn.backend.dto.common.ApiResponse;
import dangthehao.datn.backend.dto.report.DailyRevenueTrendDto;
import dangthehao.datn.backend.dto.report.FinancialKpiDto;
import dangthehao.datn.backend.dto.report.OverviewRes;
import dangthehao.datn.backend.service.ReportService;
import dangthehao.datn.backend.util.ApiResponseBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportController {
  ReportService reportService;

  @PreAuthorize("hasRole('RECEPTIONIST')")
  @GetMapping("/overview")
  public ResponseEntity<ApiResponse<OverviewRes>> getOverview() {
    OverviewRes result = reportService.getOverviewStats();
    return ResponseEntity.ok(ApiResponseBuilder.success(result));
  }

  @PreAuthorize("hasRole('MANAGER')")
  @GetMapping("/financial-kpi")
  public ResponseEntity<ApiResponse<FinancialKpiDto>> getFinancialKpi(
      @RequestParam(name = "start") LocalDate startDate,
      @RequestParam(name = "end") LocalDate endDate) {
    FinancialKpiDto result = reportService.getFinancialKpi(startDate, endDate);
    return ResponseEntity.ok(ApiResponseBuilder.success(result));
  }

  @PreAuthorize("hasRole('MANAGER')")
  @GetMapping("/daily-revenue-trend")
  public ResponseEntity<ApiResponse<List<DailyRevenueTrendDto>>> getDailyRevenueTrend(
      @RequestParam(name = "start") LocalDate startDate,
      @RequestParam(name = "end") LocalDate endDate) {
    List<DailyRevenueTrendDto> result = reportService.getDailyRevenueTrend(startDate, endDate);
    return ResponseEntity.ok(ApiResponseBuilder.success(result));
  }
}
