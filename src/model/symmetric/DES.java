package model.symmetric;

import model.AbstractEncryptionAlgorithm;
import model.Constant;
import model.EncryptionUtil;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.SecureRandom;
import java.util.Base64;

public class DES extends AbstractEncryptionAlgorithm {


    @Override
    public String encrypt(String plaintext, String key, int keyLength, String mode, String padding) {
        if (padding.equals(Constant.NO_PADDING)) {
            plaintext = EncryptionUtil.padPlaintextWithZeroBytes(plaintext, 8); // DES block size is 8 bytes
        }
        if (padding.equals(Constant.ZERO_BYTE_PADDING)) {
            plaintext = EncryptionUtil.padPlaintextWithZeroBytes(plaintext, 8); // DES block size is 8 bytes
            padding = Constant.NO_PADDING;
        }
        if (mode.equals(Constant.CTR_MODE)) {
            padding = Constant.NO_PADDING; // CTR không hỗ trợ padding
        }
        try {
            SecretKeySpec secretKey = EncryptionUtil.generateSecretKey(Constant.DES_CIPHER, key, keyLength);
            Cipher cipher = EncryptionUtil.createCipher(Constant.DES_CIPHER, mode, padding);

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
            SecretKeySpec secretKey = EncryptionUtil.generateSecretKey(Constant.DES_CIPHER, key, keyLength);
            Cipher cipher = EncryptionUtil.createCipher(Constant.DES_CIPHER, mode, padding);

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
    public void decryptFile(String inputPath, String outputPath, String key, int keyLength, String mode, String padding) throws Exception {
        EncryptionUtil.validatePath(inputPath, outputPath);
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        CipherInputStream cis = null;
        // Handle padding based on input
        if (padding.equals(Constant.ZERO_BYTE_PADDING)) {
            padding = Constant.NO_PADDING;
        }
        if (mode.equals(Constant.CTR_MODE)) {
            padding = Constant.NO_PADDING; // CTR mode does not support padding
        }

        try {
            SecretKeySpec secretKey = EncryptionUtil.generateSecretKey(Constant.DES_CIPHER, key, keyLength);
            Cipher cipher = EncryptionUtil.createCipher(Constant.DES_CIPHER, mode, padding);
            bis = new BufferedInputStream(new FileInputStream(inputPath));
            bos = new BufferedOutputStream(new FileOutputStream(outputPath));
            // Extract IV if present
            byte[] iv = null;
            if (!mode.equals(Constant.ECB_MODE)) {
                iv = bis.readNBytes(8);
            }

            // Initialize the cipher for decryption mode
            if (!mode.equals(Constant.ECB_MODE)) {
                IvParameterSpec ivSpec = new IvParameterSpec(iv);
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, secretKey);

            }

            cis = new CipherInputStream(bis, cipher);
            // Giải mã dữ liệu
            byte[] inputBytes = new byte[4096];
            int bytesRead;
            while ((bytesRead = cis.read(inputBytes)) != -1) {
                bos.write(inputBytes, 0, bytesRead);
            }
            bos.flush();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while decrypting file: " + e.getMessage(), e);
        }finally {
            EncryptionUtil.closeStream(cis, null, bis, bos);
        }
    }


    @Override
    public void encryptFile(String inputPath, String outputPath, String key, int keyLength, String mode, String padding) throws Exception {
        EncryptionUtil.validatePath(inputPath, outputPath);
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        CipherOutputStream cos = null;

        if (padding.equals(Constant.ZERO_BYTE_PADDING) || mode.equals(Constant.CTR_MODE)) {
            padding = Constant.NO_PADDING;
        }

        try {
            SecretKeySpec secretKey = EncryptionUtil.generateSecretKey(Constant.DES_CIPHER, key, keyLength);
            Cipher cipher = EncryptionUtil.createCipher(Constant.DES_CIPHER, mode, padding);

            byte[] iv = null;
            if (!mode.equals(Constant.ECB_MODE)) {
                iv = new byte[8]; // DES uses 8-byte IV
                new SecureRandom().nextBytes(iv);
                IvParameterSpec ivSpec = new IvParameterSpec(iv);
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            }

            bis = new BufferedInputStream(new FileInputStream(inputPath));
            bos = new BufferedOutputStream(new FileOutputStream(outputPath));
            cos = new CipherOutputStream(bos, cipher);

            // If there's an IV, prepend it to the encrypted data
            if (iv != null) {
                bos.write(iv);
            }

            byte[] inputBytes = new byte[4096];
            int bytesRead;
            while ((bytesRead = bis.read(inputBytes)) != -1) {
                cos.write(inputBytes, 0, bytesRead);
            }
            cos.flush();

        } catch (Exception e) {
            throw new RuntimeException("Error while encrypting: " + e.toString(), e);
        } finally {
            EncryptionUtil.closeStream(null, cos, bis, bos);
        }
    }

    @Override
    public boolean requireKey() {
        return true;
    }

    @Override
    public boolean isValidKey(String key) {
        byte[] keyDecoded = Base64.getDecoder().decode(key);
        return keyDecoded.length == 8; // DES requires 8-byte key (64 bits)
    }

    @Override
    public String generateKey() {
        try {
            return EncryptionUtil.generateKey(56, Constant.DES_CIPHER);
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
        return Constant.DES_CIPHER;
    }

    public static void main(String[] args) {
        DES alg = new DES();
        String inputPath = "C:\\Users\\FPT SHOP\\Documents\\New Folder\\hc.jpg";

        String[] keyLengths = alg.getKeyLengths();
        String[] paddings = alg.getPaddings();
        String[] modes = alg.getModes();
        for (String keyLength : keyLengths) {
            int keyLengthInBits = Integer.parseInt(keyLength);
            String key = alg.generateKey(keyLengthInBits);
            for (String padding : paddings) {
                for (String mode : modes) {
                    String enPath = String.format("C:\\Users\\FPT SHOP\\Documents\\New Folder\\1\\en_%s_%s_%s.jpg",keyLength, mode, padding);
                    String dePath = String.format("C:\\Users\\FPT SHOP\\Documents\\New Folder\\de_%s_%s_%s.jpg",keyLength, mode, padding);
                    try {
                        alg.encryptFile(inputPath, enPath, key, keyLengthInBits, mode, padding);
                        alg.decryptFile(enPath, dePath, key, keyLengthInBits, mode, padding);
                    } catch (Exception e) {
                        System.out.println("Error with Key Length: " + keyLength + ", Padding: " + padding + ", Mode: " + mode);
                        e.printStackTrace();
                        System.out.println("=====================================");
                        return;
                    }
                }
            }
        }
    }
}
