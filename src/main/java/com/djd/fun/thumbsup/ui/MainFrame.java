package com.djd.fun.thumbsup.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.djd.fun.thumbsup.util.Fonts;
import com.google.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainFrame extends JFrame {

  private static final Logger log = LoggerFactory.getLogger(MainFrame.class);
  private static final String FRAME_TITLE = "Koala";

  @Inject
  public MainFrame(JPanel mainPanel) {
    super(FRAME_TITLE);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    add(mainPanel);
    Fonts.configure();
    log.info("init");
  }
}
