package net.lenords.repley.model.chart;

public enum ChartType {
  LINE {
    @Override
    public boolean generateMultiColorDataset() {
      return false;
    }
  },
  PIE {
    @Override
    public boolean generateMultiColorDataset() {
      return true;
    }
  },
  BAR {
    @Override
    public boolean generateMultiColorDataset() {
      return false;
    }
  },
  DOUGHNUT {
    @Override
    public boolean generateMultiColorDataset() {
      return true;
    }
  },
  RADAR {
    @Override
    public boolean generateMultiColorDataset() {
      return false;
    }
  },
  POLAR_AREA {
    @Override
    public boolean generateMultiColorDataset() {
      return false;
    }

    @Override
    public String getTypeString() {
      return "polarArea";
    }
  },
  HORIZONTAL_BAR {
    @Override
    public boolean generateMultiColorDataset() {
      return false;
    }

    @Override
    public String getTypeString() {
      return "horizontalBar";
    }
  };


  /**
   * Whether or not there should be multiple colors for each dataset.
   * For example, in a Pie chart you want multiple colors in a dataset, since you want each
   * slice of the pie to be a different color. However, with a line chart, you want a dataset to
   * be a single color (otherwise chartjs has issues and cant render some overlay stuff)
   * @return whether this chart type should have multiple colors generated on a per dataset basis
   */
  public abstract boolean generateMultiColorDataset();

  public String getTypeString() {
    return this.name().toLowerCase();
  }

  public static ChartType getTypeFromStr(String type) {
    type = type.toLowerCase();
    switch (type) {
      case "pie":
        return PIE;
      case "bar":
        return BAR;
      case "doughnut":
        return DOUGHNUT;
      case "radar":
        return RADAR;
      case "polararea":
        return POLAR_AREA;
      case "horizonalbar":
        return HORIZONTAL_BAR;
      default:
        return LINE;
    }
  }

}
