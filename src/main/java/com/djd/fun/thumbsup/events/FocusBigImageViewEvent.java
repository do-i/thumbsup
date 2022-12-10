package com.djd.fun.thumbsup.events;

public class FocusBigImageViewEvent {

  private static final FocusBigImageViewEvent EVENT = new FocusBigImageViewEvent();

  private FocusBigImageViewEvent() {
    // no instance
  }

  public static final FocusBigImageViewEvent get() {
    return EVENT;
  }
}
