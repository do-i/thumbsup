package com.djd.fun.thumbsup.models;

import com.djd.fun.thumbsup.annotations.ThumbnailCacheDir;
import com.djd.fun.thumbsup.util.SizeFormatter;
import com.google.common.base.MoreObjects;
import com.google.common.io.MoreFiles;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import javax.annotation.Nullable;
import org.apache.commons.codec.digest.DigestUtils;

public class AssetImpl implements Asset, Comparable<Asset> {

  private final File cacheDir;
  private final Path path;
  private final @Nullable Path thumbPath;

  @Inject
  public AssetImpl(@ThumbnailCacheDir File cacheDir, @Assisted Path path) {
    this.path = path;
    this.cacheDir = cacheDir;
    this.thumbPath = getFileType() == FileType.IMAGE
        ? Paths.get(cacheDir.toString(),
        DigestUtils.sha1Hex(path.toString()) + "." + MoreFiles.getFileExtension(path))
        : null;
  }

  @Override
  public Path getFilePath() {
    return path;
  }

  @Override
  public FileType getFileType() {
    return FileType.findType(path);
  }

  @Override
  public File getFile() {
    return path.toFile();
  }

  @Override
  public Path getThumbnailPath() {
    if (thumbPath == null) {
      throw new UnsupportedOperationException("Not supported for this file type");
    }
    return thumbPath;
  }

  @Override
  public long getFileByteSize() {
    return path.toFile().length();
  }

  @Override
  public String getReadableFileSize() {
    return SizeFormatter.humanReadableByteCount(getFileByteSize());
  }

  @Override
  public int compareTo(Asset other) {
    int typeComp = getFileType().compareTo(other.getFileType());
    return typeComp != 0 ? typeComp : getFile().compareTo(other.getFile());
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("cacheDir", cacheDir)
        .add("path", path)
        .add("size", getReadableFileSize())
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AssetImpl asset)) {
      return false;
    }
    return Objects.equals(path, asset.path) &&
        Objects.equals(cacheDir, asset.cacheDir);
  }

  @Override
  public int hashCode() {
    return Objects.hash(path, cacheDir);
  }
}
