package com.djd.fun.thumbsup.util;

import com.djd.fun.thumbsup.annotations.Experiment;
import com.djd.fun.thumbsup.models.Asset;
import com.djd.fun.thumbsup.models.ImageFile;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.djd.fun.thumbsup.util.SizeFormatter.humanReadableByteCount;

public class StatsHelper {

  private static final Logger log = LoggerFactory.getLogger(StatsHelper.class);
  private final LoadingCache<Asset, ImageFile> loadingCache;

  @Inject
  public StatsHelper(@Experiment LoadingCache<Asset, ImageFile> loadingCache) {
    this.loadingCache = loadingCache;
  }

  public void memoryDump() {
    Runtime runtime = Runtime.getRuntime();
    CacheStats cacheStats = loadingCache.stats();
    log.info("Memory.used:{},free:{},max:{},cache:{},evicted:{}",
        humanReadableByteCount(runtime.totalMemory()),
        humanReadableByteCount(runtime.freeMemory()),
        humanReadableByteCount(runtime.maxMemory()),
        cacheStats.loadCount(),
        cacheStats.evictionCount());
  }
}
