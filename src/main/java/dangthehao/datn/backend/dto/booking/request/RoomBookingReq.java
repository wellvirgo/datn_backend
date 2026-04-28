package dangthehao.datn.backend.dto.booking.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomBookingReq {
  @NotNull(message = "ID loại phòng không được để trống")
  Long roomTypeId;

  @Min(value = 1, message = "Số lượng phòng đặt phải lớn hơn hoặc bằng 1")
  int roomQuantity;

  @Min(value = 1, message = "Phải có ít nhất 1 người lớn trong phòng")
  long adultsPerRoom;

  @Min(value = 0, message = "Số lượng trẻ em không được là số âm")
  long childrenPerRoom;

  @NotNull(message = "Giá phòng không được để trống")
  @DecimalMin(value = "0.0", message = "Giá phòng không được nhỏ hơn 0")
  BigDecimal price;
}
