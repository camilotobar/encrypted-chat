package com.icesi;
import java.net.*;
import java.io.*;

public class Client {

    private EncryptionUtils encryptionUtils;
    private int port;
    private String name;

    public Client(int port, String name) {
        this.port = port;
        this.name = name;
    }

    public EncryptionUtils StartClient()
    {
        try {
            // Established the connection
            System.out.println("Connecting to localhost on port " + port);
            Socket client = new Socket("localhost", port);
            System.out.println("Just connected to " + client.getRemoteSocketAddress());

            encryptionUtils = new EncryptionUtils();
            encryptionUtils.generateKeys();

            // Sends the data to client
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            out.writeUTF("");

            // Accepts the data
            DataInputStream in = new DataInputStream(client.getInputStream());;
            client.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            return encryptionUtils;
        }
    }

    public void setKeys(EncryptionUtils anotherConnection) {
        encryptionUtils.receivePublicKeyFrom(anotherConnection);
        encryptionUtils.generateCommonSecretKey();
    }

    public void sendMessage(String readLine) {

    }
}

