package model.symmetric;

import model.AbstractEncryptionAlgorithm;
import model.Constant;
import model.EncryptionUtil;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.SecureRandom;
import java.util.Base64;

public class AES extends AbstractEncryptionAlgorithm {


    @Override
    public String encrypt(String plaintext, String key, int keyLength, String mode, String padding) {
        keyLength = Base64.getDecoder().decode(key).length * 8;
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
            SecretKeySpec secretKey = EncryptionUtil.generateSecretKey(Constant.AES_CIPHER, key, keyLength);
            Cipher cipher = EncryptionUtil.createCipher(Constant.AES_CIPHER, mode, padding);

            byte[] iv = null;

            if (!mode.equals(Constant.ECB_MODE)) {
                int ivLength = mode.equals(Constant.GCM_MODE) ? 12 : 16; // GCM thường dùng IV 12 bytes, CBC dùng 16 bytes
                iv = new byte[ivLength];
                new SecureRandom().nextBytes(iv); // Tạo ngẫu nhiên IV
            }

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
    public void encryptFile(String inputPath, String outputPath, String key, int keyLength, String mode, String padding) throws Exception {
        EncryptionUtil.validatePath(inputPath, outputPath);
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        CipherOutputStream cos = null;

        // Kiểm tra padding
        if (padding.equals(Constant.ZERO_PADDING) && (
                mode.equals(Constant.CBC_MODE) || mode.equals(Constant.ECB_MODE) || mode.equals(Constant.OFB_MODE) || mode.equals(Constant.CFB_MODE))) {
            padding = Constant.NO_PADDING;
        }

        if (mode.equals(Constant.GCM_MODE)) {
            padding = Constant.NO_PADDING;
        }

        try {
            // Tạo secret key và cipher
            SecretKeySpec secretKey = EncryptionUtil.generateSecretKey(Constant.AES_CIPHER, key, keyLength);
            Cipher cipher = EncryptionUtil.createCipher(Constant.AES_CIPHER, mode, padding);

            byte[] iv = null;

            // Tạo IV nếu chế độ không phải ECB
            if (!mode.equals(Constant.ECB_MODE)) {
                int ivLength = mode.equals(Constant.GCM_MODE) ? 12 : 16; // GCM thường dùng IV 12 bytes, CBC dùng 16 bytes
                iv = new byte[ivLength];
                new SecureRandom().nextBytes(iv); // Tạo ngẫu nhiên IV
            }

            // Khởi tạo cipher dựa trên chế độ mã hóa
            if (mode.equals(Constant.GCM_MODE)) {
                GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec);
            } else if (mode.equals(Constant.ECB_MODE)) {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey); // ECB không dùng IV
            } else {
                IvParameterSpec ivSpec = new IvParameterSpec(iv);
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec); // Các chế độ khác dùng IV
            }


