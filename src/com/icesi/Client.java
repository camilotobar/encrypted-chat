package com.icesi;
import java.net.*;
import java.io.*;

public class Client {

    Socket server;
    private EncryptionUtils encryptionUtils;
    PrintWriter serverWriter;
    BufferedReader serverReader;
    BufferedReader localReader;

    private int port;
    private String name;

    public static void main(String[] args) throws IOException {
        Client client = new Client(15000, "Camilo");
        client.startClient();
        client.startChatting();
    }

    public Client(int port, String name) {
        this.port = port;
        this.name = name;
    }

    public void startClient() {
        try {
            // Established the connection
            System.out.println("Connecting to localhost on port " + port);
            server = new Socket("localhost", port);
            System.out.println("Just connected to " + server.getRemoteSocketAddress());
            serverReader = new BufferedReader(new InputStreamReader(server.getInputStream()));
            serverWriter = new PrintWriter(server.getOutputStream(), true);

            encryptionUtils = new EncryptionUtils();
            encryptionUtils.generateKeys();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setKeys() {
        encryptionUtils.receivePublicKeyFrom(encryptionUtils.getPublicKey());
        encryptionUtils.generateCommonSecretKey();
    }

    public void startChatting() throws IOException {
        localReader = new BufferedReader(new InputStreamReader(System.in));
        String line = localReader.readLine();

        while (!line.equals("finish")) {

            // we are going to write the answer and send it to the client
            String message = name + ": " + line;
            serverWriter.println(message);

            // The next code lines are going to get the name and host from the user
            System.out.println(serverReader.readLine());

            line = localReader.readLine();
        }

        // Close the streams and the socket associated to the request
        localReader.close();
        serverWriter.close();
        serverReader.close();
        server.close();
    }
}

