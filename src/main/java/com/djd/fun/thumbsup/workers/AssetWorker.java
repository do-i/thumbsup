package com.djd.fun.thumbsup.workers;

import com.djd.fun.thumbsup.events.PreloadImageEvent;
import com.djd.fun.thumbsup.models.Asset;
import com.djd.fun.thumbsup.service.ImageService;
import com.djd.fun.thumbsup.ui.ThumbPanel;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import java.awt.Image;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class AssetWorker extends SwingWorker<Image, Void> {

  private static final Logger log = LoggerFactory.getLogger(AssetWorker.class);
  private final EventBus eventBus;
  private final ThumbPanel thumbPanel;
  private final ImageService imageService;
  private final Asset asset;

  @Inject
  public AssetWorker(
      EventBus eventBus,
      ImageService imageService,
      @Assisted ThumbPanel thumbPanel,
      @Assisted Asset asset) {
    this.eventBus = eventBus;
    this.thumbPanel = thumbPanel;
    this.imageService = imageService;
    this.asset = asset;
    log.info("init assetPath:{}", asset);
  }

  @Override
  protected Image doInBackground() throws Exception {
    log.info("doInBackground");
    return imageService.createThumbnailImage(asset);
  }

  @Override
  protected void done() {
    if (isCancelled()) {
      return;
    }
    try {
      thumbPanel.setImage(get());
      if (asset.getFileType() == Asset.FileType.IMAGE) {
        eventBus.post(PreloadImageEvent.with(asset));
      }
    } catch (InterruptedException | ExecutionException e) {
      log.warn("Failed getting image");
    }
  }
}
