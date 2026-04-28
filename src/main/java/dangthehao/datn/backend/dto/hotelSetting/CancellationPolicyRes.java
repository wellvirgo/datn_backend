package dangthehao.datn.backend.dto.hotelSetting;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CancellationPolicyRes {
  String status;
  String displayText;
  List<Map<String, Long>> rawPolicy;
}
