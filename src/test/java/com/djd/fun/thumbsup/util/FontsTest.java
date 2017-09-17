package com.djd.fun.thumbsup.util;

import java.awt.GraphicsEnvironment;
import java.util.stream.Stream;

import com.google.common.collect.Streams;

import org.junit.Ignore;
import org.junit.Test;

public class FontsTest {

  @Ignore("This is a convenient tool and not a real test")
  @Test
  public void listFontNames() {
    Stream.of(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames())
        .forEach(System.out::println);
  }
}