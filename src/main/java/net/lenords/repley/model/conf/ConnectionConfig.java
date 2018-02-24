package net.lenords.repley.model.conf;

public class ConnectionConfig {
  private String host, user, pass;
  private int port;
  private boolean enabled;


  public ConnectionConfig(String host, String user, String pass, int port, boolean enabled) {
    this.host = host;
    this.user = user;
    this.pass = pass;
    this.port = port;
    this.enabled = enabled;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getPass() {
    return pass;
  }

  public void setPass(String pass) {
    this.pass = pass;
  }

  public boolean hasPass() {
    return pass != null && !pass.isEmpty();
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  @Override
  public String toString() {
    return "ConnectionConfig{" +
        "host='" + host + '\'' +
        ", user='" + user + '\'' +
        ", pass='" + pass + '\'' +
        ", port=" + port +
        '}';
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}
