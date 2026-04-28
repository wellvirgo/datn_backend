package dangthehao.datn.backend.dto.amenity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AmenityItemRes {
    Long id;
    String name;
    Boolean onRequest;
}
