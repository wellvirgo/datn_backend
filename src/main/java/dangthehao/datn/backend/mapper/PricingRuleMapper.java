package dangthehao.datn.backend.mapper;

import dangthehao.datn.backend.dto.dynamicPricing.PricingRuleCreateReq;
import dangthehao.datn.backend.entity.PricingRule;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PricingRuleMapper {
  PricingRule toPricingRule(PricingRuleCreateReq request);
}
