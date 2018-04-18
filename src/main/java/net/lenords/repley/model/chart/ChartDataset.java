package net.lenords.repley.model.chart;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ChartDataset {
  private String label;
  private List<Integer> data;
  private List<String> backgroundColor;
  private List<String> borderColor;
  private int borderWidth;

  public ChartDataset(String label, List<Integer> data) {
    this.label = label;
    this.data = data;
  }

  public void generateRandomColorsForDataset() {
    if (data != null && !data.isEmpty()) {
      final double GOLDEN_RATIO = 0.618033988749895f;
      backgroundColor = new ArrayList<>();
      borderColor = new ArrayList<>();
      double hue = ThreadLocalRandom.current().nextDouble(1.0f);

      for (int i = 0; i < data.size(); i++ ) {
        hue += GOLDEN_RATIO;
        ChartColor cc = new ChartColor(hue % 1, 0.2f);
        backgroundColor.add(cc.toString());
        cc.setA(1f);
        borderColor.add(cc.toString());
      }
    }
  }

  public void generateSingleColorForDataset() {
    if (data != null && !data.isEmpty()) {
      final double GOLDEN_RATIO = 0.618033988749895f;
      backgroundColor = new ArrayList<>();
      borderColor = new ArrayList<>();
      double hue = ThreadLocalRandom.current().nextDouble(1.0f);

      ChartColor cc = new ChartColor(hue % 1, 0.2f);
      backgroundColor.add(cc.toString());
      cc.setA(1f);
      borderColor.add(cc.toString());
    }
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public List<Integer> getData() {
    return data;
  }

  public void setData(List<Integer> data) {
    this.data = data;
  }

  public int getBorderWidth() {
    return borderWidth;
  }

  public void setBorderWidth(int borderWidth) {
    this.borderWidth = borderWidth;
  }

  public List<String> getBorderColor() {
    return borderColor;
  }

  public void setBorderColor(List<String> borderColor) {
    this.borderColor = borderColor;
  }

  public List<String> getBackgroundColor() {
    return backgroundColor;
  }

  public void setBackgroundColor(List<String> backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  @Override
  public String toString() {
    StringBuilder out = new StringBuilder("ChartDataset{ label=").append(label).append(", \n data=");
    data.forEach(val -> out.append(val).append(", "));
    out.append("}");
    return out.toString();
  }
}
