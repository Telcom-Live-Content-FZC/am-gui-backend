package mbs.am.gui.common;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.enterprise.context.ApplicationScoped;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


@ApplicationScoped
public class EncryptionUtils {

private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    
    // In your Oracle code, DBMS_CRYPTO uses a zero-filled IV by default 
    // when no IV is provided. We must match that here.
    private static final byte[] IV = new byte[16]; 

    /**
     * Replicates Oracle's RPAD(key, 32, '0') to ensure 256-bit key length
     */
    private static byte[] prepareKey(String key) {
        byte[] keyBytes = new byte[32]; // 32 bytes = 256 bits
        byte[] originalKeyBytes = key.getBytes(StandardCharsets.UTF_8);
        
        for (int i = 0; i < 32; i++) {
            if (i < originalKeyBytes.length) {
                keyBytes[i] = originalKeyBytes[i];
            } else {
                keyBytes[i] = (byte) '0'; // The '0' padding from your SQL RPAD
            }
        }
        return keyBytes;
    }

    public  String encrypt(String plainText, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(prepareKey(key), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(IV);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

        byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String decrypt(String encryptedText, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(prepareKey(key), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(IV);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

        byte[] decodedBytes = Base64.getMimeDecoder().decode(encryptedText);
        byte[] decrypted = cipher.doFinal(decodedBytes);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

}
