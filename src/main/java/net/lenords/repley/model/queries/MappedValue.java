package net.lenords.repley.model.queries;

public class MappedValue {
  private String prototypeValue, value;
  private boolean usePS;

  public MappedValue(String prototypeValue, String value, boolean usePS) {
    this.prototypeValue = prototypeValue;
    this.value = value;
    this.usePS = usePS;
  }

  public MappedValue(String prototypeValue, String value) {
    this.prototypeValue = prototypeValue;
    this.value = value;
    this.usePS = false;
  }

  public MappedValue(String value) {
    this.value = value;
    this.usePS = false;
  }

  public String getPrototypeValue() {
    return prototypeValue;
  }

  public void setPrototypeValue(String prototypeValue) {
    this.prototypeValue = prototypeValue;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public boolean usePS() {
    return usePS;
  }

  public void setUsePS(boolean usePS) {
    this.usePS = usePS;
  }
}
