package net.lenords.repley.model.chart;

import java.util.List;

public class ChartData {
  private List<String> labels;
  private List<ChartDataset> datasets;

  public ChartData(List<String> labels) {
    this.labels = labels;
  }

  public ChartData(List<String> labels,
      List<ChartDataset> datasets) {
    this.labels = labels;
    this.datasets = datasets;
  }

  public void addDatasset(ChartDataset cds) {
    datasets.add(cds);
  }

  public List<String> getLabels() {
    return labels;
  }

  public void setLabels(List<String> labels) {
    this.labels = labels;
  }

  public List<ChartDataset> getDatasets() {
    return datasets;
  }

  public void setDatasets(List<ChartDataset> datasets) {
    this.datasets = datasets;
  }
}
