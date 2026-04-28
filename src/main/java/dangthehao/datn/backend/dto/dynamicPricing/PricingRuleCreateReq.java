package dangthehao.datn.backend.dto.dynamicPricing;

import dangthehao.datn.backend.constant.PricingAdjustmentType;
import dangthehao.datn.backend.constant.PricingRuleType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PricingRuleCreateReq {
  @NotBlank(message = "Tên quy tắc không được để trống")
  @Size(max = 150, message = "Tên quy tắc không được vượt quá 150 ký tự")
  String ruleName;

  @NotNull(message = "Loại quy tắc không được để trống")
  PricingRuleType ruleType;

  @NotNull(message = "Loại điều chỉnh (phần trăm/tiền mặt) không được để trống")
  PricingAdjustmentType adjustmentType;

  @NotNull(message = "Giá trị điều chỉnh không được để trống")
  BigDecimal adjustmentValue;

  @Min(value = 1, message = "Mức độ ưu tiên phải từ 1 trở lên")
  Long priorityLevel;

  @NotEmpty(
      message = "Danh sách điều kiện áp dụng không được để trống (phải có ít nhất 1 điều kiện)")
  @Valid
  List<RuleConditionCreateReq> ruleConditions;
}
