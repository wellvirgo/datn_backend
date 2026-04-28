package dangthehao.datn.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "RULE_CONDITIONS", schema = "HOTEL_APP")
public class RuleCondition {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "CONDITION_ID", nullable = false)
  private Long id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.RESTRICT)
  @JoinColumn(name = "RULE_ID", nullable = false)
  private PricingRule rule;

  @ManyToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.RESTRICT)
  @JoinColumn(name = "ROOM_TYPE_ID")
  private RoomType roomType;

  @Column(name = "START_DATE")
  private LocalDate startDate;

  @Column(name = "END_DATE")
  private LocalDate endDate;

  @Size(max = 20)
  @Column(name = "DAYS_OF_WEEK", length = 20)
  private String daysOfWeek;

  @Column(name = "MIN_OCCUPANCY_RATE", precision = 5, scale = 2)
  private BigDecimal minOccupancyRate;

  @Column(name = "MAX_OCCUPANCY_RATE", precision = 5, scale = 2)
  private BigDecimal maxOccupancyRate;

  @Column(name = "MIN_LEAD_DAYS")
  private Long minLeadDays;

  @Column(name = "MAX_LEAD_DAYS")
  private Long maxLeadDays;
}
