package com.djd.fun.thumbsup.models;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.builders.ThumbnailParameterBuilder;
import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.geometry.Region;
import net.coobird.thumbnailator.tasks.UnsupportedFormatException;
import net.coobird.thumbnailator.tasks.io.AbstractImageSource;
import net.coobird.thumbnailator.util.exif.ExifFilterUtils;
import net.coobird.thumbnailator.util.exif.ExifUtils;
import net.coobird.thumbnailator.util.exif.Orientation;

/**
 * This implementation was copied from {@link net.coobird.thumbnailator.tasks.io.InputStreamImageSource}
 * <p>
 * Main thing I changed was apply orientation filter before return.
 */
public class ByteInputStreamImageSource extends AbstractImageSource<InputStream> {

  private static final Logger log = LoggerFactory.getLogger(ByteInputStreamImageSource.class);
  /**
   * The index used to obtain the first image in an image file.
   */
  private static final int FIRST_IMAGE_INDEX = 0;
  private final InputStream is;

  @Inject
  public ByteInputStreamImageSource(@Assisted InputStream inputStream) {
    this.is = inputStream;
    setThumbnailParameter(newThumbnailParameter());
  }

  @Override
  public BufferedImage read() throws IOException {
    ImageInputStream iis = ImageIO.createImageInputStream(is);

    if (iis == null) {
      throw new IOException("Could not open InputStream.");
    }

    Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
    if (!readers.hasNext()) {
      throw new UnsupportedFormatException(
          UnsupportedFormatException.UNKNOWN,
          "No suitable ImageReader found for source data."
      );
    }

    ImageReader reader = readers.next();
    reader.setInput(iis);
    inputFormatName = reader.getFormatName();

    try {
      if (param.useExifOrientation() && "JPEG".equals(inputFormatName)) {
        Orientation orientation =
            ExifUtils.getExifOrientation(reader, FIRST_IMAGE_INDEX);

        // Skip this code block if there's no rotation needed.
        if (orientation != null && orientation != Orientation.TOP_LEFT) {
          List<ImageFilter> filters = param.getImageFilters();

          // EXIF orientation filter is added to the beginning, as
          // it should be performed early to prevent mis-orientation
          // in later filters.
          filters.add(0, ExifFilterUtils.getFilterForOrientation(orientation));
        }
      }
    } catch (Exception e) {
      // If something goes wrong, then skip the orientation-related
      // processing.
      // TODO Ought to have some way to track errors.
      log.warn("Failed to extract exifOrientation info");
    }
    ImageReadParam irParam = reader.getDefaultReadParam();
    int width = reader.getWidth(FIRST_IMAGE_INDEX);
    int height = reader.getHeight(FIRST_IMAGE_INDEX);

    if (param != null && param.getSourceRegion() != null) {
      Region region = param.getSourceRegion();
      Rectangle sourceRegion = region.calculate(width, height);
      irParam.setSourceRegion(sourceRegion);
    }

    BufferedImage img = reader.read(FIRST_IMAGE_INDEX, irParam);

		/*
     * Dispose the reader to free resources.
		 *
		 * This seems to be one of the culprits which was causing
		 * `OutOfMemoryError`s which began appearing frequently with
		 * Java 7 Update 21.
		 *
		 * Issue:
		 * http://code.google.com/p/thumbnailator/issues/detail?id=42
		 */
    reader.dispose();
    iis.close();
    return finishedReading(orient(img, param.getImageFilters()));
  }

  @Override
  public InputStream getSource() {
    return is;
  }

  private static BufferedImage orient(BufferedImage bufferedImage, List<ImageFilter> imageFilters) {
    if (imageFilters.isEmpty()) {
      return bufferedImage;
    }
    // TODO add support for more than one filter in the future.
    ImageFilter filter = imageFilters.get(0);
    if (filter == null) {
      throw new IllegalStateException("We have null filter.");
    }
    return filter.apply(bufferedImage);
  }

  private static ThumbnailParameter newThumbnailParameter() {
    return new ThumbnailParameterBuilder()
        .useExifOrientation(true) // TODO check if non jpg file case
        .scale(1.0)
        .build();
  }
}
