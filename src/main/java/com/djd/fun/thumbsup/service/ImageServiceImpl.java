package com.djd.fun.thumbsup.service;

import com.djd.fun.thumbsup.annotations.ImageDefault;
import com.djd.fun.thumbsup.annotations.ImageFolder;
import com.djd.fun.thumbsup.annotations.ThumbnailImageBoundSize;
import com.djd.fun.thumbsup.models.Asset;
import com.djd.fun.thumbsup.models.ImmutableSize;
import com.google.common.base.Stopwatch;
import com.google.inject.Inject;
import java.awt.Image;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageServiceImpl implements ImageService {

  private static final Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);
  private final Image folderImage;
  private final Image defaultImage;
  private final ImmutableSize thumbSize;

  @Inject
  public ImageServiceImpl(
      @ImageFolder Image folderImage,
      @ImageDefault Image image,
      @ThumbnailImageBoundSize ImmutableSize thumbSize) {
    this.folderImage = folderImage;
    this.defaultImage = image;
    this.thumbSize = thumbSize;
  }

  @Override
  public Image createThumbnailImage(Asset asset) {
    if (asset.getFileType() == Asset.FileType.FOLDER) {
      return folderImage;
    }
    if (asset.getFileType() != Asset.FileType.IMAGE) {
      return defaultImage;
    }
    Path thumb = asset.getThumbnailPath();
    log.info("thumb: ", thumb);
    if (Files.exists(thumb)) {
      log.info("Speed Up by reusing existing thumbnail.");
    } else {
      Stopwatch stopwatch = Stopwatch.createStarted();
      try {
        Files.createDirectories(thumb.getParent());
        Thumbnails.of(asset.getFile())
            .size(thumbSize.getWidth(), thumbSize.getHeight())
            .toFile(thumb.toFile());
      } catch (IOException e) {
        log.warn("Failed to save thumbnail", e);
      }
      log.info("Thumbnail generation took {}ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }
    try {
      return ImageIO.read(asset.getThumbnailPath().toFile());
    } catch (IOException e) {
      log.warn("Failed to load thumbnail", e);
      return defaultImage;
    }
  }
}