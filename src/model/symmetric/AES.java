package model.symmetric;

import model.AbstractEncryptionAlgorithm;
import model.Constant;
import model.EncryptionUtil;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class AES extends AbstractEncryptionAlgorithm {

    private static final String AES = "AES";

    @Override
    public String encrypt(String plaintext, String key, int keyLength, String mode, String padding) {
        if (padding.equals(Constant.ZERO_PADDING) && (
                mode.equals(Constant.CBC_MODE) || mode.equals(Constant.ECB_MODE) || mode.equals(Constant.OFB_MODE) || mode.equals(Constant.CFB_MODE))) {
            plaintext = EncryptionUtil.padPlaintextWithZeroBytes(plaintext, 16);
            padding = Constant.NO_PADDING;
        }

        if (mode.equals(Constant.GCM_MODE)) {
            padding = Constant.NO_PADDING;
        }

        if (padding.equals(Constant.NO_PADDING) && (mode.equals(Constant.CBC_MODE) || mode.equals(Constant.ECB_MODE))) {
            plaintext = EncryptionUtil.padPlaintextWithZeroBytes(plaintext, 16);
        }

        try {
            SecretKeySpec secretKey = EncryptionUtil.generateSecretKey(AES, key, keyLength);
            Cipher cipher = EncryptionUtil.createCipher(AES, mode, padding);

            byte[] iv = null;

            // Generate IV only if the mode is not ECB
            if (!mode.equals(Constant.ECB_MODE)) {
                int ivLength = mode.equals(Constant.GCM_MODE) ? 12 : 16; // GCM thường dùng IV 12 bytes, CBC dùng 16 bytes
                iv = new byte[ivLength];
                new SecureRandom().nextBytes(iv); // Tạo ngẫu nhiên IV
            }

            // Initialize cipher based on the mode
            if (mode.equals(Constant.GCM_MODE)) {
                GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec);
            } else if (mode.equals(Constant.ECB_MODE)) {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey); // ECB không dùng IV
            } else {
                IvParameterSpec ivSpec = new IvParameterSpec(iv);
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec); // Các chế độ khác (CBC) dùng IV
            }

            // Mã hóa dữ liệu
            byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());

            // Ghép IV với dữ liệu mã hóa để lưu trữ (only if not ECB mode)
            if (iv != null) {
                byte[] combined = new byte[iv.length + encryptedBytes.length];
                System.arraycopy(iv, 0, combined, 0, iv.length);
                System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);
                return Base64.getEncoder().encodeToString(combined);
            }
            // Trả về chuỗi Base64
            return Base64.getEncoder().encodeToString(encryptedBytes);

        } catch (Exception e) {
            throw new RuntimeException("Error while encrypting: " + e.toString(), e);
        }
    }

    @Override
    public String decrypt(String encrypted, String key, int keyLength, String mode, String padding) {
        if (padding.equals(Constant.ZERO_PADDING) &&
                (mode.equals(Constant.CBC_MODE) || mode.equals(Constant.ECB_MODE) || mode.equals(Constant.OFB_MODE) || mode.equals(Constant.CFB_MODE))) {
            padding = Constant.NO_PADDING;
        }

        if (mode.equals(Constant.GCM_MODE)) {
            padding = Constant.NO_PADDING;
        }

        try {
            SecretKeySpec secretKey = EncryptionUtil.generateSecretKey(AES, key, keyLength);
            Cipher cipher = EncryptionUtil.createCipher(AES, mode, padding);

            // Giải mã dữ liệu Base64
            byte[] combined = Base64.getDecoder().decode(encrypted);

            // Tách IV từ dữ liệu kết hợp (kích thước IV phụ thuộc vào mode)
            byte[] iv = null;
            byte[] encryptedBytes;

            if (!mode.equals(Constant.ECB_MODE)) {
                int ivLength = mode.equals(Constant.GCM_MODE) ? 12 : 16; // GCM dùng IV 12 bytes, các chế độ khác (CBC) dùng IV 16 bytes
                iv = new byte[ivLength];
                encryptedBytes = new byte[combined.length - ivLength];

                // Tách IV và encryptedBytes từ mảng combined
                System.arraycopy(combined, 0, iv, 0, ivLength);
                System.arraycopy(combined, ivLength, encryptedBytes, 0, encryptedBytes.length);
            } else {
                encryptedBytes = combined; // ECB mode không có IV
            }

            if (mode.equals(Constant.GCM_MODE)) {
                GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
                cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSpec);
            } else if (mode.equals(Constant.ECB_MODE)) {
                cipher.init(Cipher.DECRYPT_MODE, secretKey); // ECB không dùng IV
            } else {
                IvParameterSpec ivSpec = new IvParameterSpec(iv);
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec); // Các chế độ khác dùng IV (CBC)
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
        return key.length() == 16 || key.length() == 24 || key.length() == 32;
    }

    @Override
    public String generateKey(int keyLength) {
        try {
            return EncryptionUtil.generateKey(keyLength, AES);
        } catch (Exception e) {
            throw new RuntimeException("Error while generating key: " + e.toString());
        }
    }

    @Override
    public String getInvalidKeyMessage() {
        return "Key must be 16, 24, or 32 bytes long for AES.";
    }

    @Override
    public String[] getKeyLengths() {
        return new String[]{"128", "192", "256"};
    }

    @Override
    public String[] getPaddings() {
        return new String[]{Constant.NO_PADDING, Constant.ZERO_PADDING, Constant.PKCS5_PADDING};
    }

    @Override
    public String[] getModes() {
        return new String[]{Constant.GCM_MODE, Constant.CBC_MODE, Constant.OFB_MODE, Constant.CFB_MODE, Constant.ECB_MODE};
    }

    @Override
    public String name() {
        return "AES (Advanced Encryption Standard)";
    }

}