package model.symmetric;

import model.AbstractEncryptionAlgorithm;
import model.Constant;

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
        return 1; // Should never happen if a and modulo are coprime
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

    public static void main(String[] args) {
        AffineCipher cipher = new AffineCipher();
        for (int i = 0; i < 10; i++) {
            System.out.println(cipher.generateKey());
        }
//        String plaintext = "Hello, World! á à ạ ả ấ ầ ậ ẩ ắ ằ ặ ẳ ẻ ẽ";
//        String encrypted = cipher.encrypt(plaintext, "3,5");
//        System.out.println("Encrypted: " + encrypted);
//
//        String decrypted = cipher.decrypt(encrypted, "3,5");
//        System.out.println("Decrypted: " + decrypted);
    }

    @Override
    public String name() {
        return Constant.AFFINE_CIPHER;
    }

    @Override
    public boolean requireKey() {
        return true;
    }
}