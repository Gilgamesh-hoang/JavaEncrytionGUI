package model.symmetric;

import model.AbstractEncryptionAlgorithm;
import model.Constant;

import java.io.*;

public class VigenereCipher extends AbstractEncryptionAlgorithm {

    @Override
    public void encryptFile(String inputPath, String outputPath, String key, int keyLength, String mode, String padding) throws Exception {
        processFile(inputPath, outputPath, key);
    }

    @Override
    public void decryptFile(String inputPath, String outputPath, String key, int keyLength, String mode, String padding) throws Exception {
        processFile(inputPath, outputPath, key);  // Vì giải mã Vigenère cũng tương tự mã hóa.
    }

    private void processFile(String inputPath, String outputPath, String key) throws IOException {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputPath));
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputPath))) {

            byte[] inputBytes = new byte[4096];  // Đọc dữ liệu theo từng khối 4KB.
            int bytesRead;
            int keyLength = key.length();

            while ((bytesRead = bis.read(inputBytes)) != -1) {
                for (int i = 0; i < bytesRead; i++) {
                    // Thực hiện XOR giữa byte của dữ liệu và byte tương ứng trong khóa.
                    inputBytes[i] = (byte) (inputBytes[i] ^ key.charAt(i % keyLength));
                }
                bos.write(inputBytes, 0, bytesRead);
            }
            bos.flush();
        } catch (IOException e) {
            throw new IOException("Lỗi khi xử lý file: " + e.getMessage(), e);
        }
    }

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


    @Override
    public String generateKey(long keyLength) {
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

    public static void main(String[] args) {
        VigenereCipher alg = new VigenereCipher();

        // Generate a key
//        String key = alg.generateKey(256);
//        System.out.println("Generated Key: " + key);
//
//        // Check if the key is valid
//        boolean isValid = alg.isValidKey(key);
//        System.out.println("Is the key valid? " + isValid);
//
//        // Encrypt a message
//        String plaintext = "Hello,!@#%^&(). World! á à ạ ả ấ ầ ậ ẩ ắ ằ ặ ẳ ẻ ẽ";
//        System.out.println("Plaintext: " + plaintext);
//        String ciphertext = alg.encrypt(plaintext, key);
//        System.out.println("Ciphertext: " + ciphertext);
//
//        // Decrypt the message
//        String decryptedText = alg.decrypt(ciphertext, key);
//        System.out.println("same? " + decryptedText.equals(plaintext));

        String inputPath = "C:\\Users\\FPT SHOP\\Documents\\New Folder\\hc.jpg";

        for (String keyLength : alg.getKeyLengths()) {
            int keyLengthInBits = Integer.parseInt(keyLength);
            String key = alg.generateKey((long)keyLengthInBits);
            String enPath = String.format("C:\\Users\\FPT SHOP\\Documents\\New Folder\\1\\en_%s.jpg", keyLength);
            String dePath = String.format("C:\\Users\\FPT SHOP\\Documents\\New Folder\\de_%s.jpg", keyLength);
            try {
                alg.encryptFile(inputPath, enPath, key, 0, null, null);
                alg.decryptFile(enPath, dePath, key, 0, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}