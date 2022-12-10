package com.djd.fun.thumbsup.util;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.UIManager;

public class Fonts {

  public static final Font DROID_SANS_13 = new Font("Droid Sans", Font.PLAIN, 13);

  public static void configure() {
    UIManager.put("Label.font", DROID_SANS_13);
  }

  public static void enableSmoothFont(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
        RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
  }
}
