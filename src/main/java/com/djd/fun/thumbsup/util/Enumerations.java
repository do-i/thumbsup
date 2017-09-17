package com.djd.fun.thumbsup.util;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Enumerations {

  // https://stackoverflow.com/questions/23261803/iterate-an-enumeration-in-java-8/23276455#23276455
  public static <T> Stream<T> stream(Enumeration<T> e) {
    return StreamSupport.stream(
        Spliterators.spliteratorUnknownSize(
            new Iterator<T>() {
              public T next() {
                return e.nextElement();
              }

              public boolean hasNext() {
                return e.hasMoreElements();
              }
            },
            Spliterator.ORDERED), false);
  }
}
