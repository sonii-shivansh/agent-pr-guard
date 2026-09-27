package dev.agentprguard.report;

import dev.agentprguard.model.Finding;
import dev.agentprguard.model.ScanResult;
import dev.agentprguard.model.Severity;
import java.util.Map;
import java.util.stream.Collectors;

/** Terminal output formatter for scan results. */
public final class TerminalReporter {
  private static final String BORDER = "─────────────────────────────────────────────";
  private static final String BOX_TOP = "╔════════════════════════════════════════════╗";
  private static final String BOX_BOTTOM = "╚════════════════════════════════════════════╝";
  private static final String BOX_SIDE = "║";

  public String report(ScanResult result) {
    if (result == null) {
      return "No scan result available.";
    }

    StringBuilder output = new StringBuilder();
    output.append(BOX_TOP).append("\n");
    output.append(formatCenter("AGENT PR GUARD")).append("\n");
    output.append(BOX_BOTTOM).append("\n\n");

    if (result.getRepository() != null) {
      output.append("Repository:  ").append(result.getRepository()).append("\n");
    }

    if (result.getBaseBranch() != null) {
      output.append("Base:        ").append(result.getBaseBranch()).append("\n");
    }

    if (result.getCurrentBranch() != null) {
      output.append("Current:     ").append(result.getCurrentBranch()).append("\n");
    }

    output.append("Changed files: ").append(result.getChangedFiles().size()).append("\n\n");

    // Summary
    output.append("Findings:\n");
    for (Severity severity : Severity.values()) {
      int count = result.countBySeverity(severity);
      if (count > 0) {
        output.append("  ").append(String.format("%2d", count)).append(" ").append(severity.getDisplayName().toUpperCase()).append("\n");
      }
    }

    output.append("\n").append(BORDER).append("\n\n");

    // Findings by severity
    for (Severity severity : new Severity[]{Severity.CRITICAL, Severity.HIGH, Severity.MEDIUM, Severity.LOW, Severity.INFO}) {
      var findingsBySeverity = result.findingsBySeverity(severity);
      if (!findingsBySeverity.isEmpty()) {
        for (Finding finding : findingsBySeverity) {
          output.append(formatFinding(finding)).append("\n\n").append(BORDER).append("\n\n");
        }
      }
    }

    output.append("RESULT: ").append(result.isPassed() ? "PASS" : "BLOCK").append("\n");

    long blockCount = result.findingsByAction(dev.agentprguard.model.PolicyAction.BLOCK).size();
    if (blockCount > 0) {
      output.append(blockCount).append(" finding").append(blockCount == 1 ? "" : "s").append(" require").append(blockCount == 1 ? "s" : "").append(" review.\n");
    }

    return output.toString();
  }

  private String formatCenter(String text) {
    int padding = (44 - text.length()) / 2;
    String left = " ".repeat(Math.max(0, padding));
    String right = " ".repeat(Math.max(0, 44 - text.length() - padding));
    return BOX_SIDE + left + text + right + BOX_SIDE;
  }

  private String formatFinding(Finding finding) {
    StringBuilder sb = new StringBuilder();
    sb.append(finding.getSeverity().getDisplayName().toUpperCase())
        .append("  ")
        .append(finding.getCategory().getDisplayName());
    sb.append("\n\n");
    sb.append(finding.getTitle()).append("\n\n");

    if (finding.getDescription() != null) {
      sb.append(finding.getDescription()).append("\n\n");
    }

    if (finding.getFilePath() != null) {
      sb.append("File:  ").append(finding.getFilePath());
      if (finding.getLineNumber() != null) {
        sb.append(":").append(finding.getLineNumber());
      }
      sb.append("\n\n");
    }

    if (finding.getEvidence() != null) {
      sb.append("Evidence:\n").append(finding.getEvidence()).append("\n\n");
    }

    if (finding.getRemediation() != null) {
      sb.append("Remediation:\n").append(finding.getRemediation()).append("\n");
    }

    return sb.toString();
  }
}
