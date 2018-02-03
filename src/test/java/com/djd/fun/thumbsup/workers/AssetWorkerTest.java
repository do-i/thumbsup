package com.djd.fun.thumbsup.workers;

import java.awt.Image;

import com.djd.fun.thumbsup.models.Asset;
import com.djd.fun.thumbsup.service.ImageService;
import com.djd.fun.thumbsup.ui.ThumbPanel;
import com.google.common.eventbus.EventBus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static com.google.common.truth.Truth.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class AssetWorkerTest extends Mockito {

  private @Mock EventBus eventBus;
  private @Mock ImageService imageService;
  private @Mock ThumbPanel thumbPanel;
  private @Mock Asset asset;
  private @Mock Image image;
  private AssetWorker assetWorker;

  @Before
  public void setUp() {
    assetWorker = new AssetWorker(eventBus, imageService, thumbPanel, asset);
  }

  @Test
  public void doInBackground() throws Exception {
    when(imageService.createThumbnailImage(asset)).thenReturn(image);
    assertThat(assetWorker.doInBackground()).isEqualTo(image);
  }
}