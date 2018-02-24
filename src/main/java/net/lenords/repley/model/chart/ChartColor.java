package net.lenords.repley.model.chart;

import java.util.concurrent.ThreadLocalRandom;

public class ChartColor {
  private static final int COLOR_MIN = 0, COLOR_MAX = 255;
  private int r, g, b;
  private float a;

  public ChartColor(float a) {
    this.a = a;
    generateRandomColor();
  }

  public ChartColor(int r, int g, int b, float a) {
    this.r = r;
    this.g = g;
    this.b = b;
    this.a = a;
  }

  @Override
  public String toString() {
    return "rgba(" + r + ", " + g + ", " + b + ", " + a + ")";
  }

  public void setA(float a) {
    this.a = a;
  }

  private void generateRandomColor() {
    this.r = ThreadLocalRandom.current().nextInt(COLOR_MIN, COLOR_MAX + 1);
    this.g = ThreadLocalRandom.current().nextInt(COLOR_MIN, COLOR_MAX + 1);
    this.b = ThreadLocalRandom.current().nextInt(COLOR_MIN, COLOR_MAX + 1);
  }

}
