package dangthehao.datn.backend.dto.roomType.request;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateBasicInfoReq {
  @Size(min = 3, max = 100, message = "Tên loại phòng phải từ 3 đến 100 ký tự")
  String name;

  @Size(max = 100, message = "Loại hướng nhìn không vượt quá 100 ký tự")
  String viewType;

  @NotNull(message = "Tổng số phòng không được để trống")
  @Min(value = 1)
  Long totalRooms;

  @Size(max = 2000, message = "Mô tả phòng tối đa 2000 ký tự")
  String description;
}
