package model.symmetric;

import model.AbstractEncryptionAlgorithm;
import model.Constant;

import java.util.*;

public class PermutationCipher extends AbstractEncryptionAlgorithm {

    @Override
//    public String encrypt(String plaintext, String keyString) {
//        int blockSize = keyString.length();
//        int[] key = stringToIntArray(keyString);
//        StringBuilder ciphertext = new StringBuilder();
//        int index = 0;
//
//        while (index < plaintext.length()) {
//            // Lấy một khối văn bản
//            String block = plaintext.substring(index, Math.min(index + blockSize, plaintext.length()));
//
//            // Nếu khối không đủ kích thước, thêm ký tự đệm
//            if (block.length() < blockSize) {
//                int paddingLength = blockSize - block.length();
//                for (int i = 0; i < paddingLength; i++) {
//                    block += "X"; // Ký tự đệm
//                }
//            }
//
//            // Chuyển khối thành mảng ký tự
//            char[] encryptedBlock = new char[blockSize];
//            for (int i = 0; i < blockSize; i++) {
//                encryptedBlock[i] = block.charAt(key[i]);
//            }
//
//            ciphertext.append(encryptedBlock);
//            index += blockSize;
//        }
//
//        return ciphertext.toString();
//    }
    public String encrypt(String plaintext, String keyString) {
        int[] key = stringToIntArray(keyString);
        int blockSize = key.length;
        StringBuilder ciphertext = new StringBuilder();
        int index = 0;

        while (index < plaintext.length()) {
            // Lấy một khối văn bản
            String block = plaintext.substring(index, Math.min(index + blockSize, plaintext.length()));

            // Nếu khối không đủ kích thước, thêm ký tự đệm
            if (block.length() < blockSize) {
                int paddingLength = blockSize - block.length();
                for (int i = 0; i < paddingLength; i++) {
                    block += "X"; // Ký tự đệm
                }
            }

            // Chuyển khối thành mảng ký tự
            char[] encryptedBlock = new char[block.length()]; // Đảm bảo chỉ sử dụng kích thước khối văn bản

            // Chỉ mã hóa các ký tự trong khối, tránh truy cập vượt quá giới hạn mảng
            for (int i = 0; i < block.length(); i++) {
                encryptedBlock[i] = block.charAt(key[i]);
            }

            ciphertext.append(encryptedBlock);
            index += blockSize;
        }

        return ciphertext.toString();
    }


    @Override
    public String decrypt(String ciphertext, String keyString) {
        int[] key = stringToIntArray(keyString);
        int blockSize = key.length;
        StringBuilder plaintext = new StringBuilder();
        int index = 0;

        // Tạo bảng ngược khóa để dễ dàng giải mã
        int[] inverseKey = new int[blockSize];
        for (int i = 0; i < blockSize; i++) {
            inverseKey[key[i]] = i;
        }

        while (index < ciphertext.length()) {
            // Lấy một khối mã hóa
            String block = ciphertext.substring(index, Math.min(index + blockSize, ciphertext.length()));

            // Chuyển khối thành mảng ký tự
            char[] decryptedBlock = new char[blockSize];
            for (int i = 0; i < blockSize; i++) {
                decryptedBlock[i] = block.charAt(inverseKey[i]);
            }

            plaintext.append(decryptedBlock);
            index += blockSize;
        }

        // Loại bỏ ký tự đệm 'X' nếu có ở cuối
        return plaintext.toString().replaceAll("X+$", "");
    }

    public static int[] stringToIntArray(String input) {
        // Bước 1: Loại bỏ dấu ngoặc vuông và khoảng trắng
        input = input.replace("[", "").replace("]", "").trim();

        // Bước 2: Tách chuỗi thành mảng các phần tử bằng dấu phẩy
        String[] stringArr = input.split(",\\s*");

        // Bước 3: Chuyển đổi mảng chuỗi thành mảng số nguyên
        int[] intArr = new int[stringArr.length];
        for (int i = 0; i < stringArr.length; i++) {
            intArr[i] = Integer.parseInt(stringArr[i]);
        }

        return intArr;
    }

    @Override
    public boolean isValidKey(String keyString, int blockSize) {
        int[] key = stringToIntArray(keyString);
        if (key.length != blockSize) {
            return false;
        }

        Set<Integer> seen = new HashSet<>();
        for (int num : key) {
            if (num < 0 || num >= blockSize || !seen.add(num)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String generateKey(int blockSize) {
        List<Integer> keyList = new ArrayList<>();
        for (int i = 0; i < blockSize; i++) {
            keyList.add(i);
        }
        Collections.shuffle(keyList);
        int[] keyArr = new int[blockSize];
        for (int i = 0; i < blockSize; i++) {
            keyArr[i] = keyList.get(i);
        }
        return Arrays.toString(keyArr);
    }

    @Override
    public String getInvalidKeyMessage() {
        return "Key must be a permutation of 0 to n-1";
    }

    @Override
    public String name() {
        return Constant.PERMUTATION_CIPHER;
    }

    @Override
    public boolean requireKey() {
        return true;
    }

    public static void main(String[] args) {
        // Create a new instance of PermutationCipher
        PermutationCipher cipher = new PermutationCipher();
        String plaintext = "Hello,!@#%^&(). World! á à ạ ả ấ ầ ậ ẩ ắ ằ ặ ẳ ẻ ẽ";
        // Define the block size
        int blockSize = plaintext.length();

        // Generate a key
        String key = cipher.generateKey(blockSize);
        System.out.println("Generated Key: " + key);

        // Check if the key is valid
        boolean isValid = cipher.isValidKey(key, blockSize);
        System.out.println("Is the key valid? " + isValid);

        // Define a plaintext message

        System.out.println("Plaintext: " + plaintext);

        // Encrypt the plaintext
        String ciphertext = cipher.encrypt(plaintext, key);
        System.out.println("Ciphertext: " + ciphertext);

        // Decrypt the ciphertext
        String decrypted = cipher.decrypt(ciphertext, key);
        System.out.println("Decrypted: " + decrypted);
    }
}