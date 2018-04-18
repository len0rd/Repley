package net.lenords.repley.model.queries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.lenords.repley.model.chart.ChartData;
import net.lenords.repley.model.chart.ChartDataset;
import net.lenords.repley.model.chart.ChartType;
import net.lenords.repley.model.sql.SqlResult;

public class QueryData {
  private String title;
  private List<QueryDataset> dataset;

  public QueryData(String title, List<QueryDataset> dataset) {
    this.title = title;
    this.dataset = dataset;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<QueryDataset> getDataset() {
    return dataset;
  }

  public void setDataset(List<QueryDataset> dataset) {
    this.dataset = dataset;
  }

  public ChartData generateChartDataFromResult(SqlResult result, ChartType ctype) {
    List<ChartDataset> datasets = new ArrayList<>();
    for (QueryDataset qd : dataset) {
      ChartDataset cds = new ChartDataset(qd.getName(), qd.getFromResult(result));
      cds.setBorderWidth(2);
      datasets.add(cds);
    }

    ChartData cd = new ChartData(getTitlesFromResult(result), datasets);
    //this boolean is dependent on the type of chart we're working with
    if (ctype.generateMultiColorDataset()) {
      for (ChartDataset cds : cd.getDatasets()) {
        cds.generateRandomColorsForDataset();
      }
    } else {
      cd.generateRandomColorsForData();
    }

    return cd;
  }

  public List<String> getTitlesFromResult(SqlResult result) {
    String[] fromDir = title.split("[.]");

    if (fromDir.length > 1) {

      if (fromDir[0].contains("column")) {
        try {
          Integer clmnNum = Integer.parseInt(fromDir[1]);
          return result.getColumns().get(clmnNum).getValues();
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else if (fromDir[0].contains("row")) {
        if (fromDir[1].equalsIgnoreCase("names")) {
          return Arrays.asList(result.getRows().get(0).getKeys());
        } else {
          try {
            Integer rowNum = Integer.parseInt(fromDir[1]);
            return result.getRows().get(rowNum).getTuples().stream()
                .map(tuple -> (String) tuple.getValue()).collect(Collectors.toList());
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return "QueryData{" +
        "title='" + title + '\'' +
        ", dataset=" + dataset +
        '}';
  }
}