            bis = new BufferedInputStream(new FileInputStream(inputPath));
            bos = new BufferedOutputStream(new FileOutputStream(outputPath));
            cos = new CipherOutputStream(bos, cipher);


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
            throw new RuntimeException(e.toString(), e);
        } finally {
            EncryptionUtil.closeStream(null, cos, bis, bos);
        }
    }

    @Override
    public String decrypt(String encrypted, String key, int keyLength, String mode, String padding) {
        keyLength = Base64.getDecoder().decode(key).length * 8;
        if (padding.equals(Constant.ZERO_PADDING) &&
                (mode.equals(Constant.CBC_MODE) || mode.equals(Constant.ECB_MODE) || mode.equals(Constant.OFB_MODE) || mode.equals(Constant.CFB_MODE))) {
            padding = Constant.NO_PADDING;
        }

        if (mode.equals(Constant.GCM_MODE)) {
            padding = Constant.NO_PADDING;
        }

        try {
            SecretKeySpec secretKey = EncryptionUtil.generateSecretKey(Constant.AES_CIPHER, key, keyLength);
            Cipher cipher = EncryptionUtil.createCipher(Constant.AES_CIPHER, mode, padding);

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
    public void decryptFile(String inputPath, String outputPath, String key, int keyLength, String mode, String padding) throws Exception {
        EncryptionUtil.validatePath(inputPath, outputPath);
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        CipherInputStream cis = null;

        if (padding.equals(Constant.ZERO_PADDING) &&
                (mode.equals(Constant.CBC_MODE) || mode.equals(Constant.ECB_MODE) || mode.equals(Constant.OFB_MODE) || mode.equals(Constant.CFB_MODE))) {
            padding = Constant.NO_PADDING;
        }

        if (mode.equals(Constant.GCM_MODE)) {
            padding = Constant.NO_PADDING;
        }

        try {
            // Tạo SecretKeySpec từ khóa
            SecretKeySpec secretKey = EncryptionUtil.generateSecretKey(Constant.AES_CIPHER, key, keyLength);
            Cipher cipher = EncryptionUtil.createCipher(Constant.AES_CIPHER, mode, padding);

            bis = new BufferedInputStream(new FileInputStream(inputPath));
            bos = new BufferedOutputStream(new FileOutputStream(outputPath));

            // Tách IV từ dữ liệu file (nếu chế độ không phải ECB)
            byte[] iv = null;

            if (!mode.equals(Constant.ECB_MODE)) {
                int ivLength = mode.equals(Constant.GCM_MODE) ? 12 : 16; // GCM dùng IV 12 bytes, CBC dùng IV 16 bytes
                iv = bis.readNBytes(ivLength);
            }

            // Khởi tạo cipher để giải mã
            if (mode.equals(Constant.GCM_MODE)) {
                GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
                cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSpec);
            } else if (mode.equals(Constant.ECB_MODE)) {
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
            } else {
                IvParameterSpec ivSpec = new IvParameterSpec(iv);
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
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
            throw new RuntimeException("Error while decrypting file: " + e.toString(), e);
        } finally {
            EncryptionUtil.closeStream(cis, null, bis, bos);
        }
    }


    @Override
    public boolean requireKey() {
        return true;
    }

    @Override
    public boolean isValidKey(String key) {
        byte[] keyDecoded = Base64.getDecoder().decode(key);
        return keyDecoded.length == 16 || keyDecoded.length == 24 || keyDecoded.length == 32;
    }

    @Override
    public String generateKey(long keyLength) {
        try {
            String s = EncryptionUtil.generateKey((int)keyLength, Constant.AES_CIPHER);
            return s;
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
        return Constant.AES_CIPHER;
    }

    public static void main(String[] args) {
        AES aes = new AES();
//        String inputPath = "C:\\Users\\FPT SHOP\\Documents\\New Folder\\DES.json";
//        String enPath = "C:\\Users\\FPT SHOP\\Documents\\New Folder\\DES1.json";
//        String dePath = "C:\\Users\\FPT SHOP\\Documents\\New Folder\\DES2.json";
//        String key = aes.generateKey(128);
//        int keyLength = 128; // DES key length is 56 bits
//        String mode = Constant.GCM_MODE; // or any other mode you want to use
//        String padding = Constant.PKCS5_PADDING; // or any other padding you want to use
//
//        try {
//            aes.encryptFile(inputPath, enPath, key, keyLength, mode, padding);
//            aes.decryptFile(enPath, dePath, key, keyLength, mode, padding);
//            System.out.println("Decryption completed. Check the output file.");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        String inputPath = "C:\\Users\\FPT SHOP\\Documents\\New Folder\\hc.jpg";

        String[] keyLengths = aes.getKeyLengths();
        String[] paddings = aes.getPaddings();
        String[] modes = aes.getModes();
        for (String keyLength : keyLengths) {
            int keyLengthInBits = Integer.parseInt(keyLength);
            String key = aes.generateKey((long)keyLengthInBits);
            for (String padding : paddings) {
                for (String mode : modes) {
                    String enPath = String.format("C:\\Users\\FPT SHOP\\Documents\\New Folder\\1\\en_%s_%s_%s.jpg",keyLength, mode, padding);
                    String dePath = String.format("C:\\Users\\FPT SHOP\\Documents\\New Folder\\de_%s_%s_%s.jpg",keyLength, mode, padding);
                    try {
                        aes.encryptFile(inputPath, enPath, key, keyLengthInBits, mode, padding);
                        aes.decryptFile(enPath, dePath, key, keyLengthInBits, mode, padding);
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