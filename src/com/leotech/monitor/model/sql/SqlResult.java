package com.leotech.monitor.model.sql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;

public class SqlResult {

  private List<Row> rows;

  public SqlResult() {
    rows = new ArrayList<>();
  }

  public SqlResult populate(ResultSet rs, ResultSetMetaData rsmd) {

    try {
      List<String> columnNames = new ArrayList<>();
      List<Integer> columnTypes = new ArrayList<>();
      if (rs.first()) {
        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
          columnNames.add(rsmd.getColumnName(i));
          columnTypes.add(rsmd.getColumnType(i));
        }
      }
      rs.beforeFirst();//go back so we get the first row's data
      while (rs.next()) {
        rows.add(new Row().populate(rs, columnNames, columnTypes));
      }
    } catch (SQLException e) {
      System.out.println("ERR::Failed to iterate query results");
      e.printStackTrace();
    }

    return this;
  }

  public JSONArray toJSONArray() {
    if (isEmpty()) {
      return null;
    }

    JSONArray out = new JSONArray();
    for (Row r : rows) {
      out.put(r.toJSONObject());
    }

    return out;
  }

  public List<Row> getRows() {
    return isEmpty() ? null : rows;
  }

  public boolean isEmpty() {
    return rows == null || rows.isEmpty();
  }
}
