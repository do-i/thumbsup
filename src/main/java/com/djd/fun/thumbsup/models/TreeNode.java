package com.djd.fun.thumbsup.models;

import java.nio.file.Path;

import com.djd.fun.thumbsup.util.MoreMoreFiles;
import com.google.common.collect.ImmutableSortedSet;

/**
 * This immutable model class represents a node in the {@link javax.swing.JTree}
 */
public class TreeNode {

  private final Path dir;
  private final int imageCount;
  private final ImmutableSortedSet<Path> folders;

  public TreeNode(Path dir) {
    this.dir = dir;
    this.imageCount = MoreMoreFiles.getNumberOfImageFiles(dir);
    this.folders = MoreMoreFiles.getFolders(dir);
  }

  public Path getDir() {
    return dir;
  }

  public int getImageCount() {
    return imageCount;
  }

  public ImmutableSortedSet<Path> getFolders() {
    return folders;
  }

  private String displayName() {
    return String.format("%s (%d)", dir.getFileName().toString(), imageCount);
  }

  @Override
  public String toString() {
    return displayName();
  }
}
