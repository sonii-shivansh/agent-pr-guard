package dev.agentprguard.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dev.agentprguard.model.ScanResult;
import java.io.IOException;

/** JSON output formatter for scan results. */
public final class JsonReporter {
  private static final ObjectMapper mapper = new ObjectMapper();

  static {
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
  }

  public String report(ScanResult result) throws IOException {
    return mapper.writeValueAsString(result);
  }
}
