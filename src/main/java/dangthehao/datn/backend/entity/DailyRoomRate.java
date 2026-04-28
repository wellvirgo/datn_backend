package dangthehao.datn.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "DAILY_ROOM_RATES", schema = "HOTEL_APP")
public class DailyRoomRate {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "RATE_ID", nullable = false)
  private Long id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.RESTRICT)
  @JoinColumn(name = "ROOM_TYPE_ID", nullable = false)
  private RoomType roomType;

  @NotNull
  @Column(name = "TARGET_DATE", nullable = false)
  private LocalDate targetDate;

  @NotNull
  @Column(name = "FINAL_PRICE", nullable = false, precision = 12, scale = 2)
  private BigDecimal finalPrice;

  @Size(max = 255)
  @Column(name = "APPLIED_RULE_IDS")
  private String appliedRuleIds;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "LAST_CALCULATED_AT")
  private LocalDateTime lastCalculatedAt;
}
