package com.djd.fun.thumbsup.service;

import static com.djd.fun.thumbsup.testutil.MoreMoreResources.getPathFromResouces;
import static com.google.common.truth.Truth.assertThat;

import com.djd.fun.thumbsup.models.AssetImpl;
import com.djd.fun.thumbsup.models.ImmutableSize;
import com.google.common.io.MoreFiles;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ImageServiceImplTest extends Mockito {

  private static final Path IMAGE_PATH = getPathFromResouces("test.png");
  private static final ImmutableSize THUMB_SIZE = ImmutableSize.of(200, 200);
  private @Mock Image folderImage;
  private @Mock Image defaultImage;
  private File cacheDir;
  private AssetImpl imageAsset;
  private ImageServiceImpl imageService;

  @BeforeEach
  public void setUp() throws IOException {
    cacheDir = Files.createTempDirectory("thumbcache").toFile();
    imageService = new ImageServiceImpl(folderImage, defaultImage, THUMB_SIZE);
    imageAsset = new AssetImpl(cacheDir, IMAGE_PATH, digestUtils);
  }

  @AfterEach
  public void tireDown() throws IOException {
    MoreFiles.deleteRecursively(cacheDir.toPath());
  }

  @Test
  public void createThumbnailImage() throws Exception {
    assertThat(Files.exists(imageAsset.getThumbnailPath())).isFalse();
    Image image = imageService.createThumbnailImage(imageAsset);
    assertThat(image.getHeight(null)).isEqualTo(141);
    assertThat(image.getWidth(null)).isEqualTo(200);
    assertThat(Files.exists(imageAsset.getThumbnailPath())).isTrue();
    verifyNoMoreInteractions(folderImage);
    verifyNoMoreInteractions(defaultImage);
  }
}