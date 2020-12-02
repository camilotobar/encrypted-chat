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

    public void StartClient()
    {
        try {
            encryptionUtils = new EncryptionUtils();
            encryptionUtils.generateKeys();

            // Established the connection
            System.out.println("Connecting to localhost on port " + port);
            Socket client = new Socket("localhost", port);
            System.out.println("Just connected to " + client.getRemoteSocketAddress());

            // Sends the data to client
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            pstr = Integer.toString(p);
            out.writeUTF(pstr); // Sending p

            gstr = Integer.toString(g);
            out.writeUTF(gstr); // Sending g

            double A = ((Math.pow(g, a)) % p); // calculation of A
            Astr = Double.toString(A);
            out.writeUTF(Astr); // Sending A

            // Client's Private Key
            System.out.println("From Client : Private Key = " + a);

            // Accepts the data
            DataInputStream in = new DataInputStream(client.getInputStream());

            serverB = Double.parseDouble(in.readUTF());
            System.out.println("From Server : Public Key = " + serverB);

            Adash = ((Math.pow(serverB, a)) % p); // calculation of Adash

            System.out.println("Secret Key to perform Symmetric Encryption = " + Adash);
            client.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

