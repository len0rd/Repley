package net.lenords.repley.datamanager.sql;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import net.lenords.repley.model.conf.MySqlConnectionConfig;
import net.lenords.repley.model.conf.SshConnectionConfig;

/**
 * The purpose of this class is to automatically setup the proper MySql accessor (either a normal
 * accessor or tunnelling accessor) based on the available xml configuration files
 *
 * @author len0rd
 */
public class ConnectionHelper {
  private static final String DEFAULT_TUNNEL_CONF_LOCATION = "conf/ssh_connection.json";
  private static final String DEFAULT_MYSQL_CONF_LOCATION = "conf/mysql_connection.json";

  private MySqlAccessor accessor;

  public ConnectionHelper(String tunnelConf, String mysqlConf) {
    Gson gson = new Gson();

    SshConnectionConfig sshConf = null;
    MySqlConnectionConfig mysqlConn = null;
    try  {
      mysqlConn = gson.fromJson(new FileReader(getAbsolutePath() + mysqlConf), MySqlConnectionConfig.class);

    } catch (FileNotFoundException e) {
      System.out.println("ERR:: Failed to find mysql config. Any query attempt will fail");
      e.printStackTrace();
      this.accessor = null;
      return;
    } catch (URISyntaxException e) {
      System.out.println("ERR:: Failed to load mysql conf, something wrong with pathfinding.");
      e.printStackTrace();
      this.accessor = null;
      return;
    }

    if (tunnelConf != null) {
      try {
        sshConf = gson.fromJson(new FileReader(getAbsolutePath() + tunnelConf), SshConnectionConfig.class);
      } catch (FileNotFoundException e) {
        System.out.println("Warn:: Failed to import ssh config. proceeding without one.");
      } catch (URISyntaxException e) {
        System.out.println("ERR:: Failed to load ssh conf, something wrong with pathfinding. "
            + "Attempting to proceed without one.");
        e.printStackTrace();
      }
    } else {
      System.out.println("Warn:: Failed to import ssh config. proceeding without one.");
    }

    if (sshConf != null && sshConf.isEnabled()) {
      accessor = new MySqlTunneler(sshConf, mysqlConn);
    } else { //if we dont have an enabled ssh connection, then we only want direct mysql access
      accessor = new MySqlAccessor(mysqlConn);
    }
  }

  public ConnectionHelper() {
    this(DEFAULT_TUNNEL_CONF_LOCATION, DEFAULT_MYSQL_CONF_LOCATION);
  }

  public MySqlAccessor getAccessor() {
    return accessor;
  }


  private String getAbsolutePath() throws URISyntaxException {
    String path = getAbsoluteJarFilePath();
    path = path.substring(0, path.lastIndexOf(File.separator) + 1);
    return path;
  }

  private String getAbsoluteJarFilePath() throws URISyntaxException {
    return ConnectionHelper.class.getProtectionDomain().getCodeSource().getLocation()
        .toURI()
        .getPath();
  }

}
