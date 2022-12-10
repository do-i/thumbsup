package com.djd.fun.thumbsup.ui;

import com.djd.fun.thumbsup.annotations.PanelThumbs;
import com.djd.fun.thumbsup.events.BackToThumbsViewEvent;
import com.djd.fun.thumbsup.events.FocusBigImageViewEvent;
import com.djd.fun.thumbsup.events.FocusThumbViewEvent;
import com.djd.fun.thumbsup.events.FolderSelectedEvent;
import com.djd.fun.thumbsup.events.ImageSelectedEvent;
import com.djd.fun.thumbsup.events.tree.TreeNodeSelectEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import java.awt.CardLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CardPanel extends JPanel {

  private static final Logger log = LoggerFactory.getLogger(CardPanel.class);
  private static final String THUMB = "THUMB";
  private static final String BIG = "BIG";
  private final EventBus eventBus;
  private final BigImagePanel bigImagePanel;
  private String visible = THUMB;

  @Inject
  public CardPanel(EventBus eventBus, @PanelThumbs JScrollPane thumbsScrollPanel,
      BigImagePanel bigImagePanel) {
    this.eventBus = eventBus;
    this.bigImagePanel = bigImagePanel;
    setLayout(new CardLayout());
    add(thumbsScrollPanel, THUMB);
    add(bigImagePanel, BIG);
    log.info("init");
  }

  @Subscribe
  public void imageSelected(ImageSelectedEvent event) {
    log.info("==> {}", event.getAsset());
    bigImagePanel.setAsset(event.getAsset());
    switchView();
  }

  @Subscribe
  public void backToThumbsView(BackToThumbsViewEvent event) {
    switchView();
  }

  @Subscribe
  public void treeNodeSelected(TreeNodeSelectEvent event) {
    if (visible.equals(BIG)) {
      getLayout().show(this, THUMB);
      visible = THUMB;
    }
    eventBus.post(FocusThumbViewEvent.get());
    eventBus.post(FolderSelectedEvent.with(event.getAsset()));
  }

  @Override
  public CardLayout getLayout() {
    return (CardLayout) super.getLayout();
  }

  private void switchView() {
    CardLayout layout = getLayout();
    if (visible.equals(THUMB)) {
      layout.show(this, BIG);
      visible = BIG;
      eventBus.post(FocusBigImageViewEvent.get());
    } else {
      layout.show(this, THUMB);
      visible = THUMB;
      eventBus.post(FocusThumbViewEvent.get());
    }
  }

}
