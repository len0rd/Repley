package net.lenords.repley.model.sql;

import java.util.ArrayList;
import java.util.List;

public class Column<T> {
  private String key;
  private List<T> values;

  public Column(String key, T firstValue) {
    this.key = key;
    this.values = new ArrayList<>();
    values.add(firstValue);
  }

  public void addValue(T value) {
    this.values.add(value);
  }

  public List<T> getValues() {
    return values;
  }

  public String getKey() {
    return key;
  }
}
