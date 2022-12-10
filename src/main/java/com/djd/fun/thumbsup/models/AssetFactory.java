package com.djd.fun.thumbsup.models;

import java.nio.file.Path;

public interface AssetFactory {

  Asset createAsset(Path assetPath);
}
