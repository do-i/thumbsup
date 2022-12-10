package com.djd.fun.thumbsup.service;

import com.djd.fun.thumbsup.models.Asset;
import com.djd.fun.thumbsup.models.AssetFactory;
import com.djd.fun.thumbsup.util.MoreMoreFiles;
import com.google.inject.Inject;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Optional;

public class AssetServiceImpl implements AssetService {

  private final AssetFactory assetFactory;

  @Inject
  public AssetServiceImpl(AssetFactory assetFactory) {
    this.assetFactory = assetFactory;
  }

  @Override
  public Optional<Asset> getPreviousAsset(Asset asset) {
    Path path = asset.getFilePath();
    // TODO improve algorithm to speed up
    Iterator<Path> peers = MoreMoreFiles.getPeerImagePaths(path).iterator();
    Path prevPath = peers.next(); // assume there must be at least one
    if (path.equals(prevPath)) {
      return Optional.empty();
    }
    while (peers.hasNext()) {
      Path currPath = peers.next();
      if (path.equals(currPath)) {
        break;
      }
      prevPath = currPath;
    }
    return Optional.of(assetFactory.createAsset(prevPath));
  }

  @Override
  public Optional<Asset> getNextAsset(Asset asset) {
    Path path = asset.getFilePath();
    // TODO improve algorithm to speed up
    Iterator<Path> peers = MoreMoreFiles.getPeerImagePaths(path).iterator();
    while (peers.hasNext()) {
      if (path.equals(peers.next()) && peers.hasNext()) {
        return Optional.of(assetFactory.createAsset(peers.next()));
      }
    }
    return Optional.empty();
  }
}
