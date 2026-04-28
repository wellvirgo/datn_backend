package dangthehao.datn.backend.dto.booking.request;

import dangthehao.datn.backend.annotation.DateRange;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@DateRange(
    message = "Ngày trả phòng không hợp lệ",
    startDate = "checkInDate",
    endDate = "checkOutDate")
public class BookingReq {
  @NotBlank(message = "Tên khách hàng không được để trống")
  @Size(min = 2, max = 100, message = "Tên khách hàng phải từ 2 đến 100 ký tự")
  private String customerName;

  @NotBlank(message = "Số điện thoại không được để trống")
  @Pattern(
      regexp = "^(0|\\+84)[35789][0-9]{8}$",
      message = "Số điện thoại không đúng định dạng (VD: 0901234567)")
  private String customerPhone;

  @NotNull(message = "Ngày nhận phòng không được để trống")
  @FutureOrPresent(message = "Ngày nhận phòng phải là hôm nay hoặc trong tương lai")
  private LocalDate checkInDate;

  @NotNull(message = "Ngày trả phòng không được để trống")
  private LocalDate checkOutDate;

  @Min(value = 1, message = "Số đêm lưu trú tối thiểu là 1")
  private int nights;

  @NotEmpty(message = "Danh sách phòng đặt không được để trống")
  @Valid
  private List<RoomBookingReq> rooms;
}
