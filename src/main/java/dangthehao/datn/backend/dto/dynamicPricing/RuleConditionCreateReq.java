package dangthehao.datn.backend.dto.dynamicPricing;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RuleConditionCreateReq {
  Long roomTypeId;

  @FutureOrPresent(message = "Ngày bắt đầu (startDate) phải từ hôm nay trở đi")
  LocalDate startDate;

  @FutureOrPresent(message = "Ngày kết thúc (endDate) phải từ hôm nay trở đi")
  LocalDate endDate;

  @Size(max = 20, message = "Chuỗi daysOfWeek không được vượt quá 20 ký tự")
  String daysOfWeek;

  @DecimalMin(value = "0.00", message = "Tỷ lệ lấp đầy tối thiểu (minOccupancyRate) không được âm")
  @DecimalMax(value = "90.00", message = "Tỷ lệ lấp đầy tối thiểu không được vượt quá 90%")
  @Digits(
      integer = 3,
      fraction = 2,
      message = "Định dạng số không hợp lệ (tối đa 3 chữ số nguyên, 2 chữ số thập phân)")
  BigDecimal minOccupancyRate;

  @DecimalMin(value = "0.00", message = "Tỷ lệ lấp đầy tối đa không được âm")
  @DecimalMax(value = "100.00", message = "Tỷ lệ lấp đầy tối đa không được vượt quá 100%")
  @Digits(
      integer = 3,
      fraction = 2,
      message = "Định dạng số không hợp lệ (tối đa 3 chữ số nguyên, 2 chữ số thập phân)")
  BigDecimal maxOccupancyRate;

  @Min(value = 0, message = "Thời gian đặt trước tối thiểu không được âm")
  Long minLeadDays;

  @Min(value = 0, message = "Thời gian đặt trước tối đa không được âm")
  Long maxLeadDays;
}
