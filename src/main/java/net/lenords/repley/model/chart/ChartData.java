package net.lenords.repley.model.chart;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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

  public void generateRandomColorsForData() {
    if (datasets != null && !datasets.isEmpty()) {
      final double GOLDEN_RATIO = 0.618033988749895f;
      double hue = ThreadLocalRandom.current().nextDouble(1.0f);

      for (ChartDataset dataset : datasets) {
        hue += GOLDEN_RATIO;
        ChartColor cc = new ChartColor(hue % 1, 0.2f);
        dataset.setBackgroundColor(Collections.singletonList(cc.toString()));
        cc.setA(1f);
        dataset.setBorderColor(Collections.singletonList(cc.toString()));
      }
    }
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
