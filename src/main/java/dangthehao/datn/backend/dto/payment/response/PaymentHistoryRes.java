package dangthehao.datn.backend.dto.payment.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentHistoryRes {
    String transactionNo;
    BigDecimal amount;
    LocalDateTime paidAt;
    String paymentMethod;
    String status;
}
