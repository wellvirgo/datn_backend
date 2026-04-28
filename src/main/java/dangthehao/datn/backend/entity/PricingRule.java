package dangthehao.datn.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "PRICING_RULES", schema = "HOTEL_APP")
@EntityListeners(AuditingEntityListener.class)
public class PricingRule {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @Size(max = 150)
  @NotNull
  @Column(name = "RULE_NAME", nullable = false, length = 150)
  private String ruleName;

  @Size(max = 50)
  @NotNull
  @Column(name = "RULE_TYPE", nullable = false, length = 50)
  private String ruleType;

  @Size(max = 20)
  @NotNull
  @Column(name = "ADJUSTMENT_TYPE", nullable = false, length = 20)
  private String adjustmentType;

  @NotNull
  @Column(name = "ADJUSTMENT_VALUE", nullable = false, precision = 10, scale = 2)
  private BigDecimal adjustmentValue;

  @ColumnDefault("1")
  @Column(name = "PRIORITY_LEVEL")
  private Long priorityLevel;

  @ColumnDefault("1")
  @Column(name = "ACTIVE")
  private Boolean active = true;

  @CreatedDate
  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "CREATED_AT")
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "rule", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<RuleCondition> ruleConditions = new LinkedHashSet<>();

  public void addRuleCondition(RuleCondition ruleCondition) {
    ruleConditions.add(ruleCondition);
    ruleCondition.setRule(this);
  }

  public void removeRuleCondition(RuleCondition ruleCondition) {
    ruleConditions.remove(ruleCondition);
    ruleCondition.setRule(null);
  }
}
