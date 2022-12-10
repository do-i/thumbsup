package com.djd.fun.thumbsup.events;

public class FocusThumbViewEvent {

  private static final FocusThumbViewEvent EVENT = new FocusThumbViewEvent();

  private FocusThumbViewEvent() {
    // no instance
  }

  public static final FocusThumbViewEvent get() {
    return EVENT;
  }
}
