package com.djd.fun.thumbsup.testutil;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import com.google.common.io.Resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoreMoreResources {

  private static final Logger log = LoggerFactory.getLogger(MoreMoreResources.class);

  private MoreMoreResources() {
    // no instance
  }

  public static Path getPathFromResouces(String resourceName) {
    try {
      return Paths.get(Resources.getResource(resourceName).toURI());
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  // https://stackoverflow.com/questions/35988192/java-nio-most-concise-recursive-directory-delete
  public static void deleteRecursively(Path dir) throws IOException {
    Files.walk(dir, FileVisitOption.FOLLOW_LINKS)
        .sorted(Comparator.reverseOrder())
        .map(Path::toFile)
        .peek(file -> log.info("Delete: {}", file))
        .forEach(File::delete);
  }
}
