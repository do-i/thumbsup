package com.djd.fun.thumbsup.events;

import java.util.Objects;

import com.djd.fun.thumbsup.models.Asset;

public class PreloadImageEvent {

  private final Asset asset;

  public PreloadImageEvent(Asset asset) {
    this.asset = asset;
  }

  public Asset getAsset() {
    return asset;
  }

  public static PreloadImageEvent with(Asset asset) {
    return new PreloadImageEvent(asset);
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PreloadImageEvent)) return false;
    PreloadImageEvent that = (PreloadImageEvent)o;
    return Objects.equals(asset, that.asset);
  }

  @Override public int hashCode() {
    return Objects.hash(asset);
  }
}
