package com.djd.fun.thumbsup.service;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.djd.fun.thumbsup.models.AssetImpl;
import com.djd.fun.thumbsup.models.ImmutableSize;
import com.google.common.io.MoreFiles;
import com.google.common.io.Resources;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static com.djd.fun.thumbsup.testutil.MoreMoreResources.getPathFromResouces;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ImageServiceImplTest extends Mockito {

  private static final Path IMAGE_PATH = getPathFromResouces("test.png");
  private static final ImmutableSize THUMB_SIZE = ImmutableSize.of(200, 200);
  private @Mock Image folderImage;
  private @Mock Image defaultImage;
  private File cacheDir;
  private AssetImpl imageAsset;
  private ImageServiceImpl imageService;

  @Before
  public void setUp() throws IOException {
    cacheDir = Files.createTempDirectory("thumbcache").toFile();
    imageService = new ImageServiceImpl(folderImage, defaultImage, THUMB_SIZE);
    imageAsset = new AssetImpl(cacheDir, IMAGE_PATH);
  }

  @After
  public void tireDown() throws IOException {
    MoreFiles.deleteRecursively(cacheDir.toPath());
  }

  @Test
  public void createThumbnailImage() throws Exception {
    assertFalse(Files.exists(imageAsset.getThumbnailPath()));
    Image image = imageService.createThumbnailImage(imageAsset);
    assertThat(image.getHeight(null)).isEqualTo(141);
    assertThat(image.getWidth(null)).isEqualTo(200);
    assertTrue(Files.exists(imageAsset.getThumbnailPath()));
    verifyNoMoreInteractions(folderImage);
    verifyNoMoreInteractions(defaultImage);
  }
}