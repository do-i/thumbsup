package com.djd.fun.thumbsup.service;

import static com.google.common.truth.Truth.assertThat;

import com.djd.fun.thumbsup.models.Asset;
import com.djd.fun.thumbsup.models.AssetFactory;
import com.djd.fun.thumbsup.testutil.DummyFilesGenerator;
import com.djd.fun.thumbsup.testutil.MoreMoreResources;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(MockitoExtension.class)
public class AssetServiceImplTest extends Mockito {

  private static final Logger log = LoggerFactory.getLogger(AssetServiceImplTest.class);
  private static Path testFilesRoot;
  private static Path firstFile;
  private static Path secondFile;
  private static Path thirdFile;
  private @Mock AssetFactory assetFactory;
  private @Mock Asset asset;
  private @Mock Asset assetAnother;
  private AssetServiceImpl assetService;

  @BeforeAll
  public static void setUpOnce() throws IOException {
    testFilesRoot = DummyFilesGenerator.builder()
        .depth(1).numOfFilePerDir(3).fileSuffix(".png")
        .build().generate();
    firstFile = Paths.get(testFilesRoot.toString(), "D-0", "F-0.png");
    secondFile = Paths.get(testFilesRoot.toString(), "D-0", "F-1.png");
    thirdFile = Paths.get(testFilesRoot.toString(), "D-0", "F-2.png");

    log.info("tetFileRoot: {}", testFilesRoot);
  }

  @AfterAll
  public static void tearDownOnce() throws IOException {
    MoreMoreResources.deleteRecursively(testFilesRoot);
  }

  @BeforeEach
  public void setUp() {
    assetService = new AssetServiceImpl(assetFactory);
  }

  @Test
  public void getPreviousAsset_first_empty() {
    when(asset.getFilePath()).thenReturn(firstFile);
    assertThat(assetService.getPreviousAsset(asset)).isEqualTo(Optional.empty());
    verifyNoInteractions(assetFactory);
  }

  @Test
  public void getPreviousAsset_second_first() {
    when(asset.getFilePath()).thenReturn(secondFile);
    when(assetFactory.createAsset(firstFile)).thenReturn(assetAnother);
    assertThat(assetService.getPreviousAsset(asset)).isEqualTo(Optional.of(assetAnother));
  }

  @Test
  public void getPreviousAsset_third_second() {
    when(asset.getFilePath()).thenReturn(thirdFile);
    when(assetFactory.createAsset(secondFile)).thenReturn(assetAnother);
    assertThat(assetService.getPreviousAsset(asset)).isEqualTo(Optional.of(assetAnother));
  }

  @Test
  public void getNextAsset_first_second() {
    when(asset.getFilePath()).thenReturn(firstFile);
    when(assetFactory.createAsset(secondFile)).thenReturn(assetAnother);
    assertThat(assetService.getNextAsset(asset)).isEqualTo(Optional.of(assetAnother));
  }

  @Test
  public void getNextAsset_second_third() {
    when(asset.getFilePath()).thenReturn(secondFile);
    when(assetFactory.createAsset(thirdFile)).thenReturn(assetAnother);
    assertThat(assetService.getNextAsset(asset)).isEqualTo(Optional.of(assetAnother));
  }

  @Test
  public void getNextAsset_third_empty() {
    when(asset.getFilePath()).thenReturn(thirdFile);
    assertThat(assetService.getNextAsset(asset)).isEqualTo(Optional.empty());
    verifyNoInteractions(assetFactory);
  }
}