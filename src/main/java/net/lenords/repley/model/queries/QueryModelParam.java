package net.lenords.repley.model.queries;

import java.util.List;

/**
 * Parameter for the query.
 *
 * <p>Here mappings map input values from a web server request to values specified in the
 * query_conf.json file. If no mappings are specified (Dangerous!), then the raw value from the
 * server will be applied to the sql query.
 *
 * @author len0rd
 */
public class QueryModelParam {
  private String name, defaultValue;
  private List<Mapping> mappings;

  public QueryModelParam(String name, String defaultValue, List<Mapping> mappings) {
    this.name = name;
    this.defaultValue = defaultValue;
    this.mappings = mappings;
  }

  public QueryModelParam(String name, List<Mapping> mappings) {
    this.name = name;
    this.defaultValue = null;
    this.mappings = mappings;
  }

  public QueryModelParam(String name) {
    this.name = name;
    this.defaultValue = null;
  }

  public MappedValue mapToValue(String inputValue) {
    String ogInput = inputValue;
    if (hasMapping()) {
      for (Mapping mapping : mappings) {
        if (inputValue.matches(mapping.getInput())) {
          inputValue = mapping.getMapto();
          inputValue = inputValue.replace("~@input@~", ogInput);
          MappedValue mval = new MappedValue(inputValue);
          if (inputValue.contains("~@ps_input@~")) {
            inputValue = inputValue.replace("~@ps_input@~", "?");
            mval.setPrototypeValue(inputValue);
            mval.setValue(ogInput);
            mval.setUsePS(true);
          }
          return mval;
        }
      }

      return new MappedValue(getDefaultValue());
    }
    return new MappedValue(inputValue);
  }

  public String getDefaultValue() {
    return defaultValue == null ? "" : defaultValue;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  public boolean hasMapping() {
    return mappings != null && !mappings.isEmpty();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Mapping> getMappings() {
    return mappings;
  }

  public void setMappings(List<Mapping> mappings) {
    this.mappings = mappings;
  }
}
