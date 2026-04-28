package dangthehao.datn.backend.dto.roomType.request;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateOccupancyReq {
  @Min(value = 1, message = "Phòng phải có sức chứa ít nhất 1 người lớn")
  @Max(value = 15, message = "Số người lớn tiêu chuẩn vượt quá giới hạn thực tế")
  Short baseAdults;

  @Min(value = 0, message = "Số trẻ em tiêu chuẩn không được là số âm")
  @Max(value = 15, message = "Số trẻ em tiêu chuẩn vượt quá giới hạn thực tế")
  Short baseChildren;

  @Min(value = 1, message = "Số người lớn tối đa phải từ 1 trở lên")
  @Max(value = 20, message = "Số người lớn tối đa vượt quá giới hạn thực tế")
  Short maxAdults;

  @Min(value = 0, message = "Số trẻ em tối đa không được là số âm")
  @Max(value = 20, message = "Số trẻ em tối đa vượt quá giới hạn thực tế")
  Short maxChildren;

  @NotNull(message = "Giá cơ bản không được để trống")
  @DecimalMin(value = "0.0", message = "Giá cơ bản không được là số âm")
  @Digits(integer = 12, fraction = 2, message = "Giá cơ bản quá lớn hoặc sai định dạng")
  BigDecimal basePrice;

  @NotNull(message = "Cần xác định phòng có cho phép thêm giường phụ không")
  Boolean extraBedAllowed;

  @DecimalMin(value = "0.0", message = "Giá giường phụ không được là số âm")
  @Digits(integer = 12, fraction = 2, message = "Giá giường phụ quá lớn hoặc sai định dạng")
  BigDecimal extraBedPrice;
}
