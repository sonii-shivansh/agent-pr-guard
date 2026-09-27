package dev.agentprguard.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Objects;
import java.util.UUID;

/**
 * A single finding or risk identified by an analyzer.
 *
 * <p>Each finding provides actionable details about a security or architecture risk.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Finding {
  private String id;
  private Severity severity;
  private Category category;
  private String title;
  private String description;
  private String evidence;
  private String filePath;
  private Integer lineNumber;
  private Double confidence;
  private String remediation;
  private String analyzerId;

  // For policy engine
  private PolicyAction policyAction;

  public Finding() {}

  public Finding(Severity severity, Category category, String title, String description) {
    this.id = UUID.randomUUID().toString();
    this.severity = severity;
    this.category = category;
    this.title = title;
    this.description = description;
    this.confidence = 1.0;
    this.policyAction = PolicyAction.WARN;
  }

  public String getId() {
    return id;
  }

  public Finding withId(String id) {
    this.id = id;
    return this;
  }

  public Severity getSeverity() {
    return severity;
  }

  public Finding withSeverity(Severity severity) {
    this.severity = severity;
    return this;
  }

  public Category getCategory() {
    return category;
  }

  public Finding withCategory(Category category) {
    this.category = category;
    return this;
  }

  public String getTitle() {
    return title;
  }

  public Finding withTitle(String title) {
    this.title = title;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public Finding withDescription(String description) {
    this.description = description;
    return this;
  }

  public String getEvidence() {
    return evidence;
  }

  public Finding withEvidence(String evidence) {
    this.evidence = evidence;
    return this;
  }

  public String getFilePath() {
    return filePath;
  }

  public Finding withFilePath(String filePath) {
    this.filePath = filePath;
    return this;
  }

  public Integer getLineNumber() {
    return lineNumber;
  }

  public Finding withLineNumber(Integer lineNumber) {
    this.lineNumber = lineNumber;
    return this;
  }

  public Double getConfidence() {
    return confidence;
  }

  public Finding withConfidence(Double confidence) {
    this.confidence = confidence;
    return this;
  }

  public String getRemediation() {
    return remediation;
  }

  public Finding withRemediation(String remediation) {
    this.remediation = remediation;
    return this;
  }

  public String getAnalyzerId() {
    return analyzerId;
  }

  public Finding withAnalyzerId(String analyzerId) {
    this.analyzerId = analyzerId;
    return this;
  }

  public PolicyAction getPolicyAction() {
    return policyAction;
  }

  public Finding withPolicyAction(PolicyAction policyAction) {
    this.policyAction = policyAction;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Finding finding = (Finding) o;
    return Objects.equals(id, finding.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Finding{" +
        "id='" + id + '\'' +
        ", severity=" + severity +
        ", category=" + category +
        ", title='" + title + '\'' +
        ", filePath='" + filePath + '\'' +
        ", lineNumber=" + lineNumber +
        ", policyAction=" + policyAction +
        '}';
  }
}
