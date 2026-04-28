package dangthehao.datn.backend.dto.report;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OverviewRes {
  Integer bookedTodayCount;
  Integer totalRoomsToday;
  Integer diffBookedVsYesterday;
  Integer totalCheckIn;
  Integer pendingCheckIn;
  BigDecimal todayRevenue;
  Double diffRevenueVsLastWeekPercentage;
  Long roomOccupancyPercentage;
  String occupancyStatusLabel;
  String occupancyStatusColor;
}
