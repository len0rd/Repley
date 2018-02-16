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
    ConnectionHelper ch = new ConnectionHelper("res_stat");
    Assertions.assertNotNull(ch, "Failed to initialize SQL connection helper!");
    accessor = ch.getAccessor();
  }

  @AfterEach
  public void takedown() {
    accessor.close();
    accessor = null;
  }

  @Test
  public void resultPopulation() {
    SqlResult result = accessor.getQueryResults("SELECT * FROM export_totals");
    Assertions.assertNotNull(result, "Accessor returned null result");
    Assertions.assertFalse(result.isEmpty(), "Accessor query result is empty!");
    for (Row row : result.getRows()) {
      System.out.println(row.toJSONObject().toString(1));
    }
  }

}
