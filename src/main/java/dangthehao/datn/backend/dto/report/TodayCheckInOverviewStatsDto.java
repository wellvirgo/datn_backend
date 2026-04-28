package dangthehao.datn.backend.dto.report;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TodayCheckInOverviewStatsDto {
    Integer totalCheckIn;
    Integer totalPendingCheckIn;
}
