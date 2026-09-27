package dev.agentprguard.model;

import com.fasterxml.jackson.annotation.JsonValue;

/** Action to take when a finding matches policy rules. */
public enum PolicyAction {
  IGNORE("ignore"),
  WARN("warn"),
  REVIEW("review"),
  BLOCK("block");

  private final String displayName;

  PolicyAction(String displayName) {
    this.displayName = displayName;
  }

  @JsonValue
  public String getDisplayName() {
    return displayName;
  }

  public static PolicyAction fromString(String value) {
    if (value == null || value.isBlank()) {
      return WARN;
    }
    return valueOf(value.toUpperCase());
  }
}
