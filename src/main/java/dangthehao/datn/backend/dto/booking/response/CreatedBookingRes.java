package dangthehao.datn.backend.dto.booking.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatedBookingRes {
    String bookingCode;
    String paymentUrl;
}
