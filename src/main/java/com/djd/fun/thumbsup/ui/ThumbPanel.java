package com.djd.fun.thumbsup.ui;

import com.djd.fun.thumbsup.models.Asset;
import com.djd.fun.thumbsup.models.ImmutableSize;
import com.djd.fun.thumbsup.models.ThumbViewSize;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.annotation.Nullable;
import javax.swing.JPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThumbPanel extends JPanel {

  private static final Logger log = LoggerFactory.getLogger(ThumbPanel.class);
  private final ThumbViewSize thumbViewSize;
  private @Nullable Image image;

  @Inject
  public ThumbPanel(ThumbViewSize thumbViewSize, @Assisted Asset asset) {
    this.thumbViewSize = thumbViewSize;
    log.info("init");
  }

  @Override
  public Dimension getPreferredSize() {
    return thumbViewSize.getImageDimension();
  }

  /**
   * This method initialize the image for display.
   *
   * @param image
   */
  public void setImage(Image image) {
    this.image = image;
    repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    if (image != null) {
      super.paintComponent(g);
      int x = findCenterAlignX();
      int y = findBottomAlignY();
      g.drawImage(image, x, y, this);
      g.dispose();
    } else {
      log.debug("Skip expensive paint job");
    }
  }

  /**
   * @return x value so that an image is aligned center
   */
  private int findCenterAlignX() {
    ImmutableSize viewSize = thumbViewSize.getImageSize();
    int imageWidth = image.getWidth(null);
    return imageWidth < viewSize.getWidth()
        ? (viewSize.getWidth() - imageWidth) / 2
        : 0;
  }

  /**
   * @return y value so that an image is aligned bottom
   */
  private int findBottomAlignY() {
    ImmutableSize viewSize = thumbViewSize.getImageSize();
    int imageHeight = image.getHeight(null);
    return imageHeight < viewSize.getHeight()
        ? viewSize.getHeight() - imageHeight
        : 0;
  }
}
