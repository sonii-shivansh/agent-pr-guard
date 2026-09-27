package dev.agentprguard.analyzer;

import dev.agentprguard.model.Finding;
import java.util.List;

/** A deterministic analyzer that inspects one analysis context. */
public interface Analyzer {
  String id();

  String description();

  List<Finding> analyze(AnalysisContext context);
}
