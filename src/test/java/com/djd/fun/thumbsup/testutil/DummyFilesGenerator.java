package com.djd.fun.thumbsup.testutil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DummyFilesGenerator {

  private static final String TMP_DIR_ROOT_PREFIX = "DummyFilesGenerator";
  private final int depth;
  private final int numOfFilePerDir;
  private final String dirPrefix;
  private final String filePrefix;
  private final String fileSuffix;
  private final Path tmpDirRootPath;

  public DummyFilesGenerator(Builder builder) throws IOException {
    this.depth = builder.depth;
    this.numOfFilePerDir = builder.numOfFilePerDir;
    this.dirPrefix = builder.dirPrefix;
    this.filePrefix = builder.filePrefix;
    this.fileSuffix = builder.fileSuffix;
    this.tmpDirRootPath = Files.createTempDirectory(TMP_DIR_ROOT_PREFIX);
  }

  public Path generate() throws IOException {
    Path currDir = tmpDirRootPath;
    for (int dirCount = 0; dirCount < depth; dirCount++) {
      currDir = Files.createDirectory(Paths.get(currDir.toString(), dirPrefix + dirCount));
      for (int fileCount = 0; fileCount < numOfFilePerDir; fileCount++) {
        Files.createFile(Paths.get(currDir.toString(), filePrefix + fileCount + fileSuffix));
      }
    }
    return tmpDirRootPath;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private int depth = 2;
    private int numOfFilePerDir = 3;
    private String dirPrefix = "D-";
    private String filePrefix = "F-";
    private String fileSuffix = "";

    public Builder depth(int depth) {
      if (depth < 1) {
        throw new IllegalArgumentException("depth must be at least 1");
      }
      this.depth = depth;
      return this;
    }

    public Builder dirPrefix(String dirPrefix) {
      this.dirPrefix = dirPrefix;
      return this;
    }

    public Builder filePrefix(String filePrefix) {
      this.filePrefix = filePrefix;
      return this;
    }

    public Builder fileSuffix(String fileSuffix) {
      this.fileSuffix = fileSuffix;
      return this;
    }

    public Builder numOfFilePerDir(int numOfFilePerDir) {
      this.numOfFilePerDir = numOfFilePerDir;
      return this;
    }

    public DummyFilesGenerator build() throws IOException {
      return new DummyFilesGenerator(this);
    }
  }
}
