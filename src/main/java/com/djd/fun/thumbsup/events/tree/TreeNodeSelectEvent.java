package com.djd.fun.thumbsup.events.tree;

import com.djd.fun.thumbsup.models.Asset;

public class TreeNodeSelectEvent {

  private final Asset asset;

  public TreeNodeSelectEvent(Asset asset) {
    this.asset = asset;
  }

  public Asset getAsset() {
    return asset;
  }

  public static TreeNodeSelectEvent with(Asset asset) {
    return new TreeNodeSelectEvent(asset);
  }
}
