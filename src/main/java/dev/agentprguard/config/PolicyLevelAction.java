package dev.agentprguard.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.agentprguard.model.PolicyAction;

/** Policy action configuration for a severity level. */
public class PolicyLevelAction {
  @JsonProperty("action")
  private PolicyAction action;

  public PolicyLevelAction() {}

  public PolicyLevelAction(PolicyAction action) {
    this.action = action;
  }

  public PolicyAction getAction() {
    return action;
  }

  public void setAction(PolicyAction action) {
    this.action = action;
  }

  public PolicyLevelAction withAction(PolicyAction action) {
    this.action = action;
    return this;
  }
}
