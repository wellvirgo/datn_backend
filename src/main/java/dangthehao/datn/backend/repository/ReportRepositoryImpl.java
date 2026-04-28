package dangthehao.datn.backend.repository;

import dangthehao.datn.backend.dto.report.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Repository
@Transactional(readOnly = true)
public class ReportRepositoryImpl implements ReportRepository {
  EntityManager em;

  @Override
  public RoomInventoryOverviewStatsDto getInventoryOverviewStats() {
    String procedure = "get_daily_inventory_stats".toUpperCase();
    StoredProcedureQuery query = em.createStoredProcedureQuery(procedure);

    query.registerStoredProcedureParameter("p_booked_today", Integer.class, ParameterMode.OUT);
    query.registerStoredProcedureParameter("p_total_rooms", Integer.class, ParameterMode.OUT);
    query.registerStoredProcedureParameter("p_diff_vs_yesterday", Integer.class, ParameterMode.OUT);

    query.execute();

    return RoomInventoryOverviewStatsDto.builder()
        .bookedTodayCount((Integer) query.getOutputParameterValue("p_booked_today"))
        .totalRoomsToday((Integer) query.getOutputParameterValue("p_total_rooms"))
        .diffBookedVsYesterday((Integer) query.getOutputParameterValue("p_diff_vs_yesterday"))
        .build();
  }

  @Override
  public TodayCheckInOverviewStatsDto getTodayCheckInOverviewStats() {
    String procedure = "get_check_in_today".toUpperCase();
    StoredProcedureQuery query = em.createStoredProcedureQuery(procedure);

    query.registerStoredProcedureParameter("p_total_check_in", Integer.class, ParameterMode.OUT);
    query.registerStoredProcedureParameter("p_pending_check_in", Integer.class, ParameterMode.OUT);

    query.execute();

    return TodayCheckInOverviewStatsDto.builder()
        .totalCheckIn((Integer) query.getOutputParameterValue("p_total_check_in"))
        .totalPendingCheckIn((Integer) query.getOutputParameterValue("p_pending_check_in"))
        .build();
  }

  @Override
  public RevenueOverviewStatsDto getRevenueOverviewStats() {
    String procedure = "get_today_revenue".toUpperCase();
    StoredProcedureQuery query = em.createStoredProcedureQuery(procedure);

    query.registerStoredProcedureParameter("p_today_revenue", BigDecimal.class, ParameterMode.OUT);
    query.registerStoredProcedureParameter(
        "p_this_week_revenue", BigDecimal.class, ParameterMode.OUT);
    query.registerStoredProcedureParameter(
        "p_last_week_revenue", BigDecimal.class, ParameterMode.OUT);

    query.execute();

    return RevenueOverviewStatsDto.builder()
        .todayRevenue((BigDecimal) query.getOutputParameterValue("p_today_revenue"))
        .thisWeekRevenue((BigDecimal) query.getOutputParameterValue("p_this_week_revenue"))
        .lastWeekRevenue((BigDecimal) query.getOutputParameterValue("p_last_week_revenue"))
        .build();
  }

  @Override
  public FinancialKpiDto getFinancialKpi(LocalDate startDate, LocalDate endDate) {
    String procedure = "get_financial_kpis".toUpperCase();
    StoredProcedureQuery query = em.createStoredProcedureQuery(procedure);

    query.registerStoredProcedureParameter("p_start_date", LocalDate.class, ParameterMode.IN);
    query.registerStoredProcedureParameter("p_end_date", LocalDate.class, ParameterMode.IN);
    query.setParameter("p_start_date", startDate);
    query.setParameter("p_end_date", endDate);

    query.registerStoredProcedureParameter("p_total_revenue", BigDecimal.class, ParameterMode.OUT);
    query.registerStoredProcedureParameter("p_adr", BigDecimal.class, ParameterMode.OUT);
    query.registerStoredProcedureParameter("p_revpar", BigDecimal.class, ParameterMode.OUT);
    query.registerStoredProcedureParameter("p_total_debt", BigDecimal.class, ParameterMode.OUT);

    query.execute();

    return FinancialKpiDto.builder()
        .totalRevenue((BigDecimal) query.getOutputParameterValue("p_total_revenue"))
        .averageDailyRate((BigDecimal) query.getOutputParameterValue("p_adr"))
        .revenuePerAvailableRoom((BigDecimal) query.getOutputParameterValue("p_revpar"))
        .totalDebt((BigDecimal) query.getOutputParameterValue("p_total_debt"))
        .build();
  }

  @Override
  public List<DailyRevenueTrendDto> getDailyRevenueTrend(LocalDate startDate, LocalDate endDate) {
    String procedure = "get_daily_revenue_trend".toUpperCase();
    StoredProcedureQuery query = em.createStoredProcedureQuery(procedure);

    query.registerStoredProcedureParameter("p_start_date", LocalDate.class, ParameterMode.IN);
    query.registerStoredProcedureParameter("p_end_date", LocalDate.class, ParameterMode.IN);
    query.setParameter("p_start_date", startDate);
    query.setParameter("p_end_date", endDate);

    query.registerStoredProcedureParameter("p_result", void.class, ParameterMode.REF_CURSOR);

    query.execute();
    List<Object[]> results = query.getResultList();
    List<DailyRevenueTrendDto> dailyRevenueTrendDtos = new ArrayList<>();
    for (Object[] result : results) {
      Timestamp timestamp = (Timestamp) result[0];
      BigDecimal totalRevenue = (BigDecimal) result[1];
      LocalDate revenueDate = null;

      if (timestamp != null) {
        revenueDate = timestamp.toLocalDateTime().toLocalDate();
      }

      dailyRevenueTrendDtos.add(
          DailyRevenueTrendDto.builder().reportDate(revenueDate).revenue(totalRevenue).build());
    }

    return dailyRevenueTrendDtos;
  }
}
