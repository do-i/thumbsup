package com.djd.fun.thumbsup.models;

import static com.google.common.truth.Truth.assertThat;

import com.djd.fun.thumbsup.testutil.DummyFilesGenerator;
import com.djd.fun.thumbsup.testutil.MoreMoreResources;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TreeNodeTest {

  private static Path testFilesRoot;
  private static Path firstFile;
  private static Path secondFile;
  private static Path thirdFile;
  private TreeNode treeNode;

  @BeforeAll
  public static void setUpOnce() throws IOException {
    testFilesRoot = DummyFilesGenerator.builder()
        .depth(2).numOfFilePerDir(3).fileSuffix(".png")
        .build().generate();
    firstFile = Paths.get(testFilesRoot.toString(), "D-0", "F-0.png");
    secondFile = Paths.get(testFilesRoot.toString(), "D-0", "F-1.png");
    thirdFile = Paths.get(testFilesRoot.toString(), "D-0", "F-2.png");
  }

  @AfterAll
  public static void tearDownOnce() throws IOException {
    MoreMoreResources.deleteRecursively(testFilesRoot);
  }

  @BeforeEach
  public void setUp() {
    treeNode = new TreeNode(Paths.get(testFilesRoot.toString(), "D-0"));
  }

  @Test
  public void getImageCount() {
    TreeNode treeNode = new TreeNode(Paths.get(testFilesRoot.toString(), "D-0"));
    assertThat(treeNode.getImageCount()).isEqualTo(3);
  }

  @Test
  public void getFolders() {
    assertThat(treeNode.getFolders())
        .containsExactly(Paths.get(testFilesRoot.toString(), "D-0", "D-1"));
  }

  @Test
  public void getDir() {
    assertThat(treeNode.getDir().getFileName().toString()).isEqualTo("D-0");
  }
}