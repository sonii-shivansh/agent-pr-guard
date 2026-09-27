package dev.agentprguard.analyzer.impl;

import dev.agentprguard.analyzer.Analyzer;
import dev.agentprguard.analyzer.AnalysisContext;
import dev.agentprguard.model.Category;
import dev.agentprguard.model.Finding;
import dev.agentprguard.model.Severity;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Detects potential secrets in changed files.
 *
 * <p>Looks for patterns matching:
 * - AWS keys
 * - GitHub tokens
 * - Private keys
 * - API keys
 * - Database credentials
 * - JWT secrets
 */
public final class SecretDetectionAnalyzer implements Analyzer {
  private static final String ID = "secrets";
  private static final String DESCRIPTION = "Detects potential secrets and credentials";

  // Pattern for AWS Access Key ID
  private static final Pattern AWS_ACCESS_KEY =
      Pattern.compile("AKIA[0-9A-Z]{16}", Pattern.CASE_INSENSITIVE);

  // Pattern for AWS Secret Access Key (generic)
  private static final Pattern AWS_SECRET_KEY =
      Pattern.compile("aws[_-]?secret[_-]?access[_-]?key\\s*[=:]", Pattern.CASE_INSENSITIVE);

  // Pattern for GitHub tokens
  private static final Pattern GITHUB_TOKEN =
      Pattern.compile("(ghp_|ghu_|ghs_|ghr_)[A-Za-z0-9_]{36,255}", Pattern.CASE_INSENSITIVE);

  // Pattern for JWT tokens
  private static final Pattern JWT_TOKEN =
      Pattern.compile("(jwt|token|secret)\\s*[=:]", Pattern.CASE_INSENSITIVE);

  // Pattern for private keys
  private static final Pattern PRIVATE_KEY =
      Pattern.compile("-----BEGIN (RSA |DSA |EC )?PRIVATE KEY-----");

  // Pattern for database connection strings
  private static final Pattern DB_CONNECTION =
      Pattern.compile(
          "(password|passwd|pwd)\\s*[=:]",
          Pattern.CASE_INSENSITIVE);

  // Pattern for API keys
  private static final Pattern API_KEY =
      Pattern.compile("(api[_-]?key|apikey)\\s*[=:]", Pattern.CASE_INSENSITIVE);

  @Override
  public String id() {
    return ID;
  }

  @Override
  public String description() {
    return DESCRIPTION;
  }

  @Override
  public List<Finding> analyze(AnalysisContext context) {
    List<Finding> findings = new ArrayList<>();

    if (!context.configuration().isRuleEnabled(ID)) {
      return findings;
    }

    for (Path changedFile : context.changedFiles()) {
      if (shouldSkipFile(changedFile)) {
        continue;
      }

      try {
        List<String> lines = Files.readAllLines(changedFile);
        for (int lineNumber = 0; lineNumber < lines.size(); lineNumber++) {
          String line = lines.get(lineNumber);
          findings.addAll(scanLineForSecrets(changedFile, lineNumber + 1, line));
        }
      } catch (IOException ignored) {
        // Skip files that cannot be read
      }
    }

    return findings;
  }

  private List<Finding> scanLineForSecrets(Path file, int lineNumber, String line) {
    List<Finding> findings = new ArrayList<>();

    if (AWS_ACCESS_KEY.matcher(line).find()) {
      findings.add(
          new Finding(Severity.CRITICAL, Category.SECRETS, "AWS Access Key detected",
              "Potential AWS Access Key found in source code")
              .withFilePath(file.toString())
              .withLineNumber(lineNumber)
              .withAnalyzerId(ID)
              .withEvidence(maskSecret(line))
              .withRemediation("Remove the key and rotate it in AWS Console")
              .withConfidence(0.95));
    }

    if (AWS_SECRET_KEY.matcher(line).find()) {
      findings.add(
          new Finding(Severity.CRITICAL, Category.SECRETS, "AWS Secret Access Key pattern",
              "Potential AWS secret key assignment found")
              .withFilePath(file.toString())
              .withLineNumber(lineNumber)
              .withAnalyzerId(ID)
              .withEvidence(maskSecret(line))
              .withRemediation("Remove the key and rotate it in AWS Console")
              .withConfidence(0.85));
    }

    if (GITHUB_TOKEN.matcher(line).find()) {
      findings.add(
          new Finding(Severity.CRITICAL, Category.SECRETS, "GitHub token detected",
              "Potential GitHub Personal Access Token found in source code")
              .withFilePath(file.toString())
              .withLineNumber(lineNumber)
              .withAnalyzerId(ID)
              .withEvidence(maskSecret(line))
              .withRemediation("Remove the token and revoke it in GitHub Settings")
              .withConfidence(0.95));
    }

    if (PRIVATE_KEY.matcher(line).find()) {
      findings.add(
          new Finding(Severity.CRITICAL, Category.SECRETS, "Private key detected",
              "RSA/DSA/EC private key found in source code")
              .withFilePath(file.toString())
              .withLineNumber(lineNumber)
              .withAnalyzerId(ID)
              .withEvidence("Private key header")
              .withRemediation("Remove the private key and rotate it immediately")
              .withConfidence(0.99));
    }

    if (JWT_TOKEN.matcher(line).find()) {
      findings.add(
          new Finding(Severity.HIGH, Category.SECRETS, "JWT secret assignment",
              "Potential JWT secret or token assignment found")
              .withFilePath(file.toString())
              .withLineNumber(lineNumber)
              .withAnalyzerId(ID)
              .withEvidence(maskSecret(line))
              .withRemediation("Use environment variables or secure vaults for secrets")
              .withConfidence(0.75));
    }

    if (API_KEY.matcher(line).find()) {
      findings.add(
          new Finding(Severity.HIGH, Category.SECRETS, "API key assignment",
              "Potential API key or credential assignment found")
              .withFilePath(file.toString())
              .withLineNumber(lineNumber)
              .withAnalyzerId(ID)
              .withEvidence(maskSecret(line))
              .withRemediation("Use environment variables or secure vaults for API keys")
              .withConfidence(0.70));
    }

    if (DB_CONNECTION.matcher(line).find()) {
      findings.add(
          new Finding(Severity.HIGH, Category.SECRETS, "Database password assignment",
              "Potential database password assignment found")
              .withFilePath(file.toString())
              .withLineNumber(lineNumber)
              .withAnalyzerId(ID)
              .withEvidence(maskSecret(line))
              .withRemediation("Use environment variables or secrets management for database credentials")
              .withConfidence(0.70));
    }

    return findings;
  }

  private boolean shouldSkipFile(Path file) {
    String filename = file.getFileName().toString().toLowerCase();
    // Skip common non-code files
    return filename.endsWith(".jar")
        || filename.endsWith(".zip")
        || filename.endsWith(".png")
        || filename.endsWith(".jpg")
        || filename.endsWith(".gif")
        || filename.endsWith(".pdf")
        || filename.endsWith(".bin")
        || filename.startsWith(".");
  }

  private String maskSecret(String line) {
    // Mask the value part of assignments
    if (line.contains("=")) {
      int eqIndex = line.indexOf("=");
      String key = line.substring(0, eqIndex).trim();
      return key + " = ***MASKED***";
    }
    if (line.contains(":")) {
      int colIndex = line.indexOf(":");
      String key = line.substring(0, colIndex).trim();
      return key + ": ***MASKED***";
    }
    return "***MASKED***";
  }
}
