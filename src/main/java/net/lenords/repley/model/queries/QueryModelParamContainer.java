package net.lenords.repley.model.queries;

import java.util.List;

public class QueryModelParamContainer {
  private List<String> required;
  private List<String> optional;

  public QueryModelParamContainer(List<String> required) {
    this.required = required;
  }

  public QueryModelParamContainer(List<String> required, List<String> optional) {
    this.required = required;
    this.optional = optional;
  }

  public void addRequiredParam(String requiredParam) {
    this.required.add(requiredParam);
  }

  public void addOptionalParam(String optionalParam) {
    this.optional.add(optionalParam);
  }

  public List<String> getRequired() {
    return required;
  }

  public void setRequired(List<String> required) {
    this.required = required;
  }

  public List<String> getOptional() {
    return optional;
  }

  public void setOptional(List<String> optional) {
    this.optional = optional;
  }

  public List<String> getAll() {
    List<String> all = required;
    if (optional != null) {
      all.addAll(optional);
    }
    return all;
  }

  @Override
  public String toString() {
    String tos =  "QueryModelParams{" +
        "required=" + required.toString();

    if (optional != null) {
      tos += ", optional=" + optional.toString();
    }

    tos += '}';
    return tos;
  }
}
