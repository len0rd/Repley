package net.lenords.repley.model.sql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;

public class SqlResult {

  private List<Row> rows;
  private List<Column> columns;

  public SqlResult() {
    rows = new ArrayList<>();
    columns = new ArrayList<>();
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
      rs.first();//go back so we get the first row's data
      firstColRowInit(rs, columnNames, columnTypes);
      while (rs.next()) {
        populateNextRowCols(rs, columnNames, columnTypes);
      }

    } catch (SQLException e) {
      System.out.println("ERR::Failed to iterate query results");
      e.printStackTrace();
    }

    return this;
  }

  private void populateNextRowCols(ResultSet rs, List<String> columnNames, List<Integer> sqlTypes) {
    Row nextRow = new Row();
    for (int i = 0; i < columnNames.size(); i++) {
      Tuple nextTuple = nextRow.createTuple(rs, columnNames.get(i), sqlTypes.get(i));
      columns.get(i).addValue(nextTuple.getValue());
      nextRow.addTuple(nextTuple);
    }
    rows.add(nextRow);
  }

  private void firstColRowInit(ResultSet rs, List<String> columnNames, List<Integer> sqlTypes) {
    Row firstRow = new Row();
    for (int i = 0; i < columnNames.size(); i++) {
      firstRow.addTuple(createNewTupleAndColumn(rs, columnNames.get(i), sqlTypes.get(i)));
    }
    rows.add(firstRow);
  }

  private Tuple createNewTupleAndColumn(ResultSet rs, String columnName, int type) {
    try {
      switch (type) {
        case Types.INTEGER:
        case Types.TINYINT:
        case Types.SMALLINT:
        case Types.BIGINT:
        case Types.NUMERIC:
          columns.add(new Column<>(columnName, rs.getInt(columnName)));
          return new Tuple<>(columnName, rs.getInt(columnName));
        case Types.BOOLEAN:
          columns.add(new Column<>(columnName, rs.getBoolean(columnName)));
          return new Tuple<>(columnName, rs.getBoolean(columnName));
        case Types.DECIMAL:
        case Types.DOUBLE:
          columns.add(new Column<>(columnName, rs.getDouble(columnName)));
          return new Tuple<>(columnName, rs.getDouble(columnName));
        case Types.FLOAT:
          columns.add(new Column<>(columnName, rs.getFloat(columnName)));
          return new Tuple<>(columnName, rs.getFloat(columnName));
        case Types.NULL:
          columns.add(new Column<>(columnName, (String) null));
          return new Tuple<>(columnName, (String) null);
        default:
          columns.add(new Column<>(columnName, rs.getString(columnName)));
          return new Tuple<>(columnName, rs.getString(columnName));
      }
    } catch (SQLException se) {
      System.out.println("ERR::Couldn't create a new Tuple");
      se.printStackTrace();
    }
    return null;
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

  public List<Column> getColumns() {
    return columns;
  }

  public boolean isEmpty() {
    return rows == null || rows.isEmpty();
  }
}

