package net.lenords.repley.model.conf;

public class SshConnectionConfig extends ConnectionConfig {
  private String key;


  public SshConnectionConfig(String host, String user, String pass, int port, boolean enabled) {
    super(host, user, pass, port, enabled);
  }

  public SshConnectionConfig(String host, String user, String pass, int port, boolean enabled,
      String key) {
    super(host, user, pass, port, enabled);
    this.key = key;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String privKey) {
    this.key = privKey;
  }

  public boolean hasKey() {
    return key != null && !key.isEmpty();
  }
}
