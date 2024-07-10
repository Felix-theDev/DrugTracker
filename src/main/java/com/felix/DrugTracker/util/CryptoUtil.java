package com.felix.DrugTracker.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;

public class CryptoUtil {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    public static byte[] encrypt(String key, String data) throws Exception {
        System.out.println("Key is " + key + " Data is " + data);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, generateKey(key));
        System.out.println(Arrays.toString(data.getBytes()));
        return cipher.doFinal(data.getBytes());
    }

    public static String encryptToBase64(String key, String data) throws Exception {
        byte[] encryptedData = encrypt(key, data);
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    public static byte[] decrypt(String key, byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, generateKey(key));

        return cipher.doFinal(data);
    }

    private static SecretKeySpec generateKey(String key) {
        byte[] keyBytes = Arrays.copyOf(key.getBytes(), 16); // Adjust to 16 bytes (128 bits) for AES-128
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }
}
