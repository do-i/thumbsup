package com.djd.fun.thumbsup.ui;

import static com.djd.fun.thumbsup.util.MoreMoreFiles.FOLDER_OR_IMAGE;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_K;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_P;
import static java.awt.event.KeyEvent.VK_R;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_U;
import static java.awt.event.KeyEvent.VK_UP;

import com.djd.fun.thumbsup.annotations.BorderSelected;
import com.djd.fun.thumbsup.annotations.BorderUnselected;
import com.djd.fun.thumbsup.annotations.InitialDir;
import com.djd.fun.thumbsup.annotations.LayoutWrap;
import com.djd.fun.thumbsup.events.FocusThumbViewEvent;
import com.djd.fun.thumbsup.events.FolderSelectedEvent;
import com.djd.fun.thumbsup.events.ImageSelectedEvent;
import com.djd.fun.thumbsup.models.Asset;
import com.djd.fun.thumbsup.models.AssetFactory;
import com.djd.fun.thumbsup.models.ImmutableSize;
import com.djd.fun.thumbsup.models.ThumbPosition;
import com.djd.fun.thumbsup.models.ThumbViewSize;
import com.djd.fun.thumbsup.util.MoreMoreFiles;
import com.djd.fun.thumbsup.workers.AssetWorker;
import com.djd.fun.thumbsup.workers.WorkerFactory;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThumbsPanel extends JPanel {

  private static final Logger log = LoggerFactory.getLogger(ThumbsPanel.class);
  private final EventBus eventBus;
  private final ThumbPanelFactory thumbPanelFactory;
  private final AssetFactory assetFactory;
  private final WorkerFactory workerFactory;
  private final ThumbViewSize thumbViewSize;
  private final Border unselectedBorder;
  private final Border selectedBorder;
  private Path currentDir;
  private final ThumbPosition thumbPosition;
  private final List<SwingWorker> workers;

  @Inject
  public ThumbsPanel(EventBus eventBus,
      ThumbPanelFactory thumbPanelFactory,
      AssetFactory assetFactory,
      WorkerFactory workerFactory,
      ThumbViewSize thumbViewSize,
      @LayoutWrap LayoutManager layout,
      @BorderUnselected Border unselectedBorder,
      @BorderSelected Border selectedBorder,
      @InitialDir Path currentDir) {
    this.eventBus = eventBus;
    this.thumbPanelFactory = thumbPanelFactory;
    this.assetFactory = assetFactory;
    this.workerFactory = workerFactory;
    this.unselectedBorder = unselectedBorder;
    this.selectedBorder = selectedBorder;
    this.thumbViewSize = thumbViewSize;
    this.currentDir = currentDir;
    this.thumbPosition = new ThumbPosition();
    this.workers = Lists.newArrayList();
    setLayout(layout);
    addComponentListener(new PanelResizeListener());
    addKeyListener(new KeyInputListener());
    setFocusable(true); // this is for keyListener
    loadFiles(assetFactory.createAsset(currentDir));
    log.info("init");
  }

  /**
   * Set focus on to this panel so that keyEvent will send to this listener
   *
   * @param event
   */
  @Subscribe
  public void focus(FocusThumbViewEvent event) {
    requestFocus();
  }

  @Subscribe
  public void folderSelected(FolderSelectedEvent event) {
    loadFiles(event.getAsset());
  }

  private AssetWorker createAssetWorker(Asset asset) {
    ThumbPanel thumbPanel = thumbPanelFactory.createThumbPanel(asset);
    // Note: Add empty thumbViewPanel to thumbsPanel so that display order is deterministic.
    add(thumbPanelFactory.createThumbViewPanel(thumbPanel, asset));
    log.debug("createAssetWorker::ThumbsPanel has {} components.", getComponents().length);
    return workerFactory.createAssetWorker(thumbPanel, asset);
  }

  private void loadFiles(Asset asset) {
    // Stop any AssetWorker that is still running.
    workers.forEach(worker -> worker.cancel(true));
    // Remove all worker references to avoid memory leak
    workers.clear();
    removeAll();
    revalidate();
    repaint();
    log.debug("loadFiles::ThumbsPanel has {} components.", getComponents().length);
    log.debug("mouse Clicked: creating new asset workers and run it");
    try {
      currentDir = asset.getFilePath();
      Files.list(asset.getFilePath())
          .filter(MoreMoreFiles::isNotHidden)
          .filter(FOLDER_OR_IMAGE)
          .map(assetFactory::createAsset)
          .sorted()
          .map(ThumbsPanel.this::createAssetWorker)
          .forEach(worker -> {
            workers.add(worker);
            worker.execute();
          });
    } catch (IOException e) {
      log.warn("Failed fetching files from current dir", e);
    }
  }

  private void buildThumbViewPositionMap() {
    ImmutableSize imageViewSize = thumbViewSize.getImageViewSize();
    thumbPosition.reset();
    for (Component compo : getComponents()) {
      removeBorder(compo);
      thumbPosition.add(imageViewSize, compo);
    }
  }

  private void addBorder(Component component) {
    JComponent selected = ((JComponent) component);
    if (selected != null) {
      selected.setBorder(selectedBorder);
    }
  }

  private void removeBorder(Component component) {
    JComponent selected = ((JComponent) component);
    if (selected != null) {
      selected.setBorder(unselectedBorder);
    }
  }

  /**
   * React for window size change to update position of thumbs on a metric.
   */
  private class PanelResizeListener extends ComponentAdapter {

    @Override
    public void componentResized(ComponentEvent event) {
      // TODO redesign this....
      // Can we do lazy position rebuild
      // on this event, we just update boolean resized = true;
      // then on directional key event (up, down, left, right) if resized then call
      // buildThumbViewPositionMap();
      Stopwatch stopwatch = Stopwatch.createStarted();
      buildThumbViewPositionMap();
      log.info("Resize of panel causes re-buildThumbViewPositionMap and took {} microsec",
          stopwatch.elapsed(TimeUnit.MICROSECONDS));
    }

    /**
     * Invoked when the component has been made visible.
     */
    @Override
    public void componentShown(ComponentEvent event) {
      log.info("componentShow");
      loadFiles(assetFactory.createAsset(currentDir));
      throw new IllegalStateException("This is not intended to be called at the moment.");
    }
  }

  private class KeyInputListener extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent event) {
      removeBorder(thumbPosition.getComponent());
      switch (event.getKeyCode()) {
        case VK_UP:
          thumbPosition.moveUp();
          break;
        case VK_DOWN:
          thumbPosition.moveDown();
          break;
        case VK_LEFT:
          thumbPosition.moveLeft();
          break;
        case VK_RIGHT:
          thumbPosition.moveRight();
          break;
        case VK_ENTER:
          // TODO Is there a better way to access Asset instance without getting it from ThumbViewPanel??
          ThumbViewPanel thumbViewPanel = (ThumbViewPanel) thumbPosition.getComponent();
          if (thumbViewPanel != null) {
            Asset asset = thumbViewPanel.getAsset();
            if (asset.getFileType() == Asset.FileType.IMAGE) {
              eventBus.post(ImageSelectedEvent.with(asset));
              log.info("post ImageSelectedEvent");
            } else {
              loadFiles(asset);
              log.info("FOLDER");
            }
          }
          break;
        case VK_K:
          thumbViewSize.increment();
          repaint();
          break;
        case VK_P:
          thumbViewSize.decrement();
          repaint();
          break;
        case VK_R:
          loadFiles(assetFactory.createAsset(currentDir));
          break;
        case VK_U:
          Path parent = currentDir.getParent();
          if (parent != null) {
            loadFiles(assetFactory.createAsset(parent));
          }
          break;
        default:
          log.info("{} keycode was pressed", event.getKeyCode());
      }
      addBorder(thumbPosition.getComponent());
      thumbPosition.printDebugInfo();
    }
  }
}
