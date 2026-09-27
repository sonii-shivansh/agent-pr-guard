package dev.agentprguard.model;

import com.fasterxml.jackson.annotation.JsonValue;

/** Risk categories for findings. */
public enum Category {
  SECURITY("security"),
  AUTHORIZATION("authorization"),
  SECRETS("secrets"),
  DEPENDENCY("dependency"),
  API("api"),
  DATABASE("database"),
  ARCHITECTURE("architecture"),
  NETWORK("network"),
  INFRASTRUCTURE("infrastructure"),
  TESTING("testing"),
  OTHER("other");

  private final String displayName;

  Category(String displayName) {
    this.displayName = displayName;
  }

  @JsonValue
  public String getDisplayName() {
    return displayName;
  }

  public static Category fromString(String value) {
    if (value == null || value.isBlank()) {
      return OTHER;
    }
    return valueOf(value.toUpperCase());
  }
}
