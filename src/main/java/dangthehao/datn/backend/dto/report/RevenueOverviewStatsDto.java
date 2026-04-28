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
public class RevenueOverviewStatsDto {
    BigDecimal todayRevenue;
    BigDecimal thisWeekRevenue;
    BigDecimal lastWeekRevenue;
}
