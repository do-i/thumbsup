package com.djd.fun.thumbsup.util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageUtil {

  private static final Logger log = LoggerFactory.getLogger(ImageUtil.class);

  public static void dumpImageSize(BufferedImage bufferedImage) {
    int[] imageInts = ((DataBufferInt)bufferedImage.getData().getDataBuffer()).getData();
    log.info("image size: {}", SizeFormatter.humanReadableByteCount(imageInts.length * 4));
  }
}
