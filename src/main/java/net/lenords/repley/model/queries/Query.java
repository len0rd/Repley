package net.lenords.repley.model.queries;

import java.util.List;

public class Query {
  private String sql;
  private List<Mapping> mappings;

  public Query(String sql) {
    this.sql = sql;
  }

  public String getSql() {
    return sql;
  }

  public void setSql(String sql) {
    this.sql = sql;
  }

  public List<Mapping> getMappings() {
    return mappings;
  }

  public void setMappings(List<Mapping> mappings) {
    this.mappings = mappings;
  }
}
