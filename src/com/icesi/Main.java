package com.icesi;

import java.io.*;

public class Main {

    static BufferedReader reader;
    static BufferedWriter writer;

    public static void main(String[] args) throws IOException {
        writer = new BufferedWriter(new OutputStreamWriter(System.out));
        reader = new BufferedReader(new InputStreamReader(System.in));

        String line = reader.readLine();
        while(line != "finish")
        {

        }

        reader.close();
        writer.close();
    }
}
