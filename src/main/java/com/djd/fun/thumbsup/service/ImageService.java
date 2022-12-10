package com.djd.fun.thumbsup.service;

import com.djd.fun.thumbsup.models.Asset;
import java.awt.Image;

public interface ImageService {

  Image createThumbnailImage(Asset asset);
}
