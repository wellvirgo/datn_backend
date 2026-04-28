package dangthehao.datn.backend.event.pricingRuleChanged;

import org.springframework.context.ApplicationEvent;

public class PricingRuleChangedEvent extends ApplicationEvent {
  public PricingRuleChangedEvent(Object source) {
    super(source);
  }
}
