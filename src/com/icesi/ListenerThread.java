package com.icesi;

import java.io.ObjectInputStream;

public class ListenerThread extends Thread{

    private EncryptionUtils encryptionUtils;
    private ObjectInputStream clientObjectInput;
	private boolean stop;
    
    public ListenerThread(EncryptionUtils encryptionUtils, ObjectInputStream clientObjectInput, boolean stop) {
    	this.encryptionUtils = encryptionUtils;
		this.clientObjectInput = clientObjectInput;
		this.stop = stop;
	}
    
    /**
     * Runs the async task for receiving
     */
	public void run() {

		try {
			while (!stop) {
				System.out.println(encryptionUtils.decryptMessage((byte[]) clientObjectInput.readObject()));
			}
//			clientObjectInput.close();
		} catch (Exception e) {
			System.out.println("Connection ended");
		}
	}
	
}
