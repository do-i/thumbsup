package com.djd.fun.thumbsup.models;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

import com.djd.fun.thumbsup.annotations.ThumbnailCacheDir;
import com.djd.fun.thumbsup.util.SizeFormatter;
import com.google.common.base.MoreObjects;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssetImpl implements Asset, Comparable<Asset> {

  private static final Logger log = LoggerFactory.getLogger(AssetImpl.class);
  private final File cacheDir;
  private final Path path;

  @Inject
  public AssetImpl(@ThumbnailCacheDir File cacheDir, @Assisted Path path) {
    this.path = path;
    this.cacheDir = cacheDir;
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
    if (getFileType() != FileType.IMAGE) {
      throw new UnsupportedOperationException("Not supported for this file type");
    }
    return Paths.get(cacheDir.toString(), path.toString());
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
    if (this == o) return true;
    if (!(o instanceof AssetImpl)) return false;
    AssetImpl asset = (AssetImpl)o;
    return Objects.equals(path, asset.path) &&
        Objects.equals(cacheDir, asset.cacheDir);
  }

  @Override
  public int hashCode() {
    return Objects.hash(path, cacheDir);
  }
}
