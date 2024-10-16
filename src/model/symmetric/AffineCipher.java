package model.symmetric;

import model.AbstractEncryptionAlgorithm;
import model.Constant;
import model.EncryptionUtil;

import java.io.*;
import java.util.Random;

public class AffineCipher extends AbstractEncryptionAlgorithm {
    private static final int MODULO = 65536;

    @Override
    public String generateKey() {
        Random random = new Random();
        int a;
        // Generate 'a' that is coprime with modulo
        do {
            a = random.nextInt(MODULO); // Random a from 0 to modulo-1
        } while (gcd(a, MODULO) != 1); // Ensure a is coprime with modulo

        // Generate 'b' in the range [0, modulo-1]
        int b = random.nextInt(MODULO);

        // Return the key as a comma-separated string "a,b"
        return a + "," + b;
    }

    private int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }

    // Find modular inverse of a under modulo
    private int modInverse(int a) {
        a = a % MODULO;
        for (int x = 1; x < MODULO; x++) {
            if ((a * x) % MODULO == 1) return x;
        }
        throw new ArithmeticException("Không tìm thấy nghịch đảo modular của " + a);
    }

    @Override
    // Encrypt character
    public String encrypt(String plaintext, String key) {
        int a = Integer.parseInt(key.split(",")[0]);
        int b = Integer.parseInt(key.split(",")[1]);
        StringBuilder ciphertext = new StringBuilder();

        for (char character : plaintext.toCharArray()) {
            int x = character; // Get Unicode code point
            int encryptedChar = (a * x + b) % MODULO;
            ciphertext.append((char) encryptedChar); // Convert back to char
        }
        return ciphertext.toString();
    }

    @Override
    // Decrypt character
    public String decrypt(String ciphertext, String key) {
        int a = Integer.parseInt(key.split(",")[0]);
        int b = Integer.parseInt(key.split(",")[1]);
        StringBuilder plaintext = new StringBuilder();
        int aInverse = modInverse(a);

        for (char character : ciphertext.toCharArray()) {
            int y = character; // Get Unicode code point
            int decryptedChar = (aInverse * (y - b + MODULO)) % MODULO; // Handle negative values
            plaintext.append((char) decryptedChar); // Convert back to char
        }
        return plaintext.toString();
    }

    @Override
    public void encryptFile(String inputPath, String outputPath, String key, int keyLength, String mode, String padding) throws Exception {
        int a = Integer.parseInt(key.split(",")[0]);
        int b = Integer.parseInt(key.split(",")[1]);
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(inputPath));
            bos = new BufferedOutputStream(new FileOutputStream(outputPath));
            byte[] inputBytes = new byte[4096];
            int bytesRead;
            while ((bytesRead = bis.read(inputBytes)) != -1) {
                for (int i = 0; i < bytesRead; i++) {
                    inputBytes[i] = (byte) ((a * (inputBytes[i] & 0xFF) + b) % 256);
                }
                bos.write(inputBytes, 0, bytesRead);
            }
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Lỗi khi mã hóa file");
        } finally {
            EncryptionUtil.closeStream(null, null, bis, bos);
        }
    }

    @Override
    public void decryptFile(String inputPath, String outputPath, String key, int keyLength, String mode, String padding) throws Exception {
        int a = Integer.parseInt(key.split(",")[0]);
        int b = Integer.parseInt(key.split(",")[1]);
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        int aInverse = modInverse(a); // Tính nghịch đảo modular của a

        try {
            bis = new BufferedInputStream(new FileInputStream(inputPath));
            bos = new BufferedOutputStream(new FileOutputStream(outputPath));
            byte[] inputBytes = new byte[4096];
            int bytesRead;
            while ((bytesRead = bis.read(inputBytes)) != -1) {

                // Giải mã từng byte
                for (int i = 0; i < bytesRead; i++) {
                    inputBytes[i] = (byte) (aInverse * ((inputBytes[i] & 0xFF) - b + 256) % 256);
                }
                bos.write(inputBytes, 0, bytesRead);
            }
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Lỗi khi mã hóa file");
        } finally {
            EncryptionUtil.closeStream(null, null, bis, bos);
        }
    }

    @Override
    public boolean isValidKey(String key) {
        try {
            String[] parts = key.split(",");

            // Check if the key has exactly 2 parts
            if (parts.length != 2) {
                return false;
            }

            int a = Integer.parseInt(parts[0].trim());
            int b = Integer.parseInt(parts[1].trim());

            // Check if 'a' is coprime with MODULO and 'b' is in the correct range
            return gcd(a, MODULO) == 1 && b >= 0 && b <= MODULO - 1;

        } catch (NumberFormatException e) {
            // Return false if the key format is invalid
            return false;
        }
    }


    @Override
    public String getInvalidKeyMessage() {
        return "Số a hoặc b không hợp lệ";
    }


    @Override
    public String name() {
        return Constant.AFFINE_CIPHER;
    }

    @Override
    public boolean requireKey() {
        return true;
    }

    public static void main(String[] args) {
        AffineCipher alg = new AffineCipher();
        String inputPath = "C:\\Users\\FPT SHOP\\Documents\\New Folder\\hc.jpg";

        for (int i = 0; i < 2; i++) {
            String key = alg.generateKey();
            String enPath = String.format("C:\\Users\\FPT SHOP\\Documents\\New Folder\\1\\en_%s_.jpg", i + "");
            String dePath = String.format("C:\\Users\\FPT SHOP\\Documents\\New Folder\\de_%s.jpg", i + "");
            try {
                alg.encryptFile(inputPath, enPath, key, 0, null, null);
                alg.decryptFile(enPath, dePath, key, 0, null, null);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("=====================================");
            }
        }
    }
}