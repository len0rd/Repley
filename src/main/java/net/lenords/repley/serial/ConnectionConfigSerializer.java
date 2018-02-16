package net.lenords.repley.serial;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import net.lenords.repley.model.conf.ConnectionConfig;

public class ConnectionConfigSerializer {

  public ConnectionConfigSerializer() {}

  public ConnectionConfig importConnection(String relativeFilePathName) {
    ConnectionConfig connConfig = null;
    BufferedReader reader = null;
    File readin = null;

    try {
      readin = new File(getAbsolutePath() + relativeFilePathName);
    } catch (URISyntaxException e) {
      System.out.println("Err while trying to get file path");
      e.printStackTrace();
    }

    if (readin != null && readin.exists()) {
      try {
        reader = new BufferedReader(new FileReader(readin));
        Gson gson = new GsonBuilder().create();
        connConfig = gson.fromJson(reader, ConnectionConfig.class);

      } catch (Exception  e) {
        System.out.println("Something went wrong trying to import the connection json");
        e.printStackTrace();
      } finally {
        if (reader != null) {
          try {
            reader.close();
          } catch (Exception ignored) {}
        }
      }
    }

    return connConfig;
  }

  public ConnectionConfig importDefaultConnection() {
    return importConnection("conf/ssh_connection.json");
  }

  public void exportConnection(ConnectionConfig configToExport, String outputFilePathName) {
    Writer writer = null;

    try {
      writer = new FileWriter(getAbsolutePath() + outputFilePathName);
      Gson gson = new GsonBuilder().create();
      gson.toJson(configToExport, writer);

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (writer != null) {
        try {
          writer.close();
        } catch (IOException ignored) {}
      }
    }
  }

  public void exportGenericConnection() {
    ConnectionConfig conf = new ConnectionConfig("localhost", 3306, "root",
        "nerd", true, null);
    exportConnection(conf, "conf/default_connection.json");
  }

  private String getAbsolutePath() throws URISyntaxException {
    String path = getAbsoluteJarFilePath();
    path = path.substring(0, path.lastIndexOf(File.separator) + 1);
    return path;
  }

  private String getAbsoluteJarFilePath() throws URISyntaxException {
    return ConnectionConfigSerializer.class.getProtectionDomain().getCodeSource().getLocation()
        .toURI()
        .getPath();
  }

}
