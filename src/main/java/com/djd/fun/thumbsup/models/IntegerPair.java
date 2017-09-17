package com.djd.fun.thumbsup.models;

import java.util.Objects;

public class IntegerPair {
  protected final int value1;
  protected final int value2;

  public IntegerPair(int value1, int value2) {
    this.value1 = value1;
    this.value2 = value2;
  }

  public int getValue1() {
    return value1;
  }

  public int getValue2() {
    return value2;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof IntegerPair)) return false;
    IntegerPair that = (IntegerPair)o;
    return value1 == that.value1 &&
        value2 == that.value2;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value1, value2);
  }

  @Override
  public String toString() {
    return String.format("(%d,%d)", value1, value2);
  }
}
