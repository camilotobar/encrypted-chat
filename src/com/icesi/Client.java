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

    private int port;
    private String name;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
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
     *
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

            // we are going to write the answer and send it to the client
            byte[] message = encryptionUtils.encryptMessage(name + ": " + line);
            serverObjectOutput.writeObject(message);

            // The next code lines are going to get the name and host from the user
            System.out.println(encryptionUtils.decryptMessage((byte[]) serverObjectInput.readObject()));

            line = localReader.readLine();
        }

        // Close the streams and the socket associated to the request
        localReader.close();
        serverObjectOutput.close();
        serverObjectInput.close();
        socketKey.close();
    }
}

