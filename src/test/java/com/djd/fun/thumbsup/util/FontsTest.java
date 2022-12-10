package com.djd.fun.thumbsup.util;

import java.awt.GraphicsEnvironment;
import java.util.stream.Stream;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class FontsTest {

  @Disabled("This is a convenient tool and not a real test")
  @Test
  public void listFontNames() {
    Stream.of(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames())
        .forEach(System.out::println);
  }
}