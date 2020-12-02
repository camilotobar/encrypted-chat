package com.icesi;

import java.net.*;
import java.io.*;

public class Server {

    ServerSocket serverSocket;
    Socket client;
    private EncryptionUtils encryptionUtils;
    PrintWriter clientWriter;
    BufferedReader clientReader;
    BufferedReader localReader;

    private int port;
    private String name;

    public static void main(String[] args) throws IOException {
        Server client = new Server(15000, "Diego");
        client.startServer();
        client.startChatting();
    }

    public Server(int port, String name) {
        this.port = port;
        this.name = name;
    }

    public void startServer()
    {
        try {
            // Established the Connection
            serverSocket = new ServerSocket(port);
            System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
            client = serverSocket.accept();
            System.out.println("Just connected to " + client.getRemoteSocketAddress());
            clientReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            clientWriter = new PrintWriter(client.getOutputStream(), true);

            encryptionUtils = new EncryptionUtils();
            encryptionUtils.generateKeys();
        }
        catch (SocketTimeoutException s) {
            System.out.println("Socket timed out!");
        }
        catch (Exception e) {
            System.out.println("Exception on starting.");
        }
    }

    public void setKeys(EncryptionUtils anotherConnection) {
        encryptionUtils.receivePublicKeyFrom(null);
        encryptionUtils.generateCommonSecretKey();
    }

    public void startChatting() throws IOException {
        localReader = new BufferedReader(new InputStreamReader(System.in));
        String line = localReader.readLine();

        while (!line.equals("finish")) {

            // The next code lines are going to get the name and host from the user
            System.out.println(clientReader.readLine());

            // we are going to write the answer and send it to the client
            String message = name + ": " + line;
            clientWriter.println(message);

            line = localReader.readLine();
        }

        // Close the streams and the socket associated to the request
        localReader.close();
        clientReader.close();
        clientWriter.close();
        client.close();
    }
}

