package com.djd.fun.thumbsup.events;

import com.djd.fun.thumbsup.models.Asset;

public class ImageSelectedEvent {

  private final Asset asset;

  public ImageSelectedEvent(Asset asset) {
    this.asset = asset;
  }

  public Asset getAsset() {
    return asset;
  }

  public static ImageSelectedEvent with(Asset asset) {
    return new ImageSelectedEvent(asset);
  }
}
