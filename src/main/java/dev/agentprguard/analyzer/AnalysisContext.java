package dev.agentprguard.analyzer;

import dev.agentprguard.config.Config;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

/** Immutable input supplied to analyzers. */
public record AnalysisContext(
    Path repositoryRoot,
    List<Path> changedFiles,
    Config configuration) {
  public AnalysisContext {
    if (repositoryRoot == null) {
      throw new IllegalArgumentException("repositoryRoot is required");
    }
    changedFiles = changedFiles == null ? List.of() : List.copyOf(changedFiles);
    configuration = configuration == null ? new Config() : configuration;
  }

  public List<Path> changedFiles() {
    return Collections.unmodifiableList(changedFiles);
  }
}
