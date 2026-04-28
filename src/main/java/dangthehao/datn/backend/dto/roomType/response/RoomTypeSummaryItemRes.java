package dangthehao.datn.backend.dto.roomType.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomTypeSummaryItemRes extends RoomTypeBaseRes {
  String thumbnail;
  String description;
  String bedTypeName;
  String viewType;
  Short maxAdults;
  Short maxChildren;
  Short maxOccupancy;
  Double areaSize;
  String smokingPolicy;
  String bathroomType;
  Boolean extraBedAllowed;
}
