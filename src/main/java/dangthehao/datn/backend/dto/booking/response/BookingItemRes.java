package dangthehao.datn.backend.dto.booking.response;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingItemRes implements Serializable {
  Long id;
  String bookingCode;
  String customerName;
  String customerPhone;
  BigDecimal totalAmount;
  String paymentStatus;
  String bookingStatus;
  String stayDuration;
  LocalDate createdAt;
}
