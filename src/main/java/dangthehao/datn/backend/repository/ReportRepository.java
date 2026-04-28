package dangthehao.datn.backend.repository;

import dangthehao.datn.backend.dto.report.*;

import java.time.LocalDate;
import java.util.List;

public interface ReportRepository {
  RoomInventoryOverviewStatsDto getInventoryOverviewStats();

  TodayCheckInOverviewStatsDto getTodayCheckInOverviewStats();

  RevenueOverviewStatsDto getRevenueOverviewStats();

  FinancialKpiDto getFinancialKpi(LocalDate startDate, LocalDate endDate);

  List<DailyRevenueTrendDto> getDailyRevenueTrend(LocalDate startDate, LocalDate endDate);
}
