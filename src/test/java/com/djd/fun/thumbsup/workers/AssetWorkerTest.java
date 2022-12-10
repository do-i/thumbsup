package com.djd.fun.thumbsup.workers;

import static com.google.common.truth.Truth.assertThat;

import com.djd.fun.thumbsup.models.Asset;
import com.djd.fun.thumbsup.service.ImageService;
import com.djd.fun.thumbsup.ui.ThumbPanel;
import com.google.common.eventbus.EventBus;
import java.awt.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class AssetWorkerTest extends Mockito {

  private @Mock EventBus eventBus;
  private @Mock ImageService imageService;
  private @Mock ThumbPanel thumbPanel;
  private @Mock Asset asset;
  private @Mock Image image;
  private AssetWorker assetWorker;

  @BeforeEach
  public void setUp() {
    assetWorker = new AssetWorker(eventBus, imageService, thumbPanel, asset);
  }

  @Test
  public void doInBackground() throws Exception {
    when(imageService.createThumbnailImage(asset)).thenReturn(image);
    assertThat(assetWorker.doInBackground()).isEqualTo(image);
  }
}