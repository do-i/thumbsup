package com.djd.fun.thumbsup.models;

import com.djd.fun.thumbsup.annotations.ThumbnailImageBoundSize;
import com.google.common.collect.ImmutableList;
import java.awt.Dimension;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Mutable size holder for thumbnail view port
 *
 * @see ThumbnailImageBoundSize
 */
public class ThumbViewSize {

  private static final int Y_DELTA = 28; // space for filename
  private static final ImmutableList<ImmutableSize> IMAGE_SIZES = ImmutableList.of(
      ImmutableSize.of(60, 55),
      ImmutableSize.of(90, 85),
      ImmutableSize.of(120, 115),
      ImmutableSize.of(150, 145),
      ImmutableSize.of(180, 175),
      ImmutableSize.of(202, 202));
  private static final ImmutableList<ImmutableSize> IMAGE_VIEW_SIZES = ImmutableList.of(
      ImmutableSize.of(60, 55 + Y_DELTA),
      ImmutableSize.of(90, 85 + Y_DELTA),
      ImmutableSize.of(120, 115 + Y_DELTA),
      ImmutableSize.of(150, 145 + Y_DELTA),
      ImmutableSize.of(180, 175 + Y_DELTA),
      ImmutableSize.of(210, 205 + Y_DELTA));
  private static final int MAX_SIZE_INDEX = IMAGE_SIZES.size() - 1;

  private final AtomicInteger currentViewSizeIndex = new AtomicInteger(MAX_SIZE_INDEX);

  public ImmutableSize getImageSize() {
    return IMAGE_SIZES.get(currentViewSizeIndex.get());
  }

  public ImmutableSize getImageViewSize() {
    return IMAGE_VIEW_SIZES.get(currentViewSizeIndex.get());
  }

  public void increment() {
    int current = currentViewSizeIndex.get();
    if (current < MAX_SIZE_INDEX) {
      this.currentViewSizeIndex.compareAndSet(current, current + 1);
    }
  }

  public void decrement() {
    int current = currentViewSizeIndex.get();
    if (current > 0) {
      this.currentViewSizeIndex.compareAndSet(current, current - 1);
    }
  }

  public Dimension getImageDimension() {
    ImmutableSize size = getImageSize();
    return new Dimension(size.getWidth(), size.getHeight());
  }

  /**
   * height + delta for filename space
   *
   * @return
   */
  public Dimension getImageViewDimension() {
    ImmutableSize size = getImageViewSize();
    return new Dimension(size.getWidth(), size.getHeight());
  }
}
