package dangthehao.datn.backend.dto.roomType.request;

import dangthehao.datn.backend.constant.SmokingPolicy;
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
public class UpdateRoomSpaceReq {
  @NotNull(message = "Cần chọn kiểu giường")
  Long bedTypeId;

  @NotBlank(message = "Cách bố trí giường không được để trống")
  @Size(max = 255, message = "Mô tả bố trí giường không vượt quá 255 ký tự")
  String bedArrangement;

  @NotNull(message = "Diện tích phòng không được để trống")
  @DecimalMin(value = "5.0", message = "Diện tích phòng tối thiểu là 5 m2")
  @Digits(
      integer = 5,
      fraction = 2,
      message = "Diện tích phòng không hợp lệ (tối đa 5 chữ số phần nguyên và 2 chữ số thập phân)")
  BigDecimal areaSize;

  @NotBlank(message = "Loại phòng tắm không được để trống")
  String bathroomType;

  @NotNull(message = "Cần thiết lập chính sách hút thuốc")
  SmokingPolicy smokingPolicy;
}
