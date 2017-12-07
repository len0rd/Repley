package com.leotech.monitor.importer;

import com.leotech.monitor.model.config.ConnectionConfig;
import java.io.File;
import java.net.URISyntaxException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class ConnectionConfigSerializer {
  private final JAXBContext context;

  public ConnectionConfigSerializer() {
    JAXBContext temp = null;
    try {
      temp = JAXBContext.newInstance(ConnectionConfig.class);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      this.context = temp;
    }
  }

  public ConnectionConfig importConnection(String filePathName) {
    ConnectionConfig connConfig = null;
    try {
      final Unmarshaller unmarshaller = context.createUnmarshaller();
      connConfig = (ConnectionConfig) unmarshaller.unmarshal(new File(filePathName));
    } catch (Exception e) {
      e.printStackTrace();
    }

    return connConfig;
  }

  public void exportConnection(ConnectionConfig configToExport, String outputFilePathName) {
    try {
      final Marshaller marshaller = context.createMarshaller();
      marshaller.marshal(configToExport, new File(outputFilePathName));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void exportGenericConnection() {
    ConnectionConfig conf = new ConnectionConfig("localhost", 3306, "tyler", "nerd");
    exportConnection(conf, "conf/defaultConnection.xml");
  }

  private String getAbsolutePath() throws URISyntaxException {
    String path = getAbsoluteJarFilePath();
    path = path.substring(0, path.lastIndexOf(File.separator) + 1);
    return path;
  }

private String getAbsoluteJarFilePath() throws URISyntaxException {
    return ConnectionConfigSerializer.class.getProtectionDomain().getCodeSource().getLocation().toURI()
        .getPath();
  }

}
