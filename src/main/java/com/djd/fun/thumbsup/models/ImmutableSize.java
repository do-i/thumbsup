package com.djd.fun.thumbsup.models;

public class ImmutableSize extends IntegerPair {

  public ImmutableSize(int width, int height) {
    super(width, height);
  }

  public int getWidth() {
    return value1;
  }

  public int getHeight() {
    return value2;
  }

  public static ImmutableSize of(int width, int height) {
    return new ImmutableSize(width, height);
  }

}
