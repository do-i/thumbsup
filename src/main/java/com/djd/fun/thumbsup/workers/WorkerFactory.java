package com.djd.fun.thumbsup.workers;

import com.djd.fun.thumbsup.models.Asset;
import com.djd.fun.thumbsup.ui.ThumbPanel;

public interface WorkerFactory {
  AssetWorker createAssetWorker(ThumbPanel thumbPanel, Asset asset);
}
