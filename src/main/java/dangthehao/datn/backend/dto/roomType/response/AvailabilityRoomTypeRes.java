package dangthehao.datn.backend.dto.roomType.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AvailabilityRoomTypeRes {
  Long id;
  String name;
  String description;
  BigDecimal price;
  String viewType;
  BigDecimal areaSize;
  Short baseAdults;
  Short baseChildren;
  Short maxAdults;
  Short maxChildren;
  Short maxOccupancy;
  String bedArrangement;
  Boolean extraBedAllowed;
  BigDecimal extraBedPrice;
  Long availableRoomQuantity;
  Long totalAllotment;
  String bedTypeName;
  String amenities;
  String thumbnail;
}
