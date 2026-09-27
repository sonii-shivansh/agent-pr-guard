package dev.agentprguard.model;

import static org.assertj.core.api.Assertions.assertThat;

import dev.agentprguard.config.Config;
import dev.agentprguard.policy.PolicyEngine;
import java.util.List;
import org.junit.jupiter.api.Test;

class DomainModelTest {
  @Test
  void defaultPolicyBlocksCriticalFindings() {
    Finding finding = new Finding(Severity.CRITICAL, Category.SECURITY, "Risk", "Evidence");
    ScanResult result = new PolicyEngine(new Config()).evaluate("repo", null, "main", List.of(), List.of(finding));

    assertThat(result.isPassed()).isFalse();
    assertThat(result.getFindings().get(0).getPolicyAction()).isEqualTo(PolicyAction.BLOCK);
  }

  @Test
  void severityComparisonIsOrdered() {
    assertThat(Severity.CRITICAL.isGreaterThan(Severity.HIGH)).isTrue();
    assertThat(Severity.LOW.isGreaterThanOrEqual(Severity.INFO)).isTrue();
  }
}
