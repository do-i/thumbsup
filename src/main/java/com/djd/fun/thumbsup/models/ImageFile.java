package com.djd.fun.thumbsup.models;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Wrapper of byte[] represents a compressed image file (jpg or png). This class is immutable
 */
public class ImageFile {

  private final byte[] image;

  public ImageFile(byte[] image) {
    this.image = Arrays.copyOf(image, image.length);
  }

  public byte[] getImage() {
    return Arrays.copyOf(image, image.length);
  }

  public int size() {
    return image.length;
  }

  public InputStream getInputStream() {
    return new ByteArrayInputStream(getImage());
  }
}
