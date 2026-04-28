package dangthehao.datn.backend.service;

import dangthehao.datn.backend.constant.OccupancyStatus;
import dangthehao.datn.backend.dto.report.*;
import dangthehao.datn.backend.repository.ReportRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ReportService {
  ReportRepository reportRepository;

  public OverviewRes getOverviewStats() {
    CompletableFuture<RoomInventoryOverviewStatsDto> inventoryFuture =
        CompletableFuture.supplyAsync(reportRepository::getInventoryOverviewStats);

    CompletableFuture<TodayCheckInOverviewStatsDto> checkInFuture =
        CompletableFuture.supplyAsync(reportRepository::getTodayCheckInOverviewStats);

    CompletableFuture<RevenueOverviewStatsDto> revenueFuture =
        CompletableFuture.supplyAsync(reportRepository::getRevenueOverviewStats);

    CompletableFuture.allOf(inventoryFuture, checkInFuture, revenueFuture).join();

    RoomInventoryOverviewStatsDto inventoryStats = inventoryFuture.join();
    TodayCheckInOverviewStatsDto checkInStats = checkInFuture.join();
    RevenueOverviewStatsDto revenueStats = revenueFuture.join();

    BigDecimal thisWeekRevenue = revenueStats.getThisWeekRevenue();
    BigDecimal lastWeekRevenue = revenueStats.getLastWeekRevenue();
    Double diffRevenueVsLastWeekPercentage =
        calculateGrowthPercentage(thisWeekRevenue, lastWeekRevenue);

    Long occupancyPercentage =
        calculateRoomOccupancyPercentage(
            inventoryStats.getBookedTodayCount(), inventoryStats.getTotalRoomsToday());
    OccupancyStatus occupancyStatus = OccupancyStatus.fromPercentage(occupancyPercentage);

    return OverviewRes.builder()
        .bookedTodayCount(inventoryStats.getBookedTodayCount())
        .totalRoomsToday(inventoryStats.getTotalRoomsToday())
        .diffBookedVsYesterday(inventoryStats.getDiffBookedVsYesterday())
        .totalCheckIn(checkInStats.getTotalCheckIn())
        .pendingCheckIn(checkInStats.getTotalCheckIn())
        .todayRevenue(revenueStats.getTodayRevenue())
        .diffRevenueVsLastWeekPercentage(diffRevenueVsLastWeekPercentage)
        .roomOccupancyPercentage(occupancyPercentage)
        .occupancyStatusLabel(occupancyStatus.getLabel())
        .occupancyStatusColor(occupancyStatus.getColor())
        .build();
  }

  public FinancialKpiDto getFinancialKpi(LocalDate startDate, LocalDate endDate) {
    return reportRepository.getFinancialKpi(startDate, endDate);
  }

  public List<DailyRevenueTrendDto> getDailyRevenueTrend(LocalDate startDate, LocalDate endDate) {
    return reportRepository.getDailyRevenueTrend(startDate, endDate);
  }

  private Double calculateGrowthPercentage(BigDecimal thisWeekRevenue, BigDecimal lastWeekRevenue) {
    if (thisWeekRevenue == null) thisWeekRevenue = BigDecimal.ZERO;
    if (lastWeekRevenue == null) lastWeekRevenue = BigDecimal.ZERO;

    if (lastWeekRevenue.compareTo(BigDecimal.ZERO) == 0) {
      return thisWeekRevenue.compareTo(BigDecimal.ZERO) > 0 ? 100.0 : 0.0;
    }

    return thisWeekRevenue
        .subtract(lastWeekRevenue)
        .multiply(new BigDecimal(100L))
        .divide(lastWeekRevenue, 2, RoundingMode.HALF_UP)
        .doubleValue();
  }

  private Long calculateRoomOccupancyPercentage(Integer bookedTodayCount, Integer totalRoomsToday) {
    if (bookedTodayCount == null) bookedTodayCount = 0;
    if (totalRoomsToday == null) totalRoomsToday = 0;

    if (totalRoomsToday == 0) return 0L;

    return Math.round((bookedTodayCount * 100.0 / totalRoomsToday));
  }
}
