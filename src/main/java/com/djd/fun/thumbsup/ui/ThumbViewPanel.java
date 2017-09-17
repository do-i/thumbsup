package com.djd.fun.thumbsup.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.event.MouseInputAdapter;

import com.djd.fun.thumbsup.annotations.BorderUnselected;
import com.djd.fun.thumbsup.events.FolderSelectedEvent;
import com.djd.fun.thumbsup.events.ImageSelectedEvent;
import com.djd.fun.thumbsup.models.Asset;
import com.djd.fun.thumbsup.models.ThumbViewSize;
import com.djd.fun.thumbsup.util.Fonts;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThumbViewPanel extends JPanel {
  private static final Logger log = LoggerFactory.getLogger(ThumbViewPanel.class);
  private final EventBus eventBus;
  private final ThumbViewSize thumbViewSize;
  private final Asset asset;

  @Inject
  public ThumbViewPanel(EventBus eventBus,
      ThumbViewSize thumbViewSize,
      @BorderUnselected Border border,
      @Assisted ThumbPanel thumbPanel,
      @Assisted Asset asset) {
    this.eventBus = eventBus;
    this.thumbViewSize = thumbViewSize;
    this.asset = asset;
    setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
    add(thumbPanel);
    add(new JLabel(asset.getFile().getName()));

    addMouseListener(new MouseInput());
    setBorder(border);
    log.info("init");
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Fonts.enableSmoothFont(g);
  }

  @Override
  public Dimension getPreferredSize() {
    return thumbViewSize.getImageViewDimension();
  }

  public Asset getAsset() {
    return asset;
  }

  private class MouseInput extends MouseInputAdapter {

    @Override
    public void mouseClicked(MouseEvent event) {
      if (asset.getFileType() == Asset.FileType.IMAGE) {
        eventBus.post(ImageSelectedEvent.with(asset));
      } else {
        eventBus.post(FolderSelectedEvent.with(asset));
      }
    }
  }
}
