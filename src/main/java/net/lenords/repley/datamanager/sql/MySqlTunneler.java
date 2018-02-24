package net.lenords.repley.datamanager.sql;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.net.ServerSocket;
import net.lenords.repley.model.conf.MySqlConnectionConfig;
import net.lenords.repley.model.conf.SshConnectionConfig;

public class MySqlTunneler extends MySqlAccessor {

  private static final String REMOTE_HOST = "localhost";
  private static final int REMOTE_PORT = 3306;

  private Session sshSession;


  public MySqlTunneler(SshConnectionConfig sshConn, MySqlConnectionConfig mysqlConn) {
    createConnection(sshConn, mysqlConn);
  }

  public boolean createConnection(SshConnectionConfig sshConn, MySqlConnectionConfig mysqlConn) {
    //assert mysqlConn.getPort() != 3306; //WHY?? What was i doing...

    try {

      //get a random available port for our ssh/mysql tunnel
      //the ssh tunnel will forward all info to that port, so mysql can connect to it as
      //'localhost:PORT'
      ServerSocket socket = null;
      try {
        socket = new ServerSocket(0);
        System.out.println("Tunneling mysql connection to: " + socket.getLocalPort());
        int availablePort = socket.getLocalPort();
        mysqlConn.setPort(availablePort);
      } catch (Exception e) {
        System.out.println(
            "===ERR::Failed to grab a random available port for ssh/mysql tunneling, proceeding with defaults...");
        e.printStackTrace();
      } finally {
        if (socket != null) {
          socket.close();
        }
      }

      JSch jsch = new JSch();
      if (sshConn.hasKey()) {
        if (sshConn.hasPass()) {
          jsch.addIdentity(sshConn.getKey(), sshConn.getPass());
        } else {
          jsch.addIdentity(sshConn.getKey());
        }
      }
      // Get SSH session
      System.out.println("Get Session");
      sshSession = jsch.getSession(sshConn.getUser(), sshConn.getHost(), sshConn.getPort());

      if (!sshConn.hasKey()) {
        sshSession.setPassword(sshConn.getPass());
      }

      java.util.Properties config = new java.util.Properties();
      // Never automatically add new host keys to the host file
      config.put("StrictHostKeyChecking", "no");
      System.out.println("Set config");
      sshSession.setConfig(config);
      // Connect to remote server
      System.out.println("Connect");
      sshSession.connect();
      // Apply the port forwarding
      System.out.println("Set Port forwarding");
      sshSession.setPortForwardingL(mysqlConn.getPort(), REMOTE_HOST, REMOTE_PORT);
      // Connect to remote database
      return super.createConnection(mysqlConn);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public void close() {
    if (conn != null) {
      try {
        conn.close();
      } catch (Exception ignored) {
      }
      closeSSH();
    }
  }

  public void closeSSH() {
    if (sshSession != null && sshSession.isConnected()) {
      sshSession.disconnect();
    }
  }
}

