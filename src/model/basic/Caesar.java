package model.basic;


import model.AbstractEncryptionAlgorithm;
import model.EncryptionAlgorithm;

import java.util.List;
import java.util.Random;

public class Caesar  extends AbstractEncryptionAlgorithm {

    @Override
    public String encrypt(String plaintext, String key) {
        // Convert the key from String to int
        int shift = Integer.parseInt(key) % Character.MAX_VALUE;
        StringBuilder encrypted = new StringBuilder();

        for (char c : plaintext.toCharArray()) {
            // Shift the character
            char encryptedChar = (char) ((c + shift) % Character.MAX_VALUE);
            encrypted.append(encryptedChar);
        }

        return encrypted.toString();
    }

    @Override
    public String decrypt(String encrypted, String key) {
        // Convert the key from String to int
        int shiftKey;
        try {
            shiftKey = Integer.parseInt(key) % Character.MAX_VALUE;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Key must be a valid integer.");
        }

        StringBuilder decrypted = new StringBuilder();

        for (char c : encrypted.toCharArray()) {
            // Reverse the shift operation
            char decryptedChar = (char) ((c - shiftKey + Character.MAX_VALUE) % Character.MAX_VALUE);
            decrypted.append(decryptedChar);
        }

        return decrypted.toString();
    }



    @Override
    public boolean requireKey() {
        return true;
    }

    @Override
    public boolean isValidKey(String key) {
        try {
            int number = Integer.parseInt(key);
            return number >= 0 && number <= 65535;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public String name() {
        return "Caesar Cipher";
    }


    @Override
    public String generateKey() {
        return String.valueOf(new Random().nextInt(65536));
    }

    @Override
    public String getInvalidKeyMessage() {
        return "Key không hợp lệ! Key phải là số nguyên và nằm trong khoảng từ 0 đến 65,535.";
    }

}
