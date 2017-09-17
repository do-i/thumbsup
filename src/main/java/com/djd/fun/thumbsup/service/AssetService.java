package com.djd.fun.thumbsup.service;

import java.util.Optional;

import com.djd.fun.thumbsup.models.Asset;

public interface AssetService {
  /**
   * Finds previous asset to the given asset
   *
   * @param asset
   * @return previous asset to the given asset
   */
  Optional<Asset> getPreviousAsset(Asset asset);

  /**
   * Finds next asset to the given asset
   *
   * @param asset
   * @return next asset to the given asset
   */
  Optional<Asset> getNextAsset(Asset asset);
}
