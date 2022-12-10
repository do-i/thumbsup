package com.djd.fun.thumbsup.events;

import com.djd.fun.thumbsup.models.Asset;
import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PreloadImageEvent that)) {
      return false;
    }
    return Objects.equals(asset, that.asset);
  }

  @Override
  public int hashCode() {
    return Objects.hash(asset);
  }
}
