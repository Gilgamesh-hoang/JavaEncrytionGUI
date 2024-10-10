package model.symmetric;

import model.AbstractEncryptionAlgorithm;
import model.Constant;

public class VigenereCipher extends AbstractEncryptionAlgorithm {

    @Override
    public String encrypt(String plaintext, String key) {
        StringBuilder ciphertext = new StringBuilder();
        int keyIndex = 0;

        for (int i = 0; i < plaintext.length(); i++) {
            char c = plaintext.charAt(i);
            char shift = key.charAt(keyIndex % key.length());
            keyIndex++;

            // Mã hóa bằng cách sử dụng mã Unicode
            c = (char) ((c + shift) % Character.MAX_VALUE);

            ciphertext.append(c);
        }
        return ciphertext.toString();
    }

    @Override
    public String decrypt(String ciphertext, String key) {
        StringBuilder plaintext = new StringBuilder();
        int keyIndex = 0;

        for (int i = 0; i < ciphertext.length(); i++) {
            char c = ciphertext.charAt(i);
            char shift = key.charAt(keyIndex % key.length());
            keyIndex++;

            // Giải mã bằng cách sử dụng mã Unicode
            c = (char) ((c - shift + Character.MAX_VALUE) % Character.MAX_VALUE);

            plaintext.append(c);
        }
        return plaintext.toString();
    }


    public static void main(String[] args) {
        VigenereCipher vigenereCipher = new VigenereCipher();

        // Generate a key
        String key = vigenereCipher.generateKey(256);
        System.out.println("Generated Key: " + key);

        // Check if the key is valid
        boolean isValid = vigenereCipher.isValidKey(key);
        System.out.println("Is the key valid? " + isValid);

        // Encrypt a message
        String plaintext = "Hello,!@#%^&(). World! á à ạ ả ấ ầ ậ ẩ ắ ằ ặ ẳ ẻ ẽ";
//        String plaintext = "Hello, World! á à ạ ả ấ ầ ậ ẩ ắ ằ ặ ẳ ẻ ẽ";
        System.out.println("Plaintext: " + plaintext);
        String ciphertext = vigenereCipher.encrypt(plaintext, key);
        System.out.println("Ciphertext: " + ciphertext);

        // Decrypt the message
        String decryptedText = vigenereCipher.decrypt(ciphertext, key);
        System.out.println("Decrypted text: " + decryptedText);
    }

    @Override
    public String generateKey(int keyLength) {
        keyLength /= 8;
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < keyLength; i++) {
            key.append((char) ('a' + (int) (Math.random() * 26)));
        }
        return key.toString();
    }

    @Override
    public boolean isValidKey(String key) {
        if (key.isBlank())
            return false;
        for (char c : key.toCharArray()) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getInvalidKeyMessage() {
        return "Key must contain only letters";
    }

    @Override
    public String[] getKeyLengths() {
        return new String[]{"128", "192", "256", "512"};
    }

    @Override
    public String name() {
        return Constant.VIGENERE_CIPHER;
    }

    @Override
    public boolean requireKey() {
        return true;
    }
}