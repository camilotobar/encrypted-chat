package com.icesi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;

public class SenderThread extends Thread{
	

    private BufferedReader localReader;
    private String name;
    private EncryptionUtils encryptionUtils;
    private ObjectOutputStream clientObjectOutput;

    public SenderThread(EncryptionUtils encryptionUtils, ObjectOutputStream clientObjectOutput, String name) {
    	this.encryptionUtils = encryptionUtils;
    	this.clientObjectOutput = clientObjectOutput;
    	this.name = name;
	}
    
    /**
     * Runs the async task for sending messages
     */
	public void run() {
		
		try {
	        localReader = new BufferedReader(new InputStreamReader(System.in));
	        String line = localReader.readLine();
	        while (!line.equals("finish")) {
	        	byte[] message = encryptionUtils.encryptMessage(name + ": " + line);
	            clientObjectOutput.writeObject(message);
	            line = localReader.readLine();				
			}
	        localReader.close();
	        clientObjectOutput.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
