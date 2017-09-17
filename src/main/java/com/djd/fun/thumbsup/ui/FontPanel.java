package com.djd.fun.thumbsup.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.util.stream.Stream;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.djd.fun.thumbsup.annotations.PanelBackgroundColor;
import com.djd.fun.thumbsup.annotations.PanelForegroundColor;
import com.djd.fun.thumbsup.util.Fonts;
import com.google.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FontPanel extends JPanel {

  private static final Logger log = LoggerFactory.getLogger(FontPanel.class);
  private final Color fgColor;
  private GridBagConstraints constraints = new GridBagConstraints();

  @Inject
  public FontPanel(@PanelBackgroundColor Color bgColor, @PanelForegroundColor Color fgColor) {
    this.fgColor = fgColor;
    setLayout(new GridBagLayout());
    constraints.gridwidth = 1;
    constraints.gridy = 0;
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.insets = new Insets(2, 2, 2, 2);
    setBackground(bgColor);
    Stream.of(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames())
        .forEach(this::displayFont);
    log.info("init");
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Fonts.enableSmoothFont(g);
  }

  private void displayFont(String fontName) {
    JLabel label = new JLabel();
    label.setText("1234567890ABCDEFGHIJKLMNabcdefghijklm_.-" + fontName);
    label.setFont(new Font(fontName, Font.PLAIN, 13));
    label.setForeground(fgColor);
    add(label, constraints);
    constraints.gridy++;
  }

}
