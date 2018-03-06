package net.lenords.repley.datamanager.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import net.lenords.repley.model.conf.MySqlConnectionConfig;
import net.lenords.repley.model.sql.SqlResult;

public class MySqlAccessor implements Accessor {
  protected Connection conn = null;
  private MySqlConnectionConfig config;

  //default empty constructor
  public MySqlAccessor() {}

  public MySqlAccessor(MySqlConnectionConfig conf) {
    this.createConnection(conf);
  }

  public boolean createConnection(MySqlConnectionConfig conf) {
    this.config = conf;
    //close our connection if it's already been created
    try { //chances of throwing very low
      this.close();
    } catch (Exception ignored) {}

    try {
      DriverManager.registerDriver(new com.mysql.jdbc.Driver());
      //Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection("jdbc:mysql://" + conf.getFullHost(),
            conf.getUser(), conf.getPass());
    } catch (Exception e) {
      System.out.println("Failed to instantiate connection!");
      return false;
    }

    return true;
  }

  @Override
  public Connection getConn() {
    return conn;
  }

  @Override
  public SqlResult getQueryResult(PreparedStatement ps) {
    SqlResult result = new SqlResult();

    try (ResultSet rs = ps.executeQuery()) {
      result.populate(rs, rs.getMetaData());
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }

  @Override
  public SqlResult getQueryResult(String sqlQuery, List<Object> orderedParams) {
    PreparedStatement ps = null;
    SqlResult result = null;

    try {
      ps = conn.prepareStatement(sqlQuery);
      if (orderedParams != null && orderedParams.size() > 0) {
        for (int i = 0; i < orderedParams.size(); i++) {
          //remember 1-based indexing
          ps.setObject(i + 1, orderedParams.get(i));
        }
      }
      result = getQueryResult(ps);
    } catch (Exception e) {
      System.out.println("ERR::Failed to get query results for this query:");
      System.out.println(sqlQuery);
      e.printStackTrace();
    } finally {
      if (ps != null) {
        try {
          ps.close();
        } catch (SQLException ignored) {}
      }
    }

    return result;
  }

  @Override
  public SqlResult getQueryResult(String sqlQuery) {
    return getQueryResult(sqlQuery, null);
  }


  @Override
  public void close() {
    if (conn != null) {
      try {
        conn.close();
      } catch (Exception ignored) {
      }

    }
  }


  public MySqlConnectionConfig getConfig() {
    return config;
  }
}
