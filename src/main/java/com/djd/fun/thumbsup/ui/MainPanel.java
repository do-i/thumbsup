package com.djd.fun.thumbsup.ui;

import com.djd.fun.thumbsup.annotations.LayoutBorder;
import com.google.inject.Inject;
import java.awt.Dimension;
import java.awt.LayoutManager;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
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
