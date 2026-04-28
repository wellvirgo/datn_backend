package dangthehao.datn.backend.dto.roomType.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomTypeDetailRes extends PublicDetailRoomTypeRes {
  String thumbnail;
  Short baseAdults;
  Short baseChildren;
  BigDecimal basePrice;
  Long bedTypeId;
  BigDecimal extraBedPrice;
  Long totalRooms;
  LocalDate createdAt;
  LocalDate updatedAt;
}
