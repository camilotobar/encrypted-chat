package com.icesi;

import java.net.*;
import java.io.*;
import java.security.PublicKey;

public class Server {

    ServerSocket serverSocketKey;
    Socket socketKey;
    private EncryptionUtils encryptionUtils;
    BufferedReader localReader;
    ObjectInputStream clientObjectInput;
    ObjectOutputStream clientObjectOutput;

    private int port;
    private String name;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Server client = new Server(15000);
        client.startServer();
        client.startChatting();
    }

    /**
     * Creates a new chat server
     * @param port port selected
     */
    public Server(int port) {
        this.port = port;
    }

    /**
     *
     */
    public void startServer()
    {
        try {
            encryptionUtils = new EncryptionUtils();
            encryptionUtils.generateKeys();
            serverSocketKey = new ServerSocket(port);
            System.out.println("Waiting for client on port " + serverSocketKey.getLocalPort() + "...");
            socketKey = serverSocketKey.accept();
            System.out.println("Just connected to " + socketKey.getRemoteSocketAddress());

            clientObjectInput = new ObjectInputStream(socketKey.getInputStream());
            setKeys((PublicKey) clientObjectInput.readObject());

            clientObjectOutput = new ObjectOutputStream(socketKey.getOutputStream());
            clientObjectOutput.writeObject(encryptionUtils.getPublicKey());

        }
        catch (SocketTimeoutException s) {
            System.out.println("Socket timed out!");
        }
        catch (Exception e) {
            System.out.println("Exception on starting.");
        }
    }

    /**
     *
     * @param serverPublicKey
     */
    public void setKeys(PublicKey serverPublicKey) {
        encryptionUtils.receivePublicKeyFrom(serverPublicKey);
        encryptionUtils.generateCommonSecretKey();
    }

    /**
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void startChatting() throws IOException, ClassNotFoundException {
        localReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome back!\nPlease write your name.");
        String line = localReader.readLine();
        name = line;

        while (!line.equals("finish")) {

            // The next code lines are going to get the name and host from the user
            System.out.println(encryptionUtils.decryptMessage((byte[]) clientObjectInput.readObject()));

            // we are going to write the answer and send it to the client
            byte[] message = encryptionUtils.encryptMessage(name + ": " + line);
            clientObjectOutput.writeObject(message);

            line = localReader.readLine();
        }

        // Close the streams and the socket associated to the request
        localReader.close();
        clientObjectInput.close();
        clientObjectOutput.close();
        socketKey.close();
    }
}

