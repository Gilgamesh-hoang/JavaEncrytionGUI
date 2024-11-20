package model.symmetric;

import model.AbstractEncryptionAlgorithm;
import model.Constant;
import model.EncryptionUtil;

import java.io.*;
import java.util.Base64;
import java.util.Random;

public class AffineCipher extends AbstractEncryptionAlgorithm {
    private static final int MODULO = 65536;

    @Override
    public String generateKey() {
        // Tạo một đối tượng Random để sinh số ngẫu nhiên
        Random random = new Random();
        int a;

        // Sinh giá trị 'a' sao cho 'a' nguyên tố cùng nhau với hằng số MODULO
        do {
            a = random.nextInt(MODULO); // Sinh ngẫu nhiên giá trị a trong khoảng từ 0 đến MODULO - 1
        } while (gcd(a, MODULO) != 1); // Kiểm tra nếu 'a' và MODULO không nguyên tố cùng nhau thì tiếp tục lặp

        // Sinh giá trị 'b' ngẫu nhiên trong khoảng [0, MODULO-1]
        int b = random.nextInt(MODULO);

        // Trả về khóa dưới dạng chuỗi có định dạng "a,b"
        return a + "," + b;
    }


    private int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }

    private int modInverse(int a) {
        a = a % MODULO;
        for (int x = 1; x < MODULO; x++) {
            if ((a * x) % MODULO == 1) return x;
        }
        throw new ArithmeticException("No modular inverse of " + a);
    }

    @Override
    public String encrypt(String plaintext, String key) {
        int a = Integer.parseInt(key.split(",")[0]);
        int b = Integer.parseInt(key.split(",")[1]);
        StringBuilder ciphertext = new StringBuilder();

        for (char character : plaintext.toCharArray()) {
            int x = character;
            int encryptedChar = (a * x + b) % MODULO;
            ciphertext.append((char) encryptedChar);
        }
        return ciphertext.toString();
    }

    @Override
    public String decrypt(String ciphertext, String key) {
        // Tách giá trị 'a' và 'b' từ chuỗi khóa
        int a = Integer.parseInt(key.split(",")[0]);
        int b = Integer.parseInt(key.split(",")[1]);

        StringBuilder plaintext = new StringBuilder();

        // Tính nghịch đảo modulo của 'a' (aInverse)
        int aInverse = modInverse(a);

        for (char character : ciphertext.toCharArray()) {
            int y = character;

            // Giải mã ký tự bằng công thức: (aInverse * (y - b + MODULO)) % MODULO
            // Công thức đảm bảo giá trị không âm bằng cách thêm MODULO trước phép chia
            int decryptedChar = (aInverse * (y - b + MODULO)) % MODULO;

            // Chuyển giá trị đã giải mã thành ký tự và thêm vào chuỗi kết quả
            plaintext.append((char) decryptedChar);
        }

        // Trả về chuỗi văn bản đã giải mã
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
            throw new Exception("Error when encrypting file");
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
            throw new Exception("Error when decrypting file");
        } finally {
            EncryptionUtil.closeStream(null, null, bis, bos);
        }
    }

    @Override
    public boolean isValidKey(String key) {
        try {
            String[] parts = key.split(",");

            if (parts.length != 2) {
                return false;
            }

            // Chuyển đổi các phần của khóa thành số nguyên
            int a = Integer.parseInt(parts[0].trim());
            int b = Integer.parseInt(parts[1].trim());

            // Kiểm tra các điều kiện hợp lệ:
            // - 'a' phải nguyên tố cùng nhau với MODULO (gcd(a, MODULO) == 1)
            // - 'b' phải nằm trong khoảng từ 0 đến MODULO - 1
            return gcd(a, MODULO) == 1 && b >= 0 && b <= MODULO - 1;

        } catch (NumberFormatException e) {
            return false;
        }
    }


    @Override
    public String getInvalidKeyMessage() {
        return "Number of parts must be 2. 'a' must be coprime with " + MODULO + " and 'b' must be in the range [0, " + (MODULO - 1) + "]";
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