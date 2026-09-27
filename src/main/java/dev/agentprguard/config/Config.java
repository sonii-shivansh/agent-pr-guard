package dev.agentprguard.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Main configuration file for Agent PR Guard. */
public class Config {
  @JsonProperty("version")
  private int version;

  @JsonProperty("policy")
  private Policy policy;

  @JsonProperty("rules")
  private Map<String, Boolean> rules;

  @JsonProperty("protected")
  private ProtectedPaths protectedPaths;

  public Config() {
    this.version = 1;
    this.policy = new Policy();
    this.rules = new HashMap<>();
    this.protectedPaths = new ProtectedPaths();
    initializeDefaultRules();
  }

  private void initializeDefaultRules() {
    rules.put("secrets", true);
    rules.put("security-config", true);
    rules.put("dependency", true);
    rules.put("api", true);
    rules.put("database", true);
    rules.put("network", true);
    rules.put("architecture", true);
    rules.put("testing", true);
  }

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }

  public Policy getPolicy() {
    return policy;
  }

  public void setPolicy(Policy policy) {
    this.policy = policy;
  }

  public Map<String, Boolean> getRules() {
    return Collections.unmodifiableMap(rules);
  }

  public void setRules(Map<String, Boolean> rules) {
    this.rules = rules;
  }

  public boolean isRuleEnabled(String ruleName) {
    return rules.getOrDefault(ruleName, true);
  }

  public ProtectedPaths getProtectedPaths() {
    return protectedPaths;
  }

  public void setProtectedPaths(ProtectedPaths protectedPaths) {
    this.protectedPaths = protectedPaths;
  }

  @Override
  public String toString() {
    return "Config{" +
        "version=" + version +
        ", policy=" + policy +
        ", rules=" + rules +
        ", protectedPaths=" + protectedPaths +
        '}';
  }

  /** Protected paths configuration. */
  public static class ProtectedPaths {
    @JsonProperty("paths")
    private List<String> paths;

    public ProtectedPaths() {
      this.paths = new ArrayList<>();
    }

    public List<String> getPaths() {
      return Collections.unmodifiableList(paths);
    }

    public void setPaths(List<String> paths) {
      this.paths = new ArrayList<>(paths);
    }

    public boolean matchesPath(String filePath) {
      for (String pattern : paths) {
        if (matchesPattern(filePath, pattern)) {
          return true;
        }
      }
      return false;
    }

    private boolean matchesPattern(String path, String pattern) {
      // Simple glob-like matching
      String regex = pattern
          .replace(".", "\\.")
          .replace("*", ".*")
          .replace("?", ".");
      return path.matches(regex);
    }
  }
}
