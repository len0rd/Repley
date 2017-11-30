package com.leotech.monitor.datamanager.sql;

import com.leotech.monitor.model.config.ConnectionConfig;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.sun.istack.internal.Nullable;

import java.net.ServerSocket;

public class MySqlTunnelAccessHelper extends MySqlAccessHelper {
    private Session sshSession;
    private static final String REMOTE_HOST     = "localhost";
    private static final int    REMOTE_PORT     = 3306;
    public final ConnectionConfig defaultSSHConf = new ConnectionConfig("localhost", 22, "tyler", "password");


    public MySqlTunnelAccessHelper(ConnectionConfig sshConn, ConnectionConfig mysqlConn) {
        createConnection(sshConn, mysqlConn, null);
    }
    public MySqlTunnelAccessHelper() {
        createConnection(defaultSSHConf, defaultMYSQLlConf, null);
    }

    public boolean createConnection(ConnectionConfig sshConn, ConnectionConfig mysqlConn, @Nullable String database) {
        assert mysqlConn.getPort() != 3306;

        try {
            //get a random available port for our ssh/mysql tunnel
            ServerSocket socket = null;
            try {
                socket = new ServerSocket(0);
                System.out.println("Tunneling mysql connection to: " + socket.getLocalPort());
                int availablePort = socket.getLocalPort();
                mysqlConn.setPort(availablePort);
            }
            catch (Exception e) {
                System.out.println("===ERR::Failed to grab a random available port for ssh/mysql tunneling, proceeding with defaults...");
                e.printStackTrace();
            }
            finally {
                if (socket != null) {
                    socket.close();
                }
            }

            JSch jsch = new JSch();
            // Get SSH session
            System.out.println ("Get Session");
            sshSession = jsch.getSession(sshConn.getUsername(), sshConn.getHost(), sshConn.getPort());
            sshSession.setPassword(sshConn.getPassword());
            java.util.Properties config = new java.util.Properties();
            // Never automatically add new host keys to the host file
            config.put("StrictHostKeyChecking", "no");
            System.out.println ("Set config");
            sshSession.setConfig(config);
            // Connect to remote server
            System.out.println ("Connect");
            sshSession.connect();
            // Apply the port forwarding
            System.out.println ("Set Port forwarding");
            sshSession.setPortForwardingL(mysqlConn.getPort(), REMOTE_HOST, REMOTE_PORT);
            // Connect to remote database
            return super.createConnection(mysqlConn, database);
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
            } catch (Exception ignored){}
            closeSSH();
        }
    }

    public void closeSSH() {
        if(sshSession != null && sshSession.isConnected()){
            sshSession.disconnect();
        }
    }
}
