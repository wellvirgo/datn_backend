package dangthehao.datn.backend.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BOOKINGS", schema = "HOTEL_APP")
@EntityListeners(AuditingEntityListener.class)
public class Booking {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @Size(max = 50)
  @NotNull
  @Column(name = "BOOKING_CODE", nullable = false, length = 50)
  private String bookingCode;

  @Size(max = 256)
  @NotNull
  @Nationalized
  @Column(name = "CUSTOMER_NAME", nullable = false, length = 256)
  private String customerName;

  @Size(max = 20)
  @NotNull
  @Column(name = "CUSTOMER_PHONE", nullable = false, length = 20)
  private String customerPhone;

  @NotNull
  @Column(name = "CHECK_IN_DATE", nullable = false)
  private LocalDate checkInDate;

  @NotNull
  @Column(name = "CHECK_OUT_DATE", nullable = false)
  private LocalDate checkOutDate;

  @NotNull
  @Column(name = "TOTAL_AMOUNT", nullable = false, precision = 12, scale = 2)
  private BigDecimal totalAmount;

  @ColumnDefault("0")
  @Column(name = "DEPOSIT_AMOUNT", precision = 12, scale = 2)
  private BigDecimal depositAmount;

  @NotNull
  @Column(name = "BALANCE_DUE", nullable = false, precision = 12, scale = 2)
  private BigDecimal balanceDue;

  @NotNull
  @Column(name = "PAYMENT_DEADLINE", nullable = false)
  private LocalDateTime paymentDeadline;

  @Builder.Default
  @Size(max = 20)
  @Nationalized
  @ColumnDefault("'UNPAID'")
  @Column(name = "PAYMENT_STATUS", length = 20)
  private String paymentStatus = "UNPAID";

  @Builder.Default
  @Size(max = 20)
  @Nationalized
  @ColumnDefault("'PENDING_PAYMENT'")
  @Column(name = "BOOKING_STATUS", length = 20)
  private String bookingStatus = "PENDING_PAYMENT";

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.RESTRICT)
  @JoinColumn(name = "USER_ID", nullable = false)
  private User user;

  @NotNull
  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "CANCELLATION_RULE_SNAPSHOT", nullable = false)
  private String cancellationRuleSnapshot;

  @CreatedDate
  @ColumnDefault("sysdate")
  @Column(name = "CREATED_AT")
  private LocalDate createdAt;

  @LastModifiedDate
  @Column(name = "UPDATE_AT")
  private LocalDate updateAt;

  @OneToMany(mappedBy = "booking")
  private Set<BookingDetail> bookingDetails = new LinkedHashSet<>();

  @OneToMany(mappedBy = "booking")
  private Set<Payment> payments = new LinkedHashSet<>();

  public String getStayDuration() {
    if (checkInDate != null && checkOutDate != null) {
      return this.getCheckInDate() + " đến " + this.getCheckOutDate();
    }
    return "";
  }
}
