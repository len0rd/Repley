package net.lenords.repley.model.queries;

import java.util.List;

public class QueryModelParams {
  private List<String> required;
  private List<String> optional;

  public QueryModelParams(List<String> required) {
    this.required = required;
  }

  public QueryModelParams(List<String> required, List<String> optional) {
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

  @Override
  public String toString() {
    return "QueryModelParams{" +
        "required=" + required.toString() +
        ", optional=" + optional.toString() +
        '}';
  }
}
