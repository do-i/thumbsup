package com.djd.fun.thumbsup.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;
import javax.swing.JPanel;

import com.djd.fun.thumbsup.annotations.Experiment;
import com.djd.fun.thumbsup.events.BackToThumbsViewEvent;
import com.djd.fun.thumbsup.events.FocusBigImageViewEvent;
import com.djd.fun.thumbsup.models.Asset;
import com.djd.fun.thumbsup.models.ByteInputStreamImageSourceFactory;
import com.djd.fun.thumbsup.models.ImageFile;
import com.djd.fun.thumbsup.service.AssetService;
import com.google.common.base.Stopwatch;
import com.google.common.cache.LoadingCache;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.filters.Flip;
import net.coobird.thumbnailator.filters.Rotation;

import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_RIGHT;

public class BigImagePanel extends JPanel {

  private static final Logger log = LoggerFactory.getLogger(BigImagePanel.class);
  private final EventBus eventBus;
  private final AssetService assetService;
  private final LoadingCache<Asset, ImageFile> imageCache;
  private final ByteInputStreamImageSourceFactory imageSourceFactory;
  private @Nullable Asset asset;
  private @Nullable BufferedImage bufferedImage;

  @Inject
  public BigImagePanel(EventBus eventBus,
      AssetService assetService,
      @Experiment LoadingCache<Asset, ImageFile> imageCache,
      ByteInputStreamImageSourceFactory imageSourceFactory) {
    this.eventBus = eventBus;
    this.assetService = assetService;
    this.imageCache = imageCache;
    this.imageSourceFactory = imageSourceFactory;
    addKeyListener(new AnyKeyListener());
    setFocusable(true);
  }

  /**
   * Set focus on to this panel so that keyEvent will send to this listener
   *
   * @param event
   */
  @Subscribe
  public void focus(FocusBigImageViewEvent event) {
    requestFocus();
  }

  @Override
  protected void paintComponent(Graphics g) {
    if (bufferedImage != null) {
      Stopwatch stopwatch = Stopwatch.createStarted();
      int panelWidth = getWidth();
      int panelHeight = getHeight();
      int targetWidth = Math.min(bufferedImage.getWidth(), panelWidth);
      int targetHeight = Math.min(bufferedImage.getHeight(), panelHeight);
      BufferedImage scaledImage = Thumbnailator.createThumbnail(bufferedImage, targetWidth, targetHeight);
      log.info("resizing bufferedImage took: {}ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
      super.paintComponent(g);
      int scaledWidth = scaledImage.getWidth();
      int scaledHeight = scaledImage.getHeight();
      int pointX = 0;
      int pointY = 0;

      if (scaledWidth < panelWidth) {
        pointX = (panelWidth - scaledWidth) / 2;
      }
      if (scaledHeight < panelHeight) {
        pointY = (panelHeight - scaledHeight) / 2;
      }
      ((Graphics2D)g).drawImage(scaledImage, pointX, pointY, null);
      g.dispose();
    }
  }

  public void setAsset(Asset asset) {
    if (asset.getFileType() == Asset.FileType.IMAGE && !asset.equals(this.asset)) {
      try {
        ImageFile imageFile = imageCache.get(asset);
        Stopwatch stopwatch = Stopwatch.createStarted();
        bufferedImage = imageSourceFactory.createByteInputStreamImageSource(imageFile.getInputStream()).read();
        log.info("convert cached ImageFile to BufferedImage took: {}ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
      } catch (ExecutionException | IOException e) {
        log.warn("Failed to get image from cache", e);
      }
      log.info("cache stats: {}", imageCache.stats());
      this.asset = asset;
      repaint();
    } else {
      log.info("Skip loading big image.");
    }
  }

  private class AnyKeyListener extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent event) {
      log.info("keytyped {}", event.getKeyCode());
      switch (event.getKeyCode()) {
        case KeyEvent.VK_U:
          log.info("post event");
          eventBus.post(BackToThumbsViewEvent.with(asset));
          break;
        case KeyEvent.VK_J:
          bufferedImage = Flip.HORIZONTAL.apply(bufferedImage);
          break;
        case KeyEvent.VK_K:
          bufferedImage = Flip.VERTICAL.apply(bufferedImage);
          break;
        case KeyEvent.VK_H:
          bufferedImage = Rotation.RIGHT_90_DEGREES.apply(bufferedImage);
          break;
        case KeyEvent.VK_L:
          bufferedImage = Rotation.LEFT_90_DEGREES.apply(bufferedImage);
          break;
        case VK_LEFT:
          // show previous image
          Optional<Asset> prevAsset = assetService.getPreviousAsset(asset);
          if (prevAsset.isPresent()) {
            setAsset(prevAsset.get());
          }
          break;
        case VK_RIGHT:
          Optional<Asset> nextAsset = assetService.getNextAsset(asset);
          if (nextAsset.isPresent()) {
            setAsset(nextAsset.get());
          }
          break;
      }
      revalidate();
      repaint();
    }
  }
}
