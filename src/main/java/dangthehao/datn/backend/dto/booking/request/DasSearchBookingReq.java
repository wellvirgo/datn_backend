package dangthehao.datn.backend.dto.booking.request;

import dangthehao.datn.backend.dto.common.PageableRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DasSearchBookingReq extends PageableRequest {
    String code;
    String bookingStatus;
    String paymentStatus;
    LocalDate startDate;
    LocalDate endDate;
}
