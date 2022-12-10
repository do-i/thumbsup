package com.djd.fun.thumbsup.util;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;
import com.google.common.io.MoreFiles;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoreMoreFiles {

  private static final Logger log = LoggerFactory.getLogger(MoreMoreFiles.class);
  @VisibleForTesting
  static final ImmutableList<String> IMAGE_EXT = ImmutableList.of("jpg", "png", "jpeg");

  private static final Predicate<Path> FOLDER = java.nio.file.Files::isDirectory;

  public static final Predicate<Path> FOLDER_OR_IMAGE = path ->
      FOLDER.test(path) || MoreMoreFiles.isImageType(path);

  private MoreMoreFiles() {
    // no instance
  }

  public static boolean isNotHidden(Path path) {
    try {
      return !java.nio.file.Files.isHidden(path);
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * case insensitive
   */
  @VisibleForTesting
  static boolean anyExtensionMatches(Path path, Collection<String> extensions) {
    return extensions.stream().anyMatch(MoreFiles.getFileExtension(path)::equalsIgnoreCase);
  }

  /**
   * Case insensitive match.
   *
   * @param path
   * @return {@code true} if the specified path is a folder. {@code false} otherwise
   */
  public static boolean isFolderType(Path path) {
    return FOLDER.test(path);
  }

  /**
   * Case insensitive match.
   *
   * @param path
   * @return {@code true} if the specified path has image extension. {@code false} otherwise
   */
  public static boolean isImageType(Path path) {
    return anyExtensionMatches(path, IMAGE_EXT);
  }

  /**
   * @param dir
   * @return number of image files in the given dir
   */
  public static int getNumberOfImageFiles(Path dir) {
    try {
      return (int) Files.list(dir).filter(MoreMoreFiles::isImageType).count();
    } catch (IOException e) {
      log.warn("Failed to list dir", e);
    }
    return 0;
  }

  /**
   * @param dir
   * @return folders under dir in natural order
   */
  public static ImmutableSortedSet<Path> getFolders(Path dir) {
    try {
      return Files.list(dir).filter(FOLDER)
          .collect(ImmutableSortedSet.toImmutableSortedSet(Ordering.natural()));
    } catch (IOException e) {
      log.warn("Failed to list dir", e);
    }
    return ImmutableSortedSet.of();
  }

  /**
   * @param path
   * @return image files peer to the given path
   */
  public static ImmutableSortedSet<Path> getPeerImagePaths(Path path) {
    try {
      return Files.list(path.getParent()).filter(MoreMoreFiles::isImageType)
          .collect(ImmutableSortedSet.toImmutableSortedSet(Ordering.natural()));
    } catch (IOException e) {
      log.warn("Failed to list dir", e);
    }
    return ImmutableSortedSet.of();
  }
}
