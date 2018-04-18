package net.lenords.repley.model.queries;

/**
 * Mappings are regex patterns that can be applied to modify values.
 *
 * @author lenord
 * @since 2018-04-18
 */
public class Mapping {
  private String input, mapto;

  public Mapping(String input, String mapto) {
    this.input = input;
    this.mapto = mapto;
  }

  public String getInput() {
    return input;
  }

  public void setInput(String input) {
    this.input = input;
  }

  public String getMapto() {
    return mapto;
  }

  public void setMapto(String mapto) {
    this.mapto = mapto;
  }
}
