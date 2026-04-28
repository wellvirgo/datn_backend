package dangthehao.datn.backend.dto.roomType.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PublicDetailRoomTypeRes {
  Long id;
  String name;
  String description;
  String roomTypeImgs;
  Short maxAdults;
  Short maxChildren;
  Short maxOccupancy;
  Double areaSize;
  String bedTypeName;
  String bedArrangement;
  String viewType;
  Boolean extraBedAllowed;
  String smokingPolicy;
  String bathroomType;
  String amenities;
  String services;
}
