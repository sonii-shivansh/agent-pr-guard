package dev.agentprguard.cli;

import dev.agentprguard.analyzer.AnalysisContext;
import dev.agentprguard.analyzer.AnalyzerRegistry;
import dev.agentprguard.analyzer.impl.SecretDetectionAnalyzer;
import dev.agentprguard.config.Config;
import dev.agentprguard.config.ConfigLoader;
import dev.agentprguard.git.GitRepository;
import dev.agentprguard.model.Finding;
import dev.agentprguard.policy.PolicyEngine;
import dev.agentprguard.report.JsonReporter;
import dev.agentprguard.report.TerminalReporter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Callable;
import picocli.commandline.Command;
import picocli.commandline.Option;

@Command(
    name = "scan",
    description = "Scan repository for risks"
)
public class ScanCommand implements Callable<Integer> {
  @Option(
      names = {"-r", "--repo"},
      description = "Repository path (default: current directory)",
      defaultValue = "."
  )
  private String repository;

  @Option(
      names = {"-c", "--config"},
      description = "Configuration file path"
  )
  private String configFile;

  @Option(
      names = {"-f", "--format"},
      description = "Output format: terminal or json (default: terminal)",
      defaultValue = "terminal"
  )
  private String format;

  @Option(
      names = {"-b", "--base"},
      description = "Base branch to compare against"
  )
  private String baseBranch;

  @Override
  public Integer call() {
    try {
      Path repoPath = Paths.get(repository).toAbsolutePath();
      Config config = ConfigLoader.load(configFile != null ? Paths.get(configFile) : null);

      try (GitRepository gitRepo = GitRepository.open(repoPath)) {
        List<Path> changedFiles = gitRepo.trackedFiles();
        String currentBranch = gitRepo.branch();
        String repoName = repoPath.getFileName().toString();

        AnalysisContext context = new AnalysisContext(repoPath, changedFiles, config);
        AnalyzerRegistry registry = new AnalyzerRegistry()
            .register(new SecretDetectionAnalyzer());

        List<Finding> findings = registry.analyze(context);

        PolicyEngine policyEngine = new PolicyEngine(config);
        var result = policyEngine.evaluate(
            repoName,
            baseBranch != null ? baseBranch : "main",
            currentBranch,
            changedFiles.stream().map(Path::toString).toList(),
            findings
        );

        String output = format.equalsIgnoreCase("json")
            ? new JsonReporter().report(result)
            : new TerminalReporter().report(result);

        System.out.println(output);
        return result.isPassed() ? 0 : 1;
      }
    } catch (IOException exception) {
      System.err.println("Error: " + exception.getMessage());
      return 1;
    }
  }
}
