package net.lenords.repley.model.queries;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class QueryModelParamContainer {
  private List<QueryModelParam> required;
  private List<QueryModelParam> optional;

  public QueryModelParamContainer(List<QueryModelParam> required) {
    this.required = required;
  }

  public QueryModelParamContainer(List<QueryModelParam> required, List<QueryModelParam> optional) {
    this.required = required;
    this.optional = optional;
  }

  public void addRequiredParam(QueryModelParam requiredParam) {
    this.required.add(requiredParam);
  }

  public void addOptionalParam(QueryModelParam optionalParam) {
    this.optional.add(optionalParam);
  }

  public QueryModelParam getParamByName(String name) {
    Optional<QueryModelParam> opt =
        required.stream().filter(param -> param.getName().equalsIgnoreCase(name)).findFirst();
    if (!opt.isPresent() && optional != null) {
      opt = optional.stream().filter(param -> param.getName().equalsIgnoreCase(name)).findFirst();
    }
    return opt.orElse(null);
  }

  public List<QueryModelParam> getRequired() {
    return required;
  }

  public void setRequired(List<QueryModelParam> required) {
    this.required = required;
  }

  public List<QueryModelParam> getOptional() {
    return optional;
  }

  public void setOptional(List<QueryModelParam> optional) {
    this.optional = optional;
  }

  public List<QueryModelParam> getAll() {
    List<QueryModelParam> all = required;
    if (optional != null) {
      all.addAll(optional);
    }
    return all;
  }

  public List<String> getAllStr() {
    List<String> allStr =
        required.stream().map(param -> param.getName()).collect(Collectors.toList());
    if (optional != null) {
      allStr.addAll(optional.stream().map(param -> param.getName()).collect(Collectors.toList()));
    }
    return allStr;
  }

  @Override
  public String toString() {
    String tos = "QueryModelParams{" + "required=" + required.toString();

    if (optional != null) {
      tos += ", optional=" + optional.toString();
    }

    tos += '}';
    return tos;
  }
}
