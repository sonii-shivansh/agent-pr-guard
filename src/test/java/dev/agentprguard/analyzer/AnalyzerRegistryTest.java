package dev.agentprguard.analyzer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class AnalyzerRegistryTest {
  @Test
  void registersAndRunsAnalyzersInOrder() {
    Analyzer analyzer = new Analyzer() {
      public String id() { return "test"; }
      public String description() { return "test analyzer"; }
      public List<dev.agentprguard.model.Finding> analyze(AnalysisContext context) { return List.of(); }
    };

    AnalyzerRegistry registry = new AnalyzerRegistry().register(analyzer);

    assertThat(registry.all()).containsExactly(analyzer);
    assertThat(registry.analyze(new AnalysisContext(Path.of("."), List.of(), null))).isEmpty();
  }

  @Test
  void rejectsDuplicateIds() {
    Analyzer analyzer = new Analyzer() {
      public String id() { return "duplicate"; }
      public String description() { return "test"; }
      public List<dev.agentprguard.model.Finding> analyze(AnalysisContext context) { return List.of(); }
    };
    AnalyzerRegistry registry = new AnalyzerRegistry().register(analyzer);

    assertThatThrownBy(() -> registry.register(analyzer))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("already registered");
  }
}
