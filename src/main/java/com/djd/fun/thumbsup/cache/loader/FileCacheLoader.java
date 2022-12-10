package com.djd.fun.thumbsup.cache.loader;

import com.djd.fun.thumbsup.models.Asset;
import com.djd.fun.thumbsup.models.ImageFile;
import com.google.common.cache.CacheLoader;
import java.nio.file.Files;

public class FileCacheLoader extends CacheLoader<Asset, ImageFile> {

  @Override
  public ImageFile load(Asset asset) throws Exception {
    return new ImageFile(Files.readAllBytes(asset.getFilePath()));
  }
}
