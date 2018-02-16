package net.lenords.reporter.model.sql;

public class Tuple<T> {

  private String key;
  private T value;

  public Tuple(String key, T value) {
    this.key = key;
    this.value = value;
  }

  public String getKey() {
    return key;
  }

  public T getValue() {
    return value;
  }

}
