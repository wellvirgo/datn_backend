package dangthehao.datn.backend.dto.roomType.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchCriteriaRes {
  LocalDate checkIn;
  LocalDate checkOut;
  long nights;
}
