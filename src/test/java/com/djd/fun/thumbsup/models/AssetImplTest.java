package com.djd.fun.thumbsup.models;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static com.google.common.truth.Truth.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class AssetImplTest extends Mockito {
  private static final Path IMAGE_PATH = Paths.get("somedir", "test.jpg");
  private @Mock File cacheDir;
  private AssetImpl imageAsset;
  private AssetImpl folderAsset;

  @Before
  public void setUp() throws IOException {
    imageAsset = new AssetImpl(cacheDir, IMAGE_PATH);
    Path dirPath = Files.createTempDirectory("junit");
    folderAsset = new AssetImpl(cacheDir, dirPath);
  }

  @Test
  public void getFileType_image() throws Exception {
    assertThat(imageAsset.getFileType()).isSameAs(Asset.FileType.IMAGE);
    verifyZeroInteractions(cacheDir);
  }

  @Test
  public void getFileType_folder() throws Exception {
    assertThat(folderAsset.getFileType()).isSameAs(Asset.FileType.FOLDER);
    verifyZeroInteractions(cacheDir);
  }

  @Test
  public void getFile() throws Exception {
    assertThat(imageAsset.getFile()).isEqualTo(IMAGE_PATH.toFile());
    verifyZeroInteractions(cacheDir);
  }

  @Test
  public void getThumbnailPath_image() throws Exception {
    when(cacheDir.toString()).thenReturn("/thumbcache/200x200");
    assertThat(imageAsset.getThumbnailPath().toString()).isEqualTo("/thumbcache/200x200/somedir/test.jpg");
  }

  @Test
  public void compareTo_folderToImage() throws Exception {
    assertThat(folderAsset).isLessThan(imageAsset);
  }

  @Ignore("TODO Fix possible race condition with path name")
  @Test
  public void compareTo_foldes() throws Exception {
    AssetImpl otherFolder = new AssetImpl(cacheDir, Files.createTempDirectory("junit4"));
    assertThat(folderAsset).isLessThan(otherFolder);
  }

  @Ignore("TODO Fix possible race condition with path name")
  @Test
  public void compareTo_images() throws Exception {
    AssetImpl otherImage = new AssetImpl(cacheDir, Paths.get("somedir", "test2.jpg"));
    assertThat(imageAsset).isLessThan(otherImage);
  }

}