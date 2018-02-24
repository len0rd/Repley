package net.lenords.repley.datamanager.sql;

import net.lenords.repley.model.sql.Row;
import net.lenords.repley.model.sql.SqlResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MySqlAccessorTest {

  private MySqlAccessor accessor;

  @BeforeEach
  public void setup() {
    //Running the tests generally takes place in a differnt location than normal
    ConnectionHelper ch = new ConnectionHelper(null, "conf/mysql_connection.json");
    accessor = ch.getAccessor();
    Assertions.assertNotNull(accessor, "Failed to import connection config!");
    System.out.println(accessor.getConfig().toString());
  }

  @AfterEach
  public void takedown() {
    accessor.close();
    accessor = null;
  }

  @Test
  public void resultPopulation() {
    SqlResult result = accessor.getQueryResult("SELECT * FROM re_stat.export_total");
    Assertions.assertNotNull(result, "Accessor returned null result");
    Assertions.assertFalse(result.isEmpty(), "Accessor query result is empty!");
    for (Row row : result.getRows()) {
      System.out.println(row.toJSONObject().toString(1));
    }
  }

}
