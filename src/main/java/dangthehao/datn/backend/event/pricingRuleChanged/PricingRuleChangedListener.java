package dangthehao.datn.backend.event.pricingRuleChanged;

import dangthehao.datn.backend.service.DailyRoomRateService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PricingRuleChangedListener {
    DailyRoomRateService dailyRoomRateService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPricingRuleChanged(PricingRuleChangedEvent event) {
        dailyRoomRateService.calculateStaticDailyRateJob();
    }
}
