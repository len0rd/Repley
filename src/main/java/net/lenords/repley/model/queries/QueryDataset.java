package net.lenords.repley.model.queries;

import java.util.Arrays;
import java.util.List;
import net.lenords.repley.model.sql.SqlResult;

public class QueryDataset {
  private String name, from;

  public QueryDataset(String name, String from) {
    this.name = name;
    this.from = from.toLowerCase();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public List<Integer> getFromResult(SqlResult result) {
    String[] fromDir = from.split("[.]");

    if (fromDir.length > 1) {

      if (fromDir[0].contains("column")) {
        try {
          Integer clmnNum = Integer.parseInt(fromDir[1]);
          return result.getColumns().get(clmnNum).getValues();
        } catch (Exception e) {
          e.printStackTrace();
          return null;
        }
      } else if (fromDir[0].contains("row")) {
        try {
          Integer rowNum = Integer.parseInt(fromDir[1]);
          return Arrays.asList(result.getRows().get(rowNum).getValues());
        } catch (Exception e) {
          e.printStackTrace();
          return null;
        }
      }
    }
    return null;
  }

}
