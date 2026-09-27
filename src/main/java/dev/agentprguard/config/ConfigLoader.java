package dev.agentprguard.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/** Loads configuration from YAML files. */
public final class ConfigLoader {
  private static final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
  private static final String DEFAULT_CONFIG_FILE = ".agent-pr-guard.yml";

  public static Config load(Path configPath) throws IOException {
    Path resolvedPath = configPath;
    if (configPath == null) {
      resolvedPath = Paths.get(DEFAULT_CONFIG_FILE);
    }

    if (Files.exists(resolvedPath)) {
      return yamlMapper.readValue(Files.readAllBytes(resolvedPath), Config.class);
    }

    // Return default config if file not found
    return new Config();
  }
}
