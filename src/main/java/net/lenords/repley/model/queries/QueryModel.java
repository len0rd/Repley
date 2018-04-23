package net.lenords.repley.model.queries;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.lenords.repley.datamanager.sql.Accessor;
import net.lenords.repley.model.chart.Chart;
import net.lenords.repley.model.chart.ChartType;
import net.lenords.repley.model.sql.SqlResult;

public class QueryModel {
  private String name, type;
  private QueryModelParamContainer params;
  private Query query;
  private QueryData data;

  public QueryModel(
      String name, String type, QueryModelParamContainer params, Query query, QueryData data) {
    this.name = name;
    this.type = type;
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

  public SqlResult executeQueryFromParams(Accessor accessor, HttpServletRequest request) {
    List<Object> psVariables = new ArrayList<>();

    String query = this.query.getSql();
    if (this.params != null) {
      for (QueryModelParam param : params.getRequired()) {
        String paramName = param.getName();
        if (request.getParameter(paramName) != null) {
          MappedValue mval = param.mapToValue(request.getParameter(paramName));
          if (mval.usePS()) {
            query = query.replace("~@" + paramName + "@~", mval.getPrototypeValue());
            psVariables.add(mval.getValue());
          } else {
            query = query.replace("~@" + paramName + "@~", mval.getValue());
          }
        }
      }

      if (params.getOptional() != null) {
        for (QueryModelParam param : params.getOptional()) {
          String paramName = param.getName();
          if (request.getParameter(paramName) != null) {
            MappedValue mval = param.mapToValue(request.getParameter(paramName));
            if (mval.usePS()) {
              query = query.replace("~@" + paramName + "@~", mval.getPrototypeValue());
              psVariables.add(mval.getValue());
            } else {
              query = query.replace("~@" + paramName + "@~", mval.getValue());
            }
          } else {
            query = query.replace("~@" + paramName + "@~", param.getDefaultValue());
          }
        }
      }
    }

    System.out.println("Generated Query::" + query);
    return accessor.getQueryResult(query, psVariables);
  }

  public Chart generateChartFromResult(SqlResult result) {
    ChartType ctype = ChartType.getTypeFromStr(type);
    return new Chart(ctype, data.generateChartDataFromResult(result, ctype));
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
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
    return "QueryModel{"
        + "name='"
        + name
        + '\''
        + ", params="
        + params
        + ", query="
        + query
        + ", data="
        + data
        + '}';
  }
}
