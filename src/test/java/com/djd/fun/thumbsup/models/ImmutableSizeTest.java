package com.djd.fun.thumbsup.models;

import com.google.common.testing.EqualsTester;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class ImmutableSizeTest {

  private static final ImmutableSize size = ImmutableSize.of(10, 30);

  @Test
  public void getWidth() throws Exception {
    assertThat(size.getWidth()).isEqualTo(10);
  }

  @Test
  public void getHeight() throws Exception {
    assertThat(size.getHeight()).isEqualTo(30);
  }

  @Test
  public void equalities() {
    new EqualsTester().addEqualityGroup(size, ImmutableSize.of(10, 30))
        .addEqualityGroup(ImmutableSize.of(10, 20))
        .addEqualityGroup(ImmutableSize.of(20, 30))
        .testEquals();
  }
}