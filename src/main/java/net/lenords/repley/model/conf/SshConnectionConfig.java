package net.lenords.repley.model.conf;

public class SshConnectionConfig extends ConnectionConfig {
  private String privKey;


  public SshConnectionConfig(String host, String user, String pass, int port, boolean enabled) {
    super(host, user, pass, port, enabled);
  }

  public String getKey() {
    return privKey;
  }

  public void setKey(String privKey) {
    this.privKey = privKey;
  }

  public boolean hasKey() {
    return privKey != null && !privKey.isEmpty();
  }
}
