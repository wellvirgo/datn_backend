package dangthehao.datn.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PAYMENTS", schema = "HOTEL_APP")
@EntityListeners(AuditingEntityListener.class)
public class Payment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.RESTRICT)
  @JoinColumn(name = "BOOKING_ID", nullable = false)
  private Booking booking;

  @Size(max = 50)
  @NotNull
  @Column(name = "PAYMENT_METHOD", nullable = false, length = 50)
  private String paymentMethod;

  @Size(max = 100)
  @Column(name = "TRANSACTION_NO", length = 100)
  private String transactionNo;

  @NotNull
  @Column(name = "AMOUNT", nullable = false, precision = 12, scale = 2)
  private BigDecimal amount;

  @Builder.Default
  @Size(max = 50)
  @ColumnDefault("'PENDING'")
  @Column(name = "STATUS", length = 50)
  private String status = "PENDING";

  @CreatedDate
  @ColumnDefault("current_timestamp")
  @Column(name = "PAYMENT_DATE")
  private LocalDateTime paymentDate;

  @Column(name = "PAID_AT")
  private LocalDateTime paidAt;
}
