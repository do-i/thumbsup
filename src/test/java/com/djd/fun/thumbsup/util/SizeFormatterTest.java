package com.djd.fun.thumbsup.util;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Test;

public class SizeFormatterTest {

  @Test
  public void humanReadableByteCount_0() {
    assertThat(SizeFormatter.humanReadableByteCount(0)).isEqualTo("0 B");
  }

  @Test
  public void humanReadableByteCount_27() {
    assertThat(SizeFormatter.humanReadableByteCount(27)).isEqualTo("27 B");
  }

  @Test
  public void humanReadableByteCount_888() {
    assertThat(SizeFormatter.humanReadableByteCount(888)).isEqualTo("888 B");
  }

  @Test
  public void humanReadableByteCount_1000() {
    assertThat(SizeFormatter.humanReadableByteCount(1000)).isEqualTo("1.0 kB");
  }

  @Test
  public void humanReadableByteCount_1023() {
    assertThat(SizeFormatter.humanReadableByteCount(1023)).isEqualTo("1.0 kB");
  }

  @Test
  public void humanReadableByteCount_1024() {
    assertThat(SizeFormatter.humanReadableByteCount(1024)).isEqualTo("1.0 kB");
  }

  @Test
  public void humanReadableByteCount_1728() {
    assertThat(SizeFormatter.humanReadableByteCount(1728)).isEqualTo("1.7 kB");
  }

  @Test
  public void humanReadableByteCount_1728378() {
    assertThat(SizeFormatter.humanReadableByteCount(1728378)).isEqualTo("1.7 MB");
  }

  @Test
  public void humanReadableByteCount_1855425871872() {
    assertThat(SizeFormatter.humanReadableByteCount(1855425871872L)).isEqualTo("1.9 TB");
  }

  @Test
  public void humanReadableByteCount_MAX_LONG() {
    assertThat(SizeFormatter.humanReadableByteCount(Long.MAX_VALUE)).isEqualTo("9.2 EB");
  }

}