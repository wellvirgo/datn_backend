package dangthehao.datn.backend.dto.booking.response;

import dangthehao.datn.backend.dto.payment.response.PaymentHistoryRes;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DasBookingDetailRes extends BookingHistoryItemRes {
  String accountEmail;
  List<PaymentHistoryRes> paymentHistory;
}
