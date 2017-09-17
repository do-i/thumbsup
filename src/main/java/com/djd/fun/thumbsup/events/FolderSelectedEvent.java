package com.djd.fun.thumbsup.events;

import com.djd.fun.thumbsup.models.Asset;

public class FolderSelectedEvent {

  private final Asset asset;

  public FolderSelectedEvent(Asset asset) {
    this.asset = asset;
  }

  public Asset getAsset() {
    return asset;
  }

  public static FolderSelectedEvent with(Asset asset) {
    return new FolderSelectedEvent(asset);
  }
}
