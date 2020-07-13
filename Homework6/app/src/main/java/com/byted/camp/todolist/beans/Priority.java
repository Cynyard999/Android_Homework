package com.byted.camp.todolist.beans;

import android.graphics.Color;

public enum  Priority {
  High(2, Color.parseColor("#FFB6C1")),
  Medium(1, Color.parseColor("#FFF8DC")),
  Low(0, Color.WHITE);

  public final int value;
  public final int color;

  Priority(int intValue, int color) {
    this.value = intValue;
    this.color = color;
  }

  public static Priority getPriority(int v) {
    for (Priority priority : Priority.values()) {
      if (priority.value == v) {
        return priority;
      }
    }

    return Priority.Low ;
  }

}
