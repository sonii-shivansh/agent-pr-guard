package dev.agentprguard.cli;

import java.util.concurrent.Callable;
import picocli.commandline.Command;

@Command(
    name = "init",
    description = "Initialize configuration file"
)
public class InitCommand implements Callable<Integer> {
  @Override
  public Integer call() {
    String defaultConfig =
        """version: 1

policy:
  critical:
    action: block
  high:
    action: review
  medium:
    action: warn
  low:
    action: warn
  info:
    action: ignore

rules:
  secrets: true
  security-config: true
  dependency: true
  api: true
  database: true
  network: true
  architecture: true
  testing: true

protected:
  paths:
    - src/main/java/**/security/**
    - db/migration/**
""";

    try {
      java.nio.file.Files.write(
          java.nio.file.Paths.get(".agent-pr-guard.yml"),
          defaultConfig.getBytes()
      );
      System.out.println("✓ Created .agent-pr-guard.yml");
      return 0;
    } catch (java.io.IOException e) {
      System.err.println("Error creating config: " + e.getMessage());
      return 1;
    }
  }
}
