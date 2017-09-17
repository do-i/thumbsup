package com.djd.fun.thumbsup.cache;

import com.djd.fun.thumbsup.events.PreloadImageEvent;

public interface CacheHelper {

  void preloadImage(PreloadImageEvent event);
}
