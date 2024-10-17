package model.symmetric;

import model.AbstractEncryptionAlgorithm;
import model.Constant;

import java.io.*;
import java.util.*;

public class PermutationCipher extends AbstractEncryptionAlgorithm {

    @Override
    public String encrypt(String plaintext, String keyString) {
        long[] key = stringToIntArray(keyString);
        int blockSize = key.length;
        StringBuilder ciphertext = new StringBuilder();
        int index = 0;

        while (index < plaintext.length()) {
            // Lấy một khối văn bản
            StringBuilder block = new StringBuilder(plaintext.substring(index, Math.min(index + blockSize, plaintext.length())));

            // Nếu khối không đủ kích thước, thêm ký tự đệm
            if (block.length() < blockSize) {
                int paddingLength = blockSize - block.length();
                block.append("X".repeat(Math.max(0, paddingLength))); // Ký tự đệm
            }

            // Chuyển khối thành mảng ký tự
            char[] encryptedBlock = new char[block.length()]; // Đảm bảo chỉ sử dụng kích thước khối văn bản

            // Chỉ mã hóa các ký tự trong khối, tránh truy cập vượt quá giới hạn mảng
            for (int i = 0; i < block.length(); i++) {
                encryptedBlock[i] = block.charAt((int) key[i]);
            }

            ciphertext.append(encryptedBlock);
            index += blockSize;
        }

        return ciphertext.toString();
    }


    @Override
    public String decrypt(String ciphertext, String keyString) {
        long[] key = stringToIntArray(keyString);
        int blockSize = key.length;
        StringBuilder plaintext = new StringBuilder();
        int index = 0;

        // Tạo bảng ngược khóa để dễ dàng giải mã
        long[] inverseKey = new long[blockSize];
        for (int i = 0; i < blockSize; i++) {
            inverseKey[(int) key[i]] = i;
        }

        while (index < ciphertext.length()) {
            // Lấy một khối mã hóa
            String block = ciphertext.substring(index, Math.min(index + blockSize, ciphertext.length()));

            // Chuyển khối thành mảng ký tự
            char[] decryptedBlock = new char[blockSize];
            for (int i = 0; i < blockSize; i++) {
                decryptedBlock[i] = block.charAt((int) inverseKey[i]);
            }

            plaintext.append(decryptedBlock);
            index += blockSize;
        }

        // Loại bỏ ký tự đệm 'X' nếu có ở cuối
        return plaintext.toString().replaceAll("X+$", "");
    }

    @Override
    public void encryptFile(String inputPath, String outputPath, String keyString, int keyLength, String mode, String padding) throws Exception {
        long[] key = stringToIntArray(keyString);
        int blockSize = key.length;

        try (
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputPath));
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputPath))
        ) {
            byte[] block = new byte[blockSize];
            int bytesRead;

            while ((bytesRead = bis.read(block)) != -1) {
                // Nếu khối đọc được không đủ kích thước, bổ sung byte 0
                if (bytesRead < blockSize) {
                    block = Arrays.copyOf(block, blockSize);
                }

                // Mã hóa bằng hoán vị
                byte[] encryptedBlock = new byte[blockSize];
                for (int i = 0; i < blockSize; i++) {
                    encryptedBlock[i] = block[(int) key[i]];
                }

                bos.write(encryptedBlock);
            }

            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Lỗi khi mã hóa file");
        }
    }

    @Override
    public void decryptFile(String inputPath, String outputPath, String keyString, int keyLength, String mode, String padding) throws Exception {
        long[] key = stringToIntArray(keyString);
        int blockSize = key.length;

        // Tính khóa giải mã bằng cách tìm hoán vị ngược
        int[] inverseKey = new int[blockSize];
        for (int i = 0; i < blockSize; i++) {
            inverseKey[(int) key[i]] = i;
        }

        try (
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputPath));
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputPath))
        ) {
            byte[] block = new byte[blockSize];
            int bytesRead;

            while ((bytesRead = bis.read(block)) != -1) {
                if (bytesRead < blockSize) {
                    block = Arrays.copyOf(block, blockSize);
                }

                // Giải mã bằng hoán vị ngược
                byte[] decryptedBlock = new byte[blockSize];
                for (int i = 0; i < blockSize; i++) {
                    decryptedBlock[i] = block[inverseKey[i]];
                }

                bos.write(decryptedBlock);
            }

            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Lỗi khi giải mã file");
        }
    }

    public static long[] stringToIntArray(String input) {
        // Bước 1: Loại bỏ dấu ngoặc vuông và khoảng trắng
        input = input.replace("[", "").replace("]", "").trim();

        // Bước 2: Tách chuỗi thành mảng các phần tử bằng dấu phẩy
        String[] stringArr = input.split(",\\s*");

        // Bước 3: Chuyển đổi mảng chuỗi thành mảng số nguyên
        long[] longArr  = new long[stringArr.length];
        for (int i = 0; i < stringArr.length; i++) {
            longArr [i] = Long.parseLong(stringArr[i]);
        }

        return longArr ;
    }

    @Override
    public boolean isValidKey(String keyString, long blockSize) {
        long[] key = stringToIntArray(keyString);
        if (key.length != blockSize) {
            return false;
        }

        Set<Long> seen = new HashSet<>();
        for (long num : key) {
            if (num < 0 || num >= blockSize || !seen.add(num)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String generateKey(long blockSize) {
        List<Long> keyList = new ArrayList<>();
        for (long i = 0; i < blockSize; i++) {
            keyList.add(i);
        }
        Collections.shuffle(keyList);
        return Arrays.toString(keyList.toArray(new Long[0]));
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
        PermutationCipher alg = new PermutationCipher();
//        String plaintext = "Hello,!@#%^&(). World! á à ạ ả ấ ầ ậ ẩ ắ ằ ặ ẳ ẻ ẽ";
//        // Define the block size
//        int blockSize = plaintext.length();
//
//        // Generate a key
//        String key = alg.generateKey(blockSize);
//        System.out.println("Generated Key: " + key);
//
//        // Check if the key is valid
//        boolean isValid = alg.isValidKey(key, blockSize);
//        System.out.println("Is the key valid? " + isValid);
//
//        // Define a plaintext message
//
//        System.out.println("Plaintext: " + plaintext);
//
//        // Encrypt the plaintext
//        String ciphertext = alg.encrypt(plaintext, key);
//        System.out.println("Ciphertext: " + ciphertext);
//
//        // Decrypt the ciphertext
//        String decrypted = alg.decrypt(ciphertext, key);
//        System.out.println("same: " + decrypted.equals(plaintext));

        String inputPath = "C:\\Users\\FPT SHOP\\Documents\\New Folder\\hc.jpg";

        for (int i = 0; i < 2; i++) {
            File file = new File(inputPath);
            String key = alg.generateKey(file.length());
            String enPath = String.format("C:\\Users\\FPT SHOP\\Documents\\New Folder\\1\\en_%s_.jpg", i+"");
            String dePath = String.format("C:\\Users\\FPT SHOP\\Documents\\New Folder\\de_%s.jpg", i+"");
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