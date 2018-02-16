package net.lenords.repley.model.conf;

public class ConnectionConfig {

  private String host, username, password, key;
  private int port;
  private boolean enabled;

  public ConnectionConfig(String host, int port, String username, String password,
      boolean enabled, String key) {
    this.host = host;
    this.port = port;
    this.username = username;
    this.password = password;
    this.enabled = enabled;
    this.key = key;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public String getStringPort() {
    return String.valueOf(port);
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  @Override
  public String toString() {
    return "ConnectionConfig{" +
        "host='" + host + '\'' +
        ", username='" + username + '\'' +
        ", password='" + password + '\'' +
        ", port=" + port +
        ", enabled=" + enabled +
        '}';
  }
}