package net.lenords.repley.model.queries;

import javax.servlet.http.HttpServletRequest;

public class QueryModel {
  private String name;
  private QueryModelParamContainer params;
  private Query query;
  private QueryData data;

  public QueryModel(String name, QueryModelParamContainer params,
      Query query, QueryData data) {
    this.name = name;
    this.params = params;
    this.query = query;
    this.data = data;
  }

  public QueryModel(String name) {
    this.name = name;
  }

  public QueryModel(String name, QueryModelParamContainer params) {
    this.name = name;
    this.params = params;
  }

  public String generateQueryFromParams(HttpServletRequest request) {
    String query = this.query.getSql();
    if (this.params != null) {
      query = params.getAll().stream().filter(param -> request.getParameter(param) != null)
          .reduce(query, (combinedQuery, param) -> combinedQuery
              .replace("%" + param, request.getParameter(param)));
    }

    return query;
  }

  public Query getQuery() {
    return query;
  }

  public void setQuery(Query query) {
    this.query = query;
  }

  public QueryData getData() {
    return data;
  }

  public void setData(QueryData data) {
    this.data = data;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public QueryModelParamContainer getParams() {
    return params;
  }

  public void setParams(QueryModelParamContainer params) {
    this.params = params;
  }

  @Override
  public String toString() {
    return "QueryModel{" +
        "name='" + name + '\'' +
        ", params=" + params +
        ", query=" + query +
        ", data=" + data +
        '}';
  }
}
