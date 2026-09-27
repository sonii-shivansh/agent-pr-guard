package dev.agentprguard.model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Severity levels for findings.
 *
 * <p>Ordered from lowest to highest risk.
 */
public enum Severity {
  INFO(0, "info"),
  LOW(1, "low"),
  MEDIUM(2, "medium"),
  HIGH(3, "high"),
  CRITICAL(4, "critical");

  private final int level;
  private final String displayName;

  Severity(int level, String displayName) {
    this.level = level;
    this.displayName = displayName;
  }

  @JsonValue
  public String getDisplayName() {
    return displayName;
  }

  public int getLevel() {
    return level;
  }

  public boolean isGreaterThanOrEqual(Severity other) {
    return this.level >= other.level;
  }

  public boolean isGreaterThan(Severity other) {
    return this.level > other.level;
  }

  public static Severity fromString(String value) {
    if (value == null || value.isBlank()) {
      return INFO;
    }
    return valueOf(value.toUpperCase());
  }
}
