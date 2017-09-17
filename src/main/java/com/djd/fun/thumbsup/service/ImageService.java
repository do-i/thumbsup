package com.djd.fun.thumbsup.service;

import java.awt.Image;
import java.awt.image.BufferedImage;

import com.djd.fun.thumbsup.models.Asset;

public interface ImageService {
  Image createThumbnailImage(Asset asset);
}
