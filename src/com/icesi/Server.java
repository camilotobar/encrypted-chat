package com.icesi;

import java.net.*;
import java.io.*;

public class Server {

    ServerSocket serverSocket;
    Socket server;
    private EncryptionUtils encryptionUtils;
    private int port;
    private String name;

    public Server(int port, String name) {
        this.port = port;
        this.name = name;
    }

    public EncryptionUtils StartServer() throws IOException
    {
        try {
            // Established the Connection
            serverSocket = new ServerSocket(port);
            System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
            server = serverSocket.accept();
            System.out.println("Just connected to " + server.getRemoteSocketAddress());

            encryptionUtils = new EncryptionUtils();
            encryptionUtils.generateKeys();
        }
        catch (SocketTimeoutException s) {
            System.out.println("Socket timed out!");
        }
        catch (Exception e) {
            System.out.println("Exception on starting.");
        }
        finally {
            return encryptionUtils;
        }
    }

    public void setKeys(EncryptionUtils anotherConnection) {
        encryptionUtils.receivePublicKeyFrom(anotherConnection);
        encryptionUtils.generateCommonSecretKey();
    }

    public void sendMessage(String readLine) throws IOException {
        // Accepts the data from client
        DataInputStream in = new DataInputStream(server.getInputStream());
        String line = in.readUTF();

        OutputStream outToClient = server.getOutputStream();
        DataOutputStream out = new DataOutputStream(outToClient);

        out.writeUTF("");
        server.close();
    }
}

