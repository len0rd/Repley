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

  public ChartColor(double hue, float a) {
    this.a = a;
    setRGBFromHSV(hue, 0.5, 0.95);
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
    setRGBFromHSV(ThreadLocalRandom.current().nextDouble(1.0f), 0.5, 0.95);
  }

  /**
   * Based on this:
   * https://martin.ankerl.com/2009/12/09/how-to-create-random-colors-programmatically/
   * Which is based on this:
   * https://en.wikipedia.org/wiki/HSL_and_HSV#Converting_to_RGB
   * And this:
   * http://en.wikipedia.org/wiki/Golden_ratio
   * @param hue         HSV hue value
   * @param saturation  HSV saturation value
   * @param value       HSV value value
   */
  private void setRGBFromHSV(double hue, double saturation, double value) {
    int h_int = (int) (hue*6);
    double f = hue * 6 - h_int;
    double p = value * (1 - saturation);
    double q = value * (1 - f * saturation);
    double t = value * (1 - (1 - f) * saturation);

    double r_doub = 0, g_doub = 0, b_doub = 0;
    switch (h_int) {
      case 0:
        r_doub = value;
        g_doub = t;
        b_doub = p;
        break;
      case 1:
        r_doub = q;
        g_doub = value;
        b_doub = p;
        break;
      case 2:
        r_doub = p;
        g_doub = value;
        b_doub = t;
        break;
      case 3:
        r_doub = p;
        g_doub = q;
        b_doub = value;
        break;
      case 4:
        r_doub = t;
        g_doub = p;
        b_doub = value;
        break;
      case 5:
        r_doub = value;
        g_doub = p;
        b_doub = q;
        break;
    }

    this.r = (int) (r_doub*256);
    this.g = (int) (g_doub*256);
    this.b = (int) (b_doub*256);
  }

}