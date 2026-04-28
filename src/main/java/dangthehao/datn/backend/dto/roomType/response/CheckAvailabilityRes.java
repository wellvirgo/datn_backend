package dangthehao.datn.backend.dto.roomType.response;

import dangthehao.datn.backend.dto.hotelSetting.CancellationPolicyRes;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CheckAvailabilityRes {
  SearchCriteriaRes searchCriteria;
  CancellationPolicyRes cancellationPolicy;
  List<AvailabilityRoomTypeRes> availableRooms;
}
