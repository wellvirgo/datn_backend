package dangthehao.datn.backend.service;

import dangthehao.datn.backend.entity.PricingRule;
import dangthehao.datn.backend.entity.RuleCondition;
import dangthehao.datn.backend.repository.PricingRuleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class PricingEngineService {
  PricingRuleRepository pricingRuleRepo;

  public BigDecimal calculateFinalDynamicPrice(
      Long roomTypeId,
      BigDecimal staticPrice,
      LocalDate checkIn,
      long totalAllotment,
      long availableRoom) {
    long leadTime = Math.max(0, ChronoUnit.DAYS.between(LocalDate.now(), checkIn));

    BigDecimal occupancyRate =
        BigDecimal.valueOf(totalAllotment - availableRoom)
            .divide(BigDecimal.valueOf(totalAllotment), 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100));

    List<PricingRule> dynamicRules = pricingRuleRepo.findActiveDynamicRules(roomTypeId);
    List<PricingRule> applicableRules =
        dynamicRules.stream()
            .filter(rule -> isDynamicConditionMatch(rule, leadTime, occupancyRate))
            .sorted(Comparator.comparing(PricingRule::getPriorityLevel))
            .toList();

    BigDecimal finalPrice = staticPrice;
    for (PricingRule rule : applicableRules) {
      finalPrice = calculateTotalPrice(rule, staticPrice, finalPrice);
    }

    return finalPrice.max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
  }

  private boolean isDynamicConditionMatch(
      PricingRule rule, long leadTimeDays, BigDecimal occupancyRate) {
    for (RuleCondition condition : rule.getRuleConditions()) {
      if ("LEAD_TIME".equals(rule.getRuleType())) {
        Long minLead = condition.getMinLeadDays();
        Long maxLead = condition.getMaxLeadDays();
        if (minLead != null && leadTimeDays < minLead) return false;
        if (maxLead != null && leadTimeDays > maxLead) return false;
      } else if ("OCCUPANCY".equals(rule.getRuleType())) {
        BigDecimal minOcc = condition.getMinOccupancyRate();
        BigDecimal maxOcc = condition.getMaxOccupancyRate();
        if (minOcc != null && occupancyRate.compareTo(minOcc) < 0) return false;
        if (maxOcc != null && occupancyRate.compareTo(maxOcc) > 0) return false;
      }
    }

    return true;
  }

  private BigDecimal calculateTotalPrice(
      PricingRule rule, BigDecimal staticPrice, BigDecimal accumulatedPrice) {
    BigDecimal adjustmentValue = rule.getAdjustmentValue();
    if ("FIXED_AMOUNT".equals(rule.getAdjustmentType())) {
      return accumulatedPrice.add(adjustmentValue);
    } else if ("PERCENTAGE".equals(rule.getAdjustmentType())) {
      BigDecimal percentage =
          adjustmentValue.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
      BigDecimal amountToChange = staticPrice.multiply(percentage);
      return accumulatedPrice.add(amountToChange);
    }

    return accumulatedPrice;
  }
}
