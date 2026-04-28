package dangthehao.datn.backend.controller;

import dangthehao.datn.backend.dto.common.ApiResponse;
import dangthehao.datn.backend.dto.dynamicPricing.PricingRuleCreateReq;
import dangthehao.datn.backend.entity.PricingRule;
import dangthehao.datn.backend.service.PricingRuleService;
import dangthehao.datn.backend.util.ApiResponseBuilder;
import dangthehao.datn.backend.util.UriUtils;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pricing-rules")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PricingRuleController {
  PricingRuleService pricingRuleService;

  @PreAuthorize("hasRole('MANAGER')")
  @PostMapping
  public ResponseEntity<ApiResponse<Map<String, Object>>> addPricingRule(
      @Valid @RequestBody PricingRuleCreateReq request) {
    Long ruleId = pricingRuleService.addPricingRule(request);
    URI location = UriUtils.generateUri(ruleId);
    Map<String, Object> data = new HashMap<>();
    data.put("ruleId", ruleId);
    ApiResponse<Map<String, Object>> response = ApiResponseBuilder.success(data);

    return ResponseEntity.created(location).body(response);
  }
}
