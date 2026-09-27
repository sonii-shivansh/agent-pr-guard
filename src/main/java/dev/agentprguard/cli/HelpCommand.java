package dev.agentprguard.cli;

import java.util.concurrent.Callable;
import picocli.commandline.Command;

@Command(
    name = "help",
    description = "Show help"
)
public class HelpCommand implements Callable<Integer> {
  @Override
  public Integer call() {
    System.out.println("""Agent PR Guard - Security and risk gate for AI-generated pull requests

Usage: agent-pr-guard <command> [options]

Commands:
  scan                   Scan repository for risks
    -r, --repo <path>    Repository path (default: .)
    -c, --config <path>  Configuration file
    -f, --format <fmt>   Output format: terminal or json
    -b, --base <branch>  Base branch to compare

  init                   Initialize default configuration

  help                   Show this help message

Examples:
  agent-pr-guard scan
  agent-pr-guard scan --base main
  agent-pr-guard scan --format json
  agent-pr-guard init
""");
    return 0;
  }
}
