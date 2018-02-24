package net.lenords.repley.model.chart;

public class Chart {
  private String type;
  private ChartData data;

  public Chart(String type, ChartData data) {
    this.type = type;
    this.data = data;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public ChartData getData() {
    return data;
  }

  public void setData(ChartData data) {
    this.data = data;
  }

}
