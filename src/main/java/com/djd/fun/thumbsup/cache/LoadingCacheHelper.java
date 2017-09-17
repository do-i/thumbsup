package com.djd.fun.thumbsup.cache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.djd.fun.thumbsup.annotations.Experiment;
import com.djd.fun.thumbsup.events.PreloadImageEvent;
import com.djd.fun.thumbsup.models.Asset;
import com.djd.fun.thumbsup.models.ImageFile;
import com.djd.fun.thumbsup.util.SizeFormatter;
import com.djd.fun.thumbsup.util.StatsHelper;
import com.google.common.cache.LoadingCache;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadingCacheHelper implements CacheHelper {

  private static final Logger log = LoggerFactory.getLogger(LoadingCacheHelper.class);
  private final LoadingCache<Asset, ImageFile> loadingCache;
  private final StatsHelper statsHelper;
  private final ExecutorService executorService;

  @Inject
  public LoadingCacheHelper(ExecutorService executorService,
      StatsHelper statsHelper,
      @Experiment LoadingCache<Asset, ImageFile> loadingCache) {
    log.info("init");
    this.loadingCache = loadingCache;
    this.statsHelper = statsHelper;
    this.executorService = executorService;
  }

  /**
   * Called back from {@link com.djd.fun.thumbsup.workers.AssetWorker}.
   * During thumbnail loading and/or creation, warm up the cache with images.
   * This allow faster loading of an image when user select a big picture.
   *
   * @param event that contains {@link Asset}
   */
  @Subscribe
  @Override
  public void preloadImage(PreloadImageEvent event) {
    executorService.execute(() -> {
      log.info("preloadImage for asset: {}", event.getAsset());
      try {
        ImageFile imageFile = loadingCache.get(event.getAsset());
        log.info("cached image file size: {}", SizeFormatter.humanReadableByteCount(imageFile.size()));
        statsHelper.memoryDump();
      } catch (ExecutionException e) {
        log.warn("Failed to preload Image", e);
      }
    });
  }
}
