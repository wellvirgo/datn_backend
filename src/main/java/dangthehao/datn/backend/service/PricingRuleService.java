package dangthehao.datn.backend.service;

import dangthehao.datn.backend.constant.PricingAdjustmentType;
import dangthehao.datn.backend.dto.dynamicPricing.PricingRuleCreateReq;
import dangthehao.datn.backend.dto.dynamicPricing.RuleConditionCreateReq;
import dangthehao.datn.backend.entity.PricingRule;
import dangthehao.datn.backend.entity.RuleCondition;
import dangthehao.datn.backend.event.pricingRuleChanged.PricingRuleChangedEvent;
import dangthehao.datn.backend.exception.AppException;
import dangthehao.datn.backend.exception.ErrorCode;
import dangthehao.datn.backend.mapper.PricingRuleMapper;
import dangthehao.datn.backend.mapper.RuleConditionMapper;
import dangthehao.datn.backend.repository.PricingRuleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class PricingRuleService {
  PricingRuleRepository pricingRuleRepo;
  PricingRuleMapper pricingRuleMapper;
  RuleConditionMapper ruleConditionMapper;
  RoomTypeService roomTypeService;
  ApplicationEventPublisher eventPublisher;

  @Transactional
  public Long addPricingRule(PricingRuleCreateReq request) {
    if (request.getAdjustmentType() == PricingAdjustmentType.FIXED_AMOUNT
        && request.getAdjustmentValue().compareTo(BigDecimal.ZERO) == 0)
      throw new AppException(
          ErrorCode.BAD_REQUEST, "Kiểu thay đổi giá theo giá trị cố định không thể dùng giá trị 0");

    if (pricingRuleRepo.existsByRuleName(request.getRuleName()))
      throw new AppException(ErrorCode.BAD_REQUEST, "Luật giá đã tồn tại");

    PricingRule rule = pricingRuleMapper.toPricingRule(request);

    for (RuleConditionCreateReq conditionCreateReq : request.getRuleConditions()) {
      RuleCondition condition = ruleConditionMapper.toRuleCondition(conditionCreateReq);
      condition.setRoomType(roomTypeService.getById(conditionCreateReq.getRoomTypeId()));
      rule.addRuleCondition(condition);
    }

    rule = pricingRuleRepo.save(rule);
    eventPublisher.publishEvent(new PricingRuleChangedEvent(this));

    return rule.getId();
  }
}
