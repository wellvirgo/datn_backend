package dangthehao.datn.backend.mapper;

import dangthehao.datn.backend.dto.dynamicPricing.RuleConditionCreateReq;
import dangthehao.datn.backend.entity.RuleCondition;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RuleConditionMapper {
    RuleCondition toRuleCondition(RuleConditionCreateReq ruleConditionCreateReq);
}
