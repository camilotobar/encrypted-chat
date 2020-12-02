package com.icesi;

import java.io.*;

public class Main {

    static BufferedReader reader;
    static BufferedWriter writer;
    static String type;
    static String name;
    static int port;

    public static void main(String[] args) throws IOException {
        writer = new BufferedWriter(new OutputStreamWriter(System.out));
        reader = new BufferedReader(new InputStreamReader(System.in));
        Server server = null;
        Client client = null;
        EncryptionUtils serverUtils = null;
        EncryptionUtils clientUtils = null;
        port = 0;

        String line = reader.readLine();
        while(line != "finish")
        {
            if(port == 0) {
                writer.write("Write your port");
                port = Integer.parseInt(reader.readLine());
            }
            else if(name == null) {
                writer.write("Write your name");
                name = reader.readLine();
            }
            else if(type == null) {
                writer.write("Type 'C' if you are client / 'S' if you are server");
                line = reader.readLine();
                type = line.equals("C")? "client" : "server";

                if(type == "client") {
                    client = new Client(port, name);
                    clientUtils = client.StartClient();
                }
                else {
                    server = new Server(port, name);
                    serverUtils = server.StartServer();
                }
            }
            else if(client != null && server != null) {
                server.setKeys(clientUtils);
                client.setKeys(serverUtils);
            }
            else {
                if(type == "client")
                    client.sendMessage(reader.readLine());
                else
                    server.sendMessage(reader.readLine());
            }
        }

        reader.close();
        writer.close();
    }
}
