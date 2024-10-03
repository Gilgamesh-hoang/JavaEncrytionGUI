package model.symmetric;

import model.AbstractEncryptionAlgorithm;
import model.Constant;
import model.EncryptionUtil;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class DES extends AbstractEncryptionAlgorithm {

    private static final String DES_ALGORITHM = "DES";

    @Override
    public String encrypt(String plaintext, String key, int keyLength, String mode, String padding) {
        if (padding.equals(Constant.NO_PADDING)) {
            plaintext = EncryptionUtil.padPlaintextWithZeroBytes(plaintext,8); // DES block size is 8 bytes
        }
        if (padding.equals(Constant.ZERO_BYTE_PADDING)) {
            plaintext = EncryptionUtil.padPlaintextWithZeroBytes(plaintext, 8); // DES block size is 8 bytes
            padding = Constant.NO_PADDING;
        }
        if (mode.equals(Constant.CTR_MODE)) {
            padding = Constant.NO_PADDING; // CTR không hỗ trợ padding
        }
        try {
            SecretKeySpec secretKey = EncryptionUtil.generateSecretKey(DES_ALGORITHM, key, keyLength);
            Cipher cipher = EncryptionUtil.createCipher(DES_ALGORITHM, mode, padding);

            // Initialization vector (IV) for modes that require it
            byte[] iv = null;
            // Initialize the cipher for encryption mode
            if (!mode.equals(Constant.ECB_MODE)) {
                iv = new byte[8]; // DES uses 8-byte IV
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

    // Method to decrypt encrypted data using DES
    @Override
    public String decrypt(String encrypted, String key, int keyLength, String mode, String padding) {
        if (padding.equals(Constant.ZERO_BYTE_PADDING)) {
            padding = Constant.NO_PADDING;
        }
        if (mode.equals(Constant.CTR_MODE)) {
            padding = Constant.NO_PADDING; // CTR không hỗ trợ padding
        }
        try {
            SecretKeySpec secretKey = EncryptionUtil.generateSecretKey(DES_ALGORITHM, key, keyLength);
            Cipher cipher = EncryptionUtil.createCipher(DES_ALGORITHM, mode, padding);

            // Decode the encrypted data from Base64
            byte[] combined = Base64.getDecoder().decode(encrypted);

            // Extract IV from the combined data (if mode is not ECB)
            byte[] iv = null;
            byte[] encryptedBytes;
            if (!mode.equals(Constant.ECB_MODE)) {
                iv = new byte[8]; // DES uses 8-byte IV
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
        return key != null && key.length() == 8; // DES requires 8-byte key (64 bits)
    }

    @Override
    public String generateKey() {
        try {
            return EncryptionUtil.generateKey(56, DES_ALGORITHM);
        } catch (Exception e) {
            throw new RuntimeException("Error while generating key: " + e.toString());
        }
    }

    @Override
    public String generateKey(int keyLength) {
        if (keyLength == 56)
            return generateKey();
        else
            throw new IllegalArgumentException("Invalid key length for DES. DES requires a key of 8 bytes (56 bits).");
    }

    @Override
    public String getInvalidKeyMessage() {
        return "Invalid key. DES requires a key of 8 bytes (64 bits).";
    }

    @Override
    public String[] getKeyLengths() {
        return new String[]{"56"}; // DES key length is 56 bits
    }

    @Override
    public String[] getPaddings() {
        return new String[]{Constant.NO_PADDING, Constant.PKCS5_PADDING, Constant.ISO10126_PADDING, Constant.ZERO_BYTE_PADDING};
    }

    @Override
    public String[] getModes() {
        return new String[]{Constant.ECB_MODE, Constant.CBC_MODE, Constant.CFB_MODE, Constant.OFB_MODE, Constant.CTR_MODE};
    }

    @Override
    public String name() {
        return "DES (Data Encryption Standard)";
    }
}
