package dangthehao.datn.backend.repository;

import dangthehao.datn.backend.entity.PricingRule;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PricingRuleRepository extends JpaRepository<PricingRule, Long> {
  boolean existsByRuleName(String ruleName);

  @EntityGraph(attributePaths = {"ruleConditions"})
  @Query(
      """
        select pr from PricingRule pr join pr.ruleConditions rc
        where pr.active = true
        and pr.ruleType in ('OCCUPANCY', 'LEAD_TIME')
        and (rc.roomType.id is null or rc.roomType.id = :roomTypeId)
        """)
  List<PricingRule> findActiveDynamicRules(Long roomTypeId);
}
