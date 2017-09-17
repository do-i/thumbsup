package com.djd.fun.thumbsup.events;

import com.djd.fun.thumbsup.models.Asset;

public class BackToThumbsViewEvent {

  private final Asset asset;

  public BackToThumbsViewEvent(Asset asset) {
    this.asset = asset;
  }

  public static BackToThumbsViewEvent with(Asset asset) {
    return new BackToThumbsViewEvent(asset);
  }
}
