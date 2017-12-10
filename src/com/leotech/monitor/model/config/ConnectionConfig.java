package com.leotech.monitor.model.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ConnectionConfig")
public class ConnectionConfig {

  @XmlElement(required = true)
  private String host, username, password;
  @XmlElement(required = true)
  private int port;
  @XmlElement(required = true)
  private boolean enabled;

  public ConnectionConfig(String host, int port, String username, String password,
      boolean enabled) {
    this.host = host;
    this.port = port;
    this.username = username;
    this.password = password;
    this.enabled = enabled;
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
}
