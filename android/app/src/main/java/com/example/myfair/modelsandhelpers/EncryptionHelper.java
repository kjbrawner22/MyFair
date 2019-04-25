package com.example.myfair.modelsandhelpers;

import android.util.Base64;

import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionHelper {

    private static EncryptionHelper encryptionHelper = null;
    private static String encryptionKey;

    private EncryptionHelper(){

    }

    public static EncryptionHelper getInstance(){
        if(encryptionHelper == null){
            encryptionHelper = new EncryptionHelper();
        }
        return encryptionHelper;
    }

    /**
     * Helper function to return the secret key
     * @param secretKey - String variable for the key
     * @return returns the variable for the key
     * @throws Exception
     */
    public SecretKey getSecretKey(String secretKey) throws Exception{
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] digestOfPassword = md.digest(secretKey.getBytes("UTF-8"));
        byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
        return new SecretKeySpec(keyBytes, "AES");
    }

    /**
     * Helper function to encrypt the message
     * @return returns a an encrypted String
     */
    public String encryptMsg() {
        return Base64.encodeToString(encryptionKey.getBytes(), Base64.DEFAULT);
    }

    /**
     * Helper function to return an encryption helper
     * @param encryptionKey String value for encrypted string
     * @return returns EncryptionHelper
     */
    public EncryptionHelper encryptionString(String encryptionKey) {
        this.encryptionKey = encryptionKey;
        return encryptionHelper;
    }

    /**
     * Helper function to decrypt a given String
     * @param encryptedText - String that holds encrypted message
     * @return - returns String holding the decrypted message
     */
    public String getDecryptionString(String encryptedText) {
        return new String(Base64.decode(encryptedText.getBytes(), Base64.DEFAULT));
    }
}
