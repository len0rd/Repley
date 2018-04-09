package net.lenords.repley.model.queries;

import java.util.List;

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

  @Override
  public String toString() {
    return "QueryData{" +
        "title='" + title + '\'' +
        ", dataset=" + dataset +
        '}';
  }
}
