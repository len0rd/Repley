package net.lenords.repley.datamanager.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import net.lenords.repley.model.conf.ConnectionConfig;
import net.lenords.repley.model.sql.SqlResult;

public class MySqlAccessor {
  protected Connection conn = null;

  //default empty constructor
  public MySqlAccessor() {}

  public MySqlAccessor(ConnectionConfig conf, String database) {
    this.createConnection(conf, database);
  }

  public MySqlAccessor(String url, String user, String password) {
    this.createConnection(url, user, password);
  }

  /**
   *
   *
   * @param host Formatted as Address:Port/database_name
   * @param user Mysql username to use
   * @param pass Mysql password to use
   * @return True if the connection was successfully created, otherwise dalse
   */
  public boolean createConnection(String host, String user, String pass) {
    //close our connection if it's already been created
    try {
      this.close();
    } catch (Exception ignored) {
    }

    try {
      DriverManager.registerDriver(new com.mysql.jdbc.Driver());
      //Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection("jdbc:mysql://" + host, user, pass);
    } catch (Exception e) {
      System.out.println("Failed to instantiate connection!");
      return false;
    }

    return true;
  }

  public boolean createConnection(ConnectionConfig conf, String database) {
    //close our connection if it's already been created
    try { //chances of throwing very low
      this.close();
    } catch (Exception ignored) {
    }

    String host = "jdbc:mysql://" + conf.getHost() + ":" + conf.getPort();
    if (database != null && !database.isEmpty()) {
      host += "/" + database;
    }
    try {
      DriverManager.registerDriver(new com.mysql.jdbc.Driver());
      //Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(host, conf.getUsername(), conf.getPassword());
    } catch (Exception e) {
      System.out.println("Failed to instantiate connection!");
      return false;
    }

    return true;
  }

  public SqlResult getQueryResults(String sqlQuery, List<String> orderedStringParams) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    SqlResult result = new SqlResult();

    try {
      ps = conn.prepareStatement(sqlQuery);
      if (orderedStringParams != null && orderedStringParams.size() > 0) {
        for (int i = 0; i < orderedStringParams.size(); i++) {
          //remember 1-based indexing
          ps.setString(i + 1, orderedStringParams.get(i));
        }
      }
      rs = ps.executeQuery();
      result.populate(rs, rs.getMetaData());
    } catch (Exception e) {
      System.out.println("ERR::Failed to get query results for this query:");
      System.out.println(sqlQuery);
      e.printStackTrace();
    } finally {
      if (rs != null) {
        try {
          rs.close();
        } catch (SQLException ignored) {
        }
      }
      if (ps != null) {
        try {
          ps.close();
        } catch (SQLException ignored) {
        }
      }
    }

    return result;
  }

  public SqlResult getQueryResults(String sqlQuery) {
    return getQueryResults(sqlQuery, null);
  }

  public void close() {
    if (conn != null) {
      try {
        conn.close();
      } catch (Exception ignored) {
      }

    }
  }
}
