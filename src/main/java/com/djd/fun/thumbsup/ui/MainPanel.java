package com.djd.fun.thumbsup.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.djd.fun.thumbsup.annotations.LayoutBorder;
import com.djd.fun.thumbsup.util.Fonts;
import com.google.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainPanel extends JPanel {

  private static final Logger log = LoggerFactory.getLogger(MainPanel.class);

  @Inject
  public MainPanel(@LayoutBorder LayoutManager layout, JSplitPane jSplitPane) {
    setLayout(layout);
    add(jSplitPane);
    log.info("init");
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(1150, 800);
  }
}
