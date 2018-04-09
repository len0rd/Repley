package net.lenords.repley.model.queries;

import java.util.ArrayList;
import java.util.List;

public class QueryModelContainer {
  private List<QueryModel> queryModels;

  public QueryModelContainer() {
    this.queryModels = new ArrayList<>();
  }

  public QueryModelContainer(List<QueryModel> queryModels) {
    this.queryModels = queryModels;
  }

  public QueryModel getQueryByName(String name) {
    return queryModels.stream().filter(model -> model.getName().equals(name)).findFirst().orElse(null);
  }

  public void addQueryModel(QueryModel queryModel) {
    this.queryModels.add(queryModel);
  }

  public List<QueryModel> getQueryModels() {
    return queryModels;
  }

  public void setQueryModels(List<QueryModel> queryModels) {
    this.queryModels = queryModels;
  }
}
