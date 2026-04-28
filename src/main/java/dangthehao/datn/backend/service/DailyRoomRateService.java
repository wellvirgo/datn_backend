package dangthehao.datn.backend.service;

import dangthehao.datn.backend.repository.DailyRoomRateRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class DailyRoomRateService {
  DailyRoomRateRepository dailyRoomRateRepo;

  @Scheduled(cron = "0 0 0 * * ?")
  public void calculateStaticDailyRateJob() {
    final int daysAhead = 365; // ahead 1 year
    dailyRoomRateRepo.callCalculateStaticDailyRateJob(daysAhead);
  }
}
