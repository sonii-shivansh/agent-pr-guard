package dev.agentprguard.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.agentprguard.model.PolicyAction;
import dev.agentprguard.model.Severity;
import java.util.HashMap;
import java.util.Map;

/** Policy configuration for severity-to-action mapping. */
public class Policy {
  @JsonProperty("critical")
  private PolicyLevelAction critical;

  @JsonProperty("high")
  private PolicyLevelAction high;

  @JsonProperty("medium")
  private PolicyLevelAction medium;

  @JsonProperty("low")
  private PolicyLevelAction low;

  @JsonProperty("info")
  private PolicyLevelAction info;

  public Policy() {
    // Defaults
    this.critical = new PolicyLevelAction().withAction(PolicyAction.BLOCK);
    this.high = new PolicyLevelAction().withAction(PolicyAction.REVIEW);
    this.medium = new PolicyLevelAction().withAction(PolicyAction.WARN);
    this.low = new PolicyLevelAction().withAction(PolicyAction.WARN);
    this.info = new PolicyLevelAction().withAction(PolicyAction.IGNORE);
  }

  public PolicyLevelAction getCritical() {
    return critical;
  }

  public void setCritical(PolicyLevelAction critical) {
    this.critical = critical;
  }

  public PolicyLevelAction getHigh() {
    return high;
  }

  public void setHigh(PolicyLevelAction high) {
    this.high = high;
  }

  public PolicyLevelAction getMedium() {
    return medium;
  }

  public void setMedium(PolicyLevelAction medium) {
    this.medium = medium;
  }

  public PolicyLevelAction getLow() {
    return low;
  }

  public void setLow(PolicyLevelAction low) {
    this.low = low;
  }

  public PolicyLevelAction getInfo() {
    return info;
  }

  public void setInfo(PolicyLevelAction info) {
    this.info = info;
  }

  public PolicyAction getActionForSeverity(Severity severity) {
    return switch (severity) {
      case CRITICAL -> critical.getAction();
      case HIGH -> high.getAction();
      case MEDIUM -> medium.getAction();
      case LOW -> low.getAction();
      case INFO -> info.getAction();
    };
  }

  public Map<String, Object> toMap() {
    Map<String, Object> map = new HashMap<>();
    map.put("critical", critical.getAction().getDisplayName());
    map.put("high", high.getAction().getDisplayName());
    map.put("medium", medium.getAction().getDisplayName());
    map.put("low", low.getAction().getDisplayName());
    map.put("info", info.getAction().getDisplayName());
    return map;
  }
}
