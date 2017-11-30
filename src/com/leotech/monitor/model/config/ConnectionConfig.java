package com.leotech.monitor.model.config;

public class ConnectionConfig {
    private String host, username, password;
    private int port;

    public ConnectionConfig(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
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

    public String getStringPort() {
        return String.valueOf(port);
    }

    public void setPort(int port) {
        this.port = port;
    }
}
