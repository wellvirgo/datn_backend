package dangthehao.datn.backend.dto.booking.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingHistoryItemRes {
  Long id;
  String bookingCode;
  String customerName;
  String customerPhone;
  LocalDate checkInDate;
  LocalDate checkOutDate;
  BigDecimal totalAmount;
  BigDecimal paidAmount;
  BigDecimal depositAmount;
  BigDecimal balanceDue;
  LocalDateTime paymentDeadline;
  String paymentStatus;
  String bookingStatus;
  String cancellationDisplayText;
  List<DetailHistoryItemRes> details;
  LocalDate createdAt;
}
