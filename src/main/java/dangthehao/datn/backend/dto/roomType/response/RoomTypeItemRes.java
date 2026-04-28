package dangthehao.datn.backend.dto.roomType.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomTypeItemRes extends RoomTypeBaseRes {
  String description;
  String thumbnail;
  BigDecimal basePrice;
  Short maxAdults;
  Short maxChildren;
  Short maxOccupancy;
  Double areaSize;
  String viewType;
  Long totalRooms;
  Long bedTypeId;
  String bedTypeName;
}
