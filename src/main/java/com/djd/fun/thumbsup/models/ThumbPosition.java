package com.djd.fun.thumbsup.models;

import java.awt.Component;
import java.util.HashMap;

import javax.annotation.Nullable;

import com.djd.fun.thumbsup.ui.ThumbViewPanel;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mutable class maintain current selected thumb
 * Also, provides a helper method to translate x,y thumbViewPanel position to row,col index system
 * This allows arrow key navigation
 */
public class ThumbPosition {

  private static final Logger log = LoggerFactory.getLogger(ThumbPosition.class);
  // TODO find out where this off by 2px is from.
  private static final int UNKNOWN_DELTA = 2;
  // TODO this number should be mutated based on thumbViewSize
  private static final IntegerPair WRAP_LAYOUT_GAP = new IntegerPair(7, 2);
  private HashMap<IntegerPair, Component> positionToCompo = Maps.newHashMap();
  private int maxRowIndex;
  private int maxColIndex;
  private int curRowIndex;
  private int curColIndex;

  /**
   * check whether there is a component for the specified position.
   *
   * @param row
   * @param col
   * @return {@code true} if component exists at {@code row} and {@code col}
   */
  private boolean isValidMove(int row, int col) {
    return positionToCompo.containsKey(new IntegerPair(row, col));
  }

  public void add(ImmutableSize imageViewSize, Component compo) {
    if (compo instanceof ThumbViewPanel) {
      int col = (compo.getX() - WRAP_LAYOUT_GAP.getValue1()) / imageViewSize.getWidth();
      int row = (compo.getY() - WRAP_LAYOUT_GAP.getValue2()) / (imageViewSize.getHeight() + UNKNOWN_DELTA);
      maxRowIndex = Math.max(maxRowIndex, row);
      maxColIndex = Math.max(maxColIndex, col);
      IntegerPair position = new IntegerPair(row, col);
      positionToCompo.put(position, compo);
    } else {
      throw new IllegalArgumentException(
          "Invalid component type. Expected ThumbViewPanel but " +
              compo.getClass());
    }
  }

  public @Nullable Component getComponent() {
    return positionToCompo.get(new IntegerPair(curRowIndex, curColIndex));
  }

  /**
   * Empty all previously added {@link Component}s
   * Set 0 to {@link this#maxRowIndex}
   * Set 0 to {@link this#maxColIndex}
   * Set 0 to {@link this#curRowIndex}
   * Set 0 to {@link this#curColIndex}
   */
  public void reset() {
    maxRowIndex = 0;
    maxColIndex = 0;
    curRowIndex = 0;
    curColIndex = 0;
    positionToCompo.clear();
  }

  /*
   * Mutation Controls
   */

  public void moveUp() {
    if (curRowIndex > 0 && isValidMove(curRowIndex - 1, curColIndex)) {
      curRowIndex--;
    }
  }

  public void moveDown() {
    if (curRowIndex < maxRowIndex && isValidMove(curRowIndex + 1, curColIndex)) {
      curRowIndex++;
    }
  }

  public void moveLeft() {
    if (curColIndex > 0 && isValidMove(curRowIndex, curColIndex - 1)) {
      curColIndex--;
    }
  }

  public void moveRight() {
    if (curColIndex < maxColIndex && isValidMove(curRowIndex, curColIndex + 1))
      curColIndex++;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("curRowIndex", curRowIndex)
        .add("curColIndex", curColIndex)
        .add("maxRowIndex", maxRowIndex)
        .add("maxColIndex", maxColIndex)
        .add("positionToCompo", positionToCompo)
        .toString();
  }

  public void printDebugInfo() {
    log.info(toString());
  }

}
