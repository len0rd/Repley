package net.lenords.repley.model.chart;

public enum ChartType {
  LINE {},
  PIE {},
  BAR {},
  DOUGHNUT {},
  RADAR {},
  POLAR_AREA {
    @Override
    public String getTypeString() {
      return "polarArea";
    }
  },
  HORIZONTAL_BAR {
    @Override
    public String getTypeString() {
      return "horizontalBar";
    }
  };


  public String getTypeString() {
    return this.name().toLowerCase();
  }

}
