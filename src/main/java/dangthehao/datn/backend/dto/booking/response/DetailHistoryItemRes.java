package dangthehao.datn.backend.dto.booking.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DetailHistoryItemRes {
    Long id;
    String roomTypeName;
    String roomNumber;
    BigDecimal price;
    Long adults;
    Long children;
}
