package com.icesi;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.SecretKeySpec;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public class EncryptionUtils {

    private PrivateKey privateKey;
    private PublicKey  publicKey;
    private PublicKey  receivedPublicKey;
    private byte[]     secretKey;

    /**
     * encrypts a message with the algorithm AES128
     * @param message to be encrypted
     * @return an array of bytes with the encrypted message
     */
    public byte[] encryptMessage(final String message) {
        byte[] encryptedMessage = null;
        try {
//          final SecretKeySpec keySpec = new SecretKeySpec(secretKey, "DES");
//          final Cipher        cipher  = Cipher.getInstance("DES/ECB/PKCS5Padding");
//          cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            Key aesKey = new SecretKeySpec(secretKey, "AES");
            Cipher cf = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cf.init(Cipher.ENCRYPT_MODE,aesKey);
            encryptedMessage = cf.doFinal(message.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            return encryptedMessage;
        }
    }

    /**
     * Generates the common secret key between the shared public key and my private key
     */
    public void generateCommonSecretKey() {

        try {
            final KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
            keyAgreement.init(privateKey);
            keyAgreement.doPhase(receivedPublicKey, true);
            secretKey = shortenSecretKey(keyAgreement.generateSecret());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates the private and public key
     */
    public void generateKeys() {

        try {
            final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
            keyPairGenerator.initialize(1024);
            final KeyPair keyPair = keyPairGenerator.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey  = keyPair.getPublic();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * Decrypts an encrypted message with the algorithm AES128
     * @param message to be decrypted
     * @return original message
     */
    public String decryptMessage(final byte[] message) {

        String secretMessage = null;
        try {
//          final SecretKeySpec keySpec = new SecretKeySpec(secretKey, "DES");
//          final Cipher        cipher  = Cipher.getInstance("DES/ECB/PKCS5Padding");
//          cipher.init(Cipher.DECRYPT_MODE, keySpec);
//          secretMessage = new String(cipher.doFinal(message));
            Key aesKey = new SecretKeySpec(secretKey, "AES");
            Cipher cf = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cf.init(Cipher.DECRYPT_MODE,aesKey);
            secretMessage = new String(cf.doFinal(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            return secretMessage;
        }
    }

    /**
     * In a real life example you must serialize the public key for transferring.
     * @param  anotherConnectionPublicKey public key
     */
    public void receivePublicKeyFrom(PublicKey anotherConnectionPublicKey) {
        receivedPublicKey = anotherConnectionPublicKey;
    }

    /**
     * 1024 bit symmetric key size is so big for AES so we must shorten the key size
     * @param   longKey
     *
     * @return
     */
    private byte[] shortenSecretKey(final byte[] longKey) {

        try {

            // Use 16 bytes (128 bits) for AES, 8 bytes (64 bits) for DES, 6 bytes (48 bits) for Blowfish
            final byte[] shortenedKey = new byte[16];

            System.arraycopy(longKey, 0, shortenedKey, 0, shortenedKey.length);

            return shortenedKey;

            // Below lines can be more secure
            // final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // final DESKeySpec       desSpec    = new DESKeySpec(longKey);
            //
            // return keyFactory.generateSecret(desSpec).getEncoded();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
