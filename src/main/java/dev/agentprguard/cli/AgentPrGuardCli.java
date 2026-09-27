package dev.agentprguard.cli;

import picocli.commandline.Command;
import picocli.commandline.CommandLine;
import picocli.commandline.ExitCode;

@Command(
    name = "agent-pr-guard",
    description = "Security and risk gate for AI-generated pull requests",
    subcommands = {ScanCommand.class, InitCommand.class, HelpCommand.class},
    version = "0.1.0",
    mixinStandardHelpOptions = true
)
public class AgentPrGuardCli {
  public static void main(String[] args) {
    System.exit(new CommandLine(new AgentPrGuardCli()).execute(args));
  }
}
