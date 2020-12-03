package com.icesi;
import java.net.*;
import java.io.*;
import java.security.PublicKey;

public class Client {

    Socket socketKey;
    private EncryptionUtils encryptionUtils;
    BufferedReader localReader;
    ObjectInputStream serverObjectInput;
    ObjectOutputStream serverObjectOutput;
    private boolean stop;

    private int port;
    private String name;

    public static void main(String[] args) throws IOException {
        Client client = new Client(15000);
        client.startClient();
        client.startChatting();
    }

    /**
     * Creates a new chat client
     * @param port port selected
     */
    public Client(int port) {
        this.port = port;
    }

    /**
     * Starts a client instance who will search for a server and establish a connection 
     */
    public void startClient() {
        try {
            encryptionUtils = new EncryptionUtils();
            encryptionUtils.generateKeys();
            System.out.println("Connecting to localhost on port " + port);
            socketKey = new Socket("localhost", port);
            System.out.println("Just connected to " + socketKey.getRemoteSocketAddress());

            serverObjectOutput = new ObjectOutputStream(socketKey.getOutputStream());
            serverObjectOutput.writeObject(encryptionUtils.getPublicKey());

            serverObjectInput = new ObjectInputStream(socketKey.getInputStream());
            setKeys((PublicKey) serverObjectInput.readObject());

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the keys that are going to be used for conversation encryption
     * @param serverPublicKey
     */
    public void setKeys(PublicKey serverPublicKey) {
        encryptionUtils.receivePublicKeyFrom(serverPublicKey);
        encryptionUtils.generateCommonSecretKey();
    }

    /**
     * Starts the threads in charge of handling the chat
     * @throws IOException
     */
    public void startChatting() throws IOException{
    	
        localReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome back!\nPlease write your name.");
        String name = localReader.readLine();
        System.out.println("Chat ready, type something to send a message");
        SenderThread sender = new SenderThread(encryptionUtils, serverObjectOutput, name);
        sender.start();
        ListenerThread listener = new ListenerThread(encryptionUtils, serverObjectInput, stop);
        listener.start();
        
    }
    
}

