package com.djd.fun.thumbsup.models;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.Test;

public class ImmutableSizeTest {

  private static final ImmutableSize size = ImmutableSize.of(10, 30);

  @Test
  void getWidth() throws Exception {
    assertThat(size.getWidth()).isEqualTo(10);
  }

  @Test
  void getHeight() throws Exception {
    assertThat(size.getHeight()).isEqualTo(30);
  }

  @Test
  void equalities() {
    new EqualsTester().addEqualityGroup(size, ImmutableSize.of(10, 30))
        .addEqualityGroup(ImmutableSize.of(10, 20))
        .addEqualityGroup(ImmutableSize.of(20, 30))
        .testEquals();
  }
}