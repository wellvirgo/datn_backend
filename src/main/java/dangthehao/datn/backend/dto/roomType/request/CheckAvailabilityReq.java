package dangthehao.datn.backend.dto.roomType.request;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import dangthehao.datn.backend.annotation.DateRange;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@DateRange(message = "Ngày trả phòng không hợp lệ")

public class CheckAvailabilityReq {
  @NotNull(message = "Ngày nhận phòng không được để trống")
  @FutureOrPresent(message = "Ngày nhận phòng không được trong quá khứ")
  @JsonPropertyDescription("Ngày khách muốn nhận phòng (check-in), định dạng YYYY-MM-DD. Hãy tự động suy luận ngày này từ câu hỏi dựa trên thời gian hiện tại.")
  LocalDate checkIn;

  @NotNull(message = "Ngày trả phòng không được để trống")
  @JsonPropertyDescription("Ngày khách muốn trả phòng (check-out), định dạng YYYY-MM-DD. Hãy tự động suy luận ngày này từ câu hỏi.")
  LocalDate checkOut;
}
