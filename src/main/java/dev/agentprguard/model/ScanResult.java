package dev.agentprguard.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Result of a scan operation.
 *
 * <p>Contains all findings and metadata about the scan.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScanResult {
  private String repository;
  private String baseBranch;
  private String currentBranch;
  private List<String> changedFiles;
  private List<Finding> findings;
  private Instant scanTime;
  private String scanId;

  public ScanResult() {
    this.findings = new ArrayList<>();
    this.changedFiles = new ArrayList<>();
    this.scanId = java.util.UUID.randomUUID().toString();
    this.scanTime = Instant.now();
  }

  public String getRepository() {
    return repository;
  }

  public ScanResult withRepository(String repository) {
    this.repository = repository;
    return this;
  }

  public String getBaseBranch() {
    return baseBranch;
  }

  public ScanResult withBaseBranch(String baseBranch) {
    this.baseBranch = baseBranch;
    return this;
  }

  public String getCurrentBranch() {
    return currentBranch;
  }

  public ScanResult withCurrentBranch(String currentBranch) {
    this.currentBranch = currentBranch;
    return this;
  }

  public List<String> getChangedFiles() {
    return Collections.unmodifiableList(changedFiles);
  }

  public ScanResult addChangedFile(String filePath) {
    this.changedFiles.add(filePath);
    return this;
  }

  public List<Finding> getFindings() {
    return Collections.unmodifiableList(findings);
  }

  public ScanResult addFinding(Finding finding) {
    this.findings.add(finding);
    return this;
  }

  public List<Finding> findingsBySeverity(Severity severity) {
    return findings.stream()
        .filter(f -> f.getSeverity() == severity)
        .collect(Collectors.toList());
  }

  public int countBySeverity(Severity severity) {
    return (int) findings.stream().filter(f -> f.getSeverity() == severity).count();
  }

  public List<Finding> findingsByCategory(Category category) {
    return findings.stream()
        .filter(f -> f.getCategory() == category)
        .collect(Collectors.toList());
  }

  public boolean hasFindingsWithAction(PolicyAction action) {
    return findings.stream().anyMatch(f -> f.getPolicyAction() == action);
  }

  public List<Finding> findingsByAction(PolicyAction action) {
    return findings.stream()
        .filter(f -> f.getPolicyAction() == action)
        .collect(Collectors.toList());
  }

  public Instant getScanTime() {
    return scanTime;
  }

  public String getScanId() {
    return scanId;
  }

  public boolean isPassed() {
    return !hasFindingsWithAction(PolicyAction.BLOCK);
  }

  @Override
  public String toString() {
    return "ScanResult{" +
        "repository='" + repository + '\'' +
        ", baseBranch='" + baseBranch + '\'' +
        ", currentBranch='" + currentBranch + '\'' +
        ", findings=" + findings.size() +
        ", scanId='" + scanId + '\'' +
        '}';
  }
}
