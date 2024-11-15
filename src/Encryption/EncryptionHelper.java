package Encryption;

import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class EncryptionHelper {

    private final Cipher cipher;

    byte[] keyBytes = new byte[] {
            0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
            0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
            0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17 };
    private final SecretKey key = new SecretKeySpec(keyBytes, "AES");

    public EncryptionHelper() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        String BOUNCY_CASTLE_PROVIDER_IDENTIFIER = "BC";
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", BOUNCY_CASTLE_PROVIDER_IDENTIFIER);
    }

    public byte[] encrypt(byte[] plainText, byte[] initializationVector) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(initializationVector));
        return cipher.doFinal(plainText);
    }

    public byte[] decrypt(byte[] cipherText, byte[] initializationVector) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(initializationVector));
        return cipher.doFinal(cipherText);
    }

}
