package dev.agentprguard.analyzer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Ordered registry of enabled analyzers. */
public final class AnalyzerRegistry {
  private final Map<String, Analyzer> analyzers = new LinkedHashMap<>();

  public AnalyzerRegistry register(Analyzer analyzer) {
    if (analyzer == null || analyzer.id() == null || analyzer.id().isBlank()) {
      throw new IllegalArgumentException("Analyzer and analyzer id are required");
    }
    if (analyzers.putIfAbsent(analyzer.id(), analyzer) != null) {
      throw new IllegalArgumentException("Analyzer already registered: " + analyzer.id());
    }
    return this;
  }

  public AnalyzerRegistry registerAll(Collection<? extends Analyzer> analyzers) {
    analyzers.forEach(this::register);
    return this;
  }

  public Analyzer get(String id) {
    return analyzers.get(id);
  }

  public List<Analyzer> all() {
    return List.copyOf(analyzers.values());
  }

  public List<dev.agentprguard.model.Finding> analyze(AnalysisContext context) {
    List<dev.agentprguard.model.Finding> findings = new ArrayList<>();
    for (Analyzer analyzer : analyzers.values()) {
      findings.addAll(analyzer.analyze(context));
    }
    return findings;
  }
}
