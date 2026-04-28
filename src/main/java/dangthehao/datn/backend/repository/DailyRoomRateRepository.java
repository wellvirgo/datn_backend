package dangthehao.datn.backend.repository;

import dangthehao.datn.backend.entity.DailyRoomRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyRoomRateRepository extends JpaRepository<DailyRoomRate, Long> {
  @Procedure("calculate_static_daily_rate")
  void callCalculateStaticDailyRateJob(@Param("p_days_ahead") int daysAhead);
}
