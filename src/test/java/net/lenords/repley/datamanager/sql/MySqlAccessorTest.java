package net.lenords.repley.datamanager.sql;

import net.lenords.repley.model.conf.ConnectionConfig;
import net.lenords.repley.model.sql.Row;
import net.lenords.repley.model.sql.SqlResult;
import net.lenords.repley.serial.ConnectionConfigSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MySqlAccessorTest {

  private MySqlAccessor accessor;

  @BeforeEach
  public void setup() {
    //Running the tests generally takes place in a differnt location than normal
    //TODO: ideally find a better way to do this
    ConnectionConfigSerializer ccs = new ConnectionConfigSerializer();
    ConnectionConfig cc = ccs.importConnection("../../../conf/mysql_connection.json");
    Assertions.assertNotNull(cc, "Failed to import connection config!");
    accessor = new MySqlAccessor(cc, "re_stat");
  }

  @AfterEach
  public void takedown() {
    accessor.close();
    accessor = null;
  }

  @Test
  public void resultPopulation() {
    SqlResult result = accessor.getQueryResult("SELECT * FROM export_totals");
    Assertions.assertNotNull(result, "Accessor returned null result");
    Assertions.assertFalse(result.isEmpty(), "Accessor query result is empty!");
    for (Row row : result.getRows()) {
      System.out.println(row.toJSONObject().toString(1));
    }
  }

}
