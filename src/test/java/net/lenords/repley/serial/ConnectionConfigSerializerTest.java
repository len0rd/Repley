package net.lenords.repley.serial;

import net.lenords.repley.model.conf.ConnectionConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConnectionConfigSerializerTest {


  @Test
  public void importConnection() {
    ConnectionConfigSerializer ccs = new ConnectionConfigSerializer();
    ConnectionConfig cc = ccs.importConnection("../../../conf/mysql_connection.json");
    Assertions.assertNotNull(cc, "Imported ConnectionConfig is null!");
  }
}
