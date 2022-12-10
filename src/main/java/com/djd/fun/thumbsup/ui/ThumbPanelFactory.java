package com.djd.fun.thumbsup.ui;

import com.djd.fun.thumbsup.models.Asset;

public interface ThumbPanelFactory {

  ThumbViewPanel createThumbViewPanel(ThumbPanel thumbPanel, Asset asset);

  ThumbPanel createThumbPanel(Asset asset);
}
