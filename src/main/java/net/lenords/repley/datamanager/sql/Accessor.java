package net.lenords.repley.datamanager.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import net.lenords.repley.model.sql.SqlResult;

public interface Accessor {

  Connection getConn();

  void close();

  SqlResult getQueryResult(String sqlQuery, List<Object> orderedParams);

  SqlResult getQueryResult(String sqlQuery);

  SqlResult getQueryResult(PreparedStatement ps);

}
