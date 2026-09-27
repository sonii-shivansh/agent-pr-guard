package dev.agentprguard.policy;

import dev.agentprguard.config.Config;
import dev.agentprguard.model.Finding;
import dev.agentprguard.model.PolicyAction;
import dev.agentprguard.model.ScanResult;
import java.util.List;

/** Applies configured actions to findings and builds a scan result. */
public final class PolicyEngine {
  private final Config configuration;

  public PolicyEngine(Config configuration) {
    this.configuration = configuration == null ? new Config() : configuration;
  }

  public Finding apply(Finding finding) {
    if (finding == null || finding.getSeverity() == null) {
      throw new IllegalArgumentException("Finding and severity are required");
    }
    finding.withPolicyAction(configuration.getPolicy().getActionForSeverity(finding.getSeverity()));
    return finding;
  }

  public ScanResult evaluate(
      String repository, String baseBranch, String currentBranch,
      List<String> changedFiles, List<Finding> findings) {
    ScanResult result = new ScanResult()
        .withRepository(repository)
        .withBaseBranch(baseBranch)
        .withCurrentBranch(currentBranch);
    if (changedFiles != null) {
      changedFiles.forEach(result::addChangedFile);
    }
    if (findings != null) {
      findings.stream().map(this::apply).forEach(result::addFinding);
    }
    return result;
  }

  public boolean shouldFail(ScanResult result) {
    return result != null && result.hasFindingsWithAction(PolicyAction.BLOCK);
  }
}
