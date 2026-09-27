package dev.agentprguard.git;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

/** Read-only Git operations used by the scanner. */
public final class GitRepository implements AutoCloseable {
  private final Repository repository;
  private final Git git;

  private GitRepository(Repository repository) {
    this.repository = repository;
    this.git = new Git(repository);
  }

  public static GitRepository open(Path start) throws IOException {
    Repository repository = new FileRepositoryBuilder()
        .findGitDir(start.toFile())
        .build();
    if (repository.getDirectory() == null) {
      repository.close();
      throw new IOException("Not inside a Git repository: " + start);
    }
    return new GitRepository(repository);
  }

  public Path root() {
    return repository.getWorkTree().toPath();
  }

  public String branch() {
    try {
      return repository.getBranch();
    } catch (IOException exception) {
      return "unknown";
    }
  }

  public List<Path> trackedFiles() throws GitAPIException {
    List<Path> files = new ArrayList<>();
    for (var entry : git.lsFiles().call()) {
      Path path = root().resolve(entry.getPathString());
      if (Files.isRegularFile(path)) {
        files.add(path);
      }
    }
    return files;
  }

  @Override
  public void close() {
    git.close();
    repository.close();
  }
}
