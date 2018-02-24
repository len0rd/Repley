package net.lenords.repley.model.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class Row {

  private List<Tuple> keyValues;

  public Row() {
    keyValues = new ArrayList<>();
  }

  public Row populate(ResultSet rs, List<String> columnNames, List<Integer> sqlTypes) {
    for (int i = 0; i < columnNames.size(); i++) {
      keyValues.add(addTuple(rs, columnNames.get(i), sqlTypes.get(i)));
    }
    return this;
  }

  private Tuple addTuple(ResultSet rs, String columnName, int type) {
    try {
      switch (type) {
        case Types.INTEGER:
        case Types.TINYINT:
        case Types.SMALLINT:
        case Types.BIGINT:
        case Types.NUMERIC:
          return new Tuple<>(columnName, rs.getInt(columnName));
        case Types.BOOLEAN:
          return new Tuple<>(columnName, rs.getBoolean(columnName));
        case Types.DECIMAL:
        case Types.DOUBLE:
          return new Tuple<>(columnName, rs.getDouble(columnName));
        case Types.FLOAT:
          return new Tuple<>(columnName, rs.getFloat(columnName));
        case Types.NULL:
          return new Tuple<>(columnName, (String) null);
        default:
          return new Tuple<>(columnName, rs.getString(columnName));
      }
    } catch (SQLException se) {
      System.out.println("ERR::Couldn't create a new Tuple");
      se.printStackTrace();
    }
    return null;
  }

  public List<Tuple> getTuples() {
    return keyValues;
  }

  public boolean isEmpty() {
    return keyValues == null || keyValues.isEmpty();
  }

  public JSONObject toJSONObject() {
    JSONObject out = new JSONObject();
    for (Tuple t : keyValues) {
      out.put(t.getKey(), t.getValue());
    }
    return out;
  }

  public Integer[] getValues() {
    Integer[] values = new Integer[keyValues.size()];
    System.out.println("get values");
    for (int i = 0; i < keyValues.size(); i++) {
      try {
        values[i] = (int) keyValues.get(i).getValue();
      } catch (ClassCastException cce) {
        cce.printStackTrace();
        values[i] = -1;
      }
      System.out.println("got " + values[i]);
    }
    return values;
  }

  public JSONArray valuesToArray() {
    JSONArray out = new JSONArray();
    //TODO: type checks here. values need to all be same type, right?
    for (Tuple t : keyValues) {
      out.put(t.getValue());
    }
    return out;
  }


}

