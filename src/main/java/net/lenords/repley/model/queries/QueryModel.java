package net.lenords.repley.model.queries;

import java.util.List;

public class QueryModel {
  private String name;
  private QueryModelParams params;
  private List<String> data_labels;

  public QueryModel(String name) {
    this.name = name;
  }

  public QueryModel(String name, QueryModelParams params) {
    this.name = name;
    this.params = params;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public QueryModelParams getParams() {
    return params;
  }

  public void setParams(QueryModelParams params) {
    this.params = params;
  }

  public List<String> getData_labels() {
    return data_labels;
  }

  public void setData_labels(List<String> data_labels) {
    this.data_labels = data_labels;
  }

  @Override
  public String toString() {
    return "QueryModel{" +
        "name='" + name + '\'' +
        ", params=" + params.toString() +
        ", data_labels=" + data_labels.toString() +
        '}';
  }
}
