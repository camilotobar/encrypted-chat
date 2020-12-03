package com.icesi;

import java.net.*;
import java.io.*;
import java.security.PublicKey;

public class Server {

    ServerSocket serverSocketKey;
    Socket socketKey;
    private EncryptionUtils encryptionUtils;
    ObjectInputStream clientObjectInput;
    ObjectOutputStream clientObjectOutput;
    private BufferedReader localReader;
    private boolean stop;

    private int port;

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
        String name = localReader.readLine();
        System.out.println("Chat ready, type something to send a message");
        SenderThread sender = new SenderThread(encryptionUtils, clientObjectOutput, name);
        sender.start();
        ListenerThread listener = new ListenerThread(encryptionUtils, clientObjectInput, stop);
        listener.start();
        // Close the streams and the socket associated to the request
//        clientObjectInput.close();
//        clientObjectOutput.close();
//        socketKey.close();
    }
    
}

