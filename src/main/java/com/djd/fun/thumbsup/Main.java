package com.djd.fun.thumbsup;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      Injector injector = Guice.createInjector(new ThumbsupModule());
      JFrame jFrame = injector.getInstance(JFrame.class);
      jFrame.pack();
      jFrame.setVisible(true);
    });
  }
}
