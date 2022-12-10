package com.djd.fun.thumbsup;

import com.djd.fun.thumbsup.annotations.BorderSelected;
import com.djd.fun.thumbsup.annotations.BorderUnselected;
import com.djd.fun.thumbsup.annotations.Experiment;
import com.djd.fun.thumbsup.annotations.ImageDefault;
import com.djd.fun.thumbsup.annotations.ImageFolder;
import com.djd.fun.thumbsup.annotations.InitialDir;
import com.djd.fun.thumbsup.annotations.LayoutBorder;
import com.djd.fun.thumbsup.annotations.LayoutWrap;
import com.djd.fun.thumbsup.annotations.PanelBackgroundColor;
import com.djd.fun.thumbsup.annotations.PanelCard;
import com.djd.fun.thumbsup.annotations.PanelFont;
import com.djd.fun.thumbsup.annotations.PanelForegroundColor;
import com.djd.fun.thumbsup.annotations.PanelThumbs;
import com.djd.fun.thumbsup.annotations.PanelTree;
import com.djd.fun.thumbsup.annotations.ScrollVertical;
import com.djd.fun.thumbsup.annotations.ThumbnailCacheDir;
import com.djd.fun.thumbsup.annotations.ThumbnailImageBoundSize;
import com.djd.fun.thumbsup.annotations.UserHomeDir;
import com.djd.fun.thumbsup.cache.CacheHelper;
import com.djd.fun.thumbsup.cache.LoadingCacheHelper;
import com.djd.fun.thumbsup.cache.loader.FileCacheLoader;
import com.djd.fun.thumbsup.layout.WrapLayout;
import com.djd.fun.thumbsup.models.Asset;
import com.djd.fun.thumbsup.models.AssetFactory;
import com.djd.fun.thumbsup.models.AssetImpl;
import com.djd.fun.thumbsup.models.ByteInputStreamImageSourceFactory;
import com.djd.fun.thumbsup.models.ImageFile;
import com.djd.fun.thumbsup.models.ImmutableSize;
import com.djd.fun.thumbsup.models.ThumbViewSize;
import com.djd.fun.thumbsup.service.AssetService;
import com.djd.fun.thumbsup.service.AssetServiceImpl;
import com.djd.fun.thumbsup.service.ImageService;
import com.djd.fun.thumbsup.service.ImageServiceImpl;
import com.djd.fun.thumbsup.ui.CardPanel;
import com.djd.fun.thumbsup.ui.FontPanel;
import com.djd.fun.thumbsup.ui.MainFrame;
import com.djd.fun.thumbsup.ui.MainPanel;
import com.djd.fun.thumbsup.ui.ThumbPanelFactory;
import com.djd.fun.thumbsup.ui.ThumbsPanel;
import com.djd.fun.thumbsup.ui.TreePanel;
import com.djd.fun.thumbsup.workers.WorkerFactory;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.eventbus.EventBus;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.LayoutManager;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThumbsupModule extends PrivateModule {

  private static final String USER_HOME = "user.home";
  private static final Logger log = LoggerFactory.getLogger(ThumbsupModule.class);

  @Override
  protected void configure() {
    Names.bindProperties(binder(), System.getProperties());
    install(new FactoryModuleBuilder().build(WorkerFactory.class));
    install(new FactoryModuleBuilder().build(ThumbPanelFactory.class));
    install(new FactoryModuleBuilder().build(ByteInputStreamImageSourceFactory.class));
    install(new FactoryModuleBuilder().implement(Asset.class, AssetImpl.class)
        .build(AssetFactory.class));
    bind(CacheHelper.class).to(LoadingCacheHelper.class).asEagerSingleton();
    createAndBindEventBus();
    bind(JPanel.class).to(MainPanel.class).in(Singleton.class);
    bind(JPanel.class).annotatedWith(PanelCard.class).to(CardPanel.class).in(Singleton.class);
    bind(JPanel.class).annotatedWith(PanelThumbs.class).to(ThumbsPanel.class).in(Singleton.class);
    bind(JPanel.class).annotatedWith(PanelFont.class).to(FontPanel.class).in(Singleton.class);
    bind(JTree.class).annotatedWith(PanelTree.class).to(TreePanel.class).in(Singleton.class);
    bind(ImageService.class).to(ImageServiceImpl.class).in(Singleton.class);
    bind(AssetService.class).to(AssetServiceImpl.class).in(Singleton.class);
    bind(JFrame.class).to(MainFrame.class);
    expose(JFrame.class);
  }

  @Provides
  @Singleton
  @LayoutWrap
  private LayoutManager provideWrapLayout() {
    log.info("provideWrapLayout");
    return new WrapLayout(WrapLayout.LEFT, 7, 2);
  }

  @Provides
  @Singleton
  @LayoutBorder
  private LayoutManager provideBorderLayout() {
    log.info("provideBorderLayout");
    return new BorderLayout();
  }

  @Provides
  @Singleton
  private JSplitPane provideJSplitPane(@PanelTree JScrollPane treePanel,
      @PanelCard JPanel cardPanel) {
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treePanel, cardPanel);
    splitPane.setResizeWeight(0.12d); // Give 12% of total split pane width for tree view
    splitPane.setOneTouchExpandable(true); // This allows one side of a panel be totally invisible.
    return splitPane;
  }

  @Provides
  @Singleton
  @PanelThumbs
  private JScrollPane provideScrollThumbsPanel(@PanelThumbs JPanel thumbsPanel,
      @ScrollVertical int verticalScrollUnitIncrement) {
    JScrollPane scrollPane = new JScrollPane(thumbsPanel);
    scrollPane.getVerticalScrollBar().setUnitIncrement(verticalScrollUnitIncrement);
    return scrollPane;
  }

  @Provides
  @Singleton
  @PanelTree
  private JScrollPane provideScrollTreePanel(@PanelTree JTree treePanel,
      @ScrollVertical int verticalScrollUnitIncrement) {
    JScrollPane scrollPane = new JScrollPane(treePanel);
    scrollPane.getVerticalScrollBar().setUnitIncrement(verticalScrollUnitIncrement);
    return scrollPane;
  }

  @Provides
  @Singleton
  @ScrollVertical
  private int provideVerticalScrollUnitIncrement() {
    return 30;
  }

  @Provides
  @Singleton
  private ThumbViewSize provideThumbSize() {
    return new ThumbViewSize();
  }

  @Provides
  @Singleton
  @ThumbnailImageBoundSize
  private ImmutableSize provideImmutableSize() {
    return ImmutableSize.of(200, 200);
  }

  @Provides
  @Singleton
  @ImageFolder
  private Image provideFolderImage() {
    try {
      return ImageIO.read(Resources.getResource("folder180.png"));
    } catch (IOException e) {
      log.error("Failed to load folder image.");
      throw new RuntimeException(e);
    }
  }

  @Provides
  @Singleton
  @ImageDefault
  private Image provideDefaultImage() {
    try {
      log.info("provideDefaultImage");
      return ImageIO.read(Resources.getResource("oops180.png"));
    } catch (IOException e) {
      log.error("Failed to load default image.");
      throw new RuntimeException(e);
    }
  }

  @Provides
  @Singleton
  @BorderSelected
  private Border provideSelectionBorder() {
    return BorderFactory.createEtchedBorder(Color.RED, Color.PINK);
  }

  @Provides
  @Singleton
  @BorderUnselected
  private Border provideUnselectionBorder() {
    return BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
  }

  @Provides
  @Singleton
  @PanelBackgroundColor
  private Color providePanelBackgroundColor() {
    return new Color(0x282C34);
  }

  @Provides
  @Singleton
  @PanelForegroundColor
  private Color providePanelForegroundColor() {
    return new Color(0xFCFCFC);
  }

  @Provides
  @Singleton
  @ThumbnailCacheDir
  private File provideCacheDir(@Named(USER_HOME) String userHome) {
    File cacheDir = Paths.get(userHome, "thumbsup").toFile();
    try {
      Files.createParentDirs(cacheDir);
    } catch (IOException e) {
      log.error("Failed to create cache dir", e);
      throw new RuntimeException(e);
    }
    return cacheDir;
  }

  @Provides
  @Singleton
  @UserHomeDir
  private Path providesUserHomeDirPath(@Named(USER_HOME) String userHome) {
    log.info("userHome: {}", userHome);
    return Paths.get(userHome);
  }

  @Provides
  @Singleton
  @InitialDir
  private Path providesUserHomeImageDirPath(@UserHomeDir Path userHome) {
    log.info("userHome: {}", userHome);
    return userHome;
  }

  @Provides
  @Singleton
  @Experiment
  private CacheLoader<Asset, ImageFile> provideFileCacheLoader(FileCacheLoader impl) {
    return impl;
  }

  @Provides
  @Singleton
  @Experiment
  private LoadingCache<Asset, ImageFile> provideFileLoadingCache(
      @Experiment CacheLoader<Asset, ImageFile> imageCacheLoader) {
    return CacheBuilder.newBuilder()
        .concurrencyLevel(2)
        .recordStats()
        .maximumSize(100)
        .expireAfterAccess(3, TimeUnit.MINUTES)
        .build(imageCacheLoader);
  }

  @Provides
  @Singleton
  private ExecutorService provideExecutorService() {
    return Executors.newFixedThreadPool(2);
  }

  /**
   * https://spin.atomicobject.com/2012/01/13/the-guava-eventbus-on-guice/ This bindListener allows
   * Guice to register all methods that are annotated with @Subscribe
   */
  private void createAndBindEventBus() {
    EventBus eventBus = new EventBus("BIG BLU BUS");
    bindListener(Matchers.any(), new TypeListener() {
      public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
        typeEncounter.register((InjectionListener<I>) i -> eventBus.register(i));
      }
    });
    bind(EventBus.class).toInstance(eventBus);
  }
}
