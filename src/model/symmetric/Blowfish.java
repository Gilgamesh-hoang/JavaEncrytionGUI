package model.symmetric;

import model.AbstractEncryptionAlgorithm;
import model.Constant;
import model.EncryptionUtil;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class Blowfish extends AbstractEncryptionAlgorithm {

    // Constants for algorithm name

    @Override
    public String encrypt(String plaintext, String key, int keyLength, String mode, String padding) {
        if (padding.equals(Constant.NO_PADDING) && (mode.equals(Constant.CBC_MODE) || mode.equals(Constant.ECB_MODE))) {
            plaintext = EncryptionUtil.padPlaintextWithZeroBytes(plaintext, 16);
        }
        if (mode.equals(Constant.CTR_MODE)) {
            padding = Constant.NO_PADDING; // CTR không hỗ trợ padding
        }
        try {
            SecretKeySpec secretKey = EncryptionUtil.generateSecretKey(Constant.BLOWFISH_CIPHER, key, keyLength);
            Cipher cipher = EncryptionUtil.createCipher(Constant.BLOWFISH_CIPHER, mode, padding);

            // Initialization vector (IV) for modes that require it
            byte[] iv = null;
            if (!mode.equals(Constant.ECB_MODE)) {
                iv = new byte[8]; // Blowfish uses 8-byte IV
                new SecureRandom().nextBytes(iv);
                IvParameterSpec ivSpec = new IvParameterSpec(iv);
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            }

            // Encrypt the plaintext
            byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());

            // If there's an IV, prepend it to the encrypted data
            if (iv != null) {
                byte[] combined = new byte[iv.length + encryptedBytes.length];
                System.arraycopy(iv, 0, combined, 0, iv.length);
                System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);
                return Base64.getEncoder().encodeToString(combined);
            }

            // Return encrypted data as a Base64 string
            return Base64.getEncoder().encodeToString(encryptedBytes);

        } catch (Exception e) {
            throw new RuntimeException("Error while encrypting: " + e.toString(), e);
        }
    }

    @Override
    public String decrypt(String encrypted, String key, int keyLength, String mode, String padding) {
        if (mode.equals(Constant.CTR_MODE)) {
            padding = Constant.NO_PADDING; // CTR không hỗ trợ padding
        }
        try {
            // Generate secret key from input key string
            SecretKeySpec secretKey = EncryptionUtil.generateSecretKey(Constant.BLOWFISH_CIPHER, key, keyLength);
            Cipher cipher = EncryptionUtil.createCipher(Constant.BLOWFISH_CIPHER, mode, padding);

            // Decode the encrypted data from Base64
            byte[] combined = Base64.getDecoder().decode(encrypted);

            // Extract IV from the combined data (if mode is not ECB)
            byte[] iv = null;
            byte[] encryptedBytes;
            if (!mode.equals(Constant.ECB_MODE)) {
                iv = new byte[8]; // Blowfish uses 8-byte IV
                encryptedBytes = new byte[combined.length - iv.length];
                System.arraycopy(combined, 0, iv, 0, iv.length);
                System.arraycopy(combined, iv.length, encryptedBytes, 0, encryptedBytes.length);

                IvParameterSpec ivSpec = new IvParameterSpec(iv);
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            } else {
                encryptedBytes = combined; // No IV for ECB mode
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
            }
            return EncryptionUtil.decryptAndRemovePadding(cipher, encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error while decrypting: " + e.toString(), e);
        }
    }

    @Override
    public boolean requireKey() {
        return true;
    }

    @Override
    public boolean isValidKey(String key) {
        byte[] keyDecoded = Base64.getDecoder().decode(key);
        // Blowfish supports key sizes from 32 bits (4 bytes) to 448 bits (56 bytes)
        return keyDecoded.length >=4 && keyDecoded.length <=56 ;
    }

    @Override
    public String generateKey(int keyLength) {
        try {
            return EncryptionUtil.generateKey(keyLength, Constant.BLOWFISH_CIPHER);
        } catch (Exception e) {
            throw new RuntimeException("Error while generating key: " + e.toString());
        }
    }

    @Override
    public String getInvalidKeyMessage() {
        return "Blowfish key must be between 32 bits and 448 bits";
    }

    @Override
    public String[] getKeyLengths() {
        return new String[]{"32", "128", "256", "448"};
    }

    @Override
    public String[] getPaddings() {
        return new String[]{Constant.NO_PADDING, Constant.PKCS5_PADDING};
    }

    @Override
    public String[] getModes() {
        return new String[]{Constant.ECB_MODE, Constant.CBC_MODE, Constant.CFB_MODE, Constant.OFB_MODE, Constant.CTR_MODE};
    }


    @Override
    public String name() {
        return Constant.BLOWFISH_CIPHER;
    }
}
