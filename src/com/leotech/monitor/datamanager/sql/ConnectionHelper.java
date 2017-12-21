package com.leotech.monitor.datamanager.sql;

import com.leotech.monitor.importer.ConnectionConfigSerializer;
import com.leotech.monitor.model.config.ConnectionConfig;

public class ConnectionHelper {
  private static final String DEFAULT_TUNNEL_CONF_LOCATION = "conf/ssh_connection.xml";
  private static final String DEFAULT_MYSQL_CONF_LOCATION = "conf/mysql_connection.xml";

  private MySqlAccessor accessor;

  public ConnectionHelper(String tunnelConf, String mysqlConf, String database) {
    ConnectionConfigSerializer ccs = new ConnectionConfigSerializer();

    ConnectionConfig mysql = ccs.importConnection(mysqlConf);
    ConnectionConfig tunnel = null; //its possible and reasonable that this config wont exist
    try {
      tunnel = ccs.importConnection(tunnelConf);
    } catch (Exception e) {
      System.out.println("ERR::Failed to import the tunnel connection config, proceeding without one");
    }

    if (tunnel != null && tunnel.isEnabled()) {
      accessor = new MySqlTunneler(tunnel, mysql);
    } else {
      accessor = new MySqlAccessor(mysql, database);
    }
  }

  public ConnectionHelper(String database) {
    this(DEFAULT_TUNNEL_CONF_LOCATION, DEFAULT_MYSQL_CONF_LOCATION, database);
  }

  public ConnectionHelper() {
    this(DEFAULT_TUNNEL_CONF_LOCATION, DEFAULT_MYSQL_CONF_LOCATION, null);
  }

  public MySqlAccessor getAccessor() {
    return accessor;
  }

}
