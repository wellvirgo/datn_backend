package dangthehao.datn.backend.dto.serviceRoomType;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceItemRes {
    Long id;
    String name;
    String notes;
}
