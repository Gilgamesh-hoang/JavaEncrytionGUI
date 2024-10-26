package model.hash;

import model.KeyPair;
import model.symmetric.AES;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Base64;

public class Hash {

    public String generateKey() throws NoSuchAlgorithmException, NoSuchProviderException {
        AES aes = new AES();
        return aes.generateKey(256L);
    }

    // Phương thức thực hiện hash với MD5, SHA-256, hoặc SHA-512
    public String hashText(String input, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Hex.toHexString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String hashFile(String filePath, String algorithm) {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath))) {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] buffer = new byte[1024];
            int bytesRead = 0;
            while ((bytesRead = bis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
            return Hex.toHexString(digest.digest());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Phương thức thực hiện HMAC với thuật toán cụ thể
    public String hmacText(String input, String keyBase64, String algorithm) {
        try {
            Mac mac = Mac.getInstance(algorithm);
            byte[] keyBytes = Base64.getDecoder().decode(keyBase64);
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, algorithm);
            mac.init(secretKey);
            byte[] hmacBytes = mac.doFinal(input.getBytes(StandardCharsets.UTF_8));
            return Hex.toHexString(hmacBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String hmacFile(String filePath, String keyBase64, String algorithm) {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath))) {
            Mac mac = Mac.getInstance(algorithm);
            byte[] keyBytes = Base64.getDecoder().decode(keyBase64);
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, algorithm);
            mac.init(secretKey);

            byte[] buffer = new byte[1024];
            int bytesRead = 0;
            while ((bytesRead = bis.read(buffer)) != -1) {
                mac.update(buffer, 0, bytesRead);
            }

            byte[] hmacBytes = mac.doFinal();
            return Hex.toHexString(hmacBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // Biến lưu trữ kết quả của lần chạy đầu tiên

    public static void main(String[] args) {
        try {
            Hash hash = new Hash();
            String[] firstRunResults = new String[5];
//            ==========================================File==========================================================
            // Chạy lần 1
            String input1 = "E:\\workspace\\ATTT\\sign\\a.txt";
            String key = Base64.getEncoder().encodeToString("my-secret-key".getBytes());
            System.out.println("Lần 1 với input: " + input1);
            storeResultsFile(input1, key, firstRunResults, hash);

            // Chạy lần 2 với input mới
//            BufferedWriter writer = new BufferedWriter(new FileWriter(input1, true));
//            writer.append("Hello, World!");
//            writer.flush();
//            writer.close();

            System.out.println("\nLần 2 với input: " + input1);
            String[] secondRunResults = new String[5];
            storeResultsFile(input1, key, secondRunResults, hash);

            // So sánh kết quả của lần 1 và lần 2
            System.out.println("\nSo sánh kết quả:");
            String[] algorithms = {"MD5", "SHA-256", "SHA-512", "HMAC-SHA256", "HMAC-SHA512"};

            for (int i = 0; i < firstRunResults.length; i++) {
                if (firstRunResults[i].equals(secondRunResults[i])) {
                    System.out.println(algorithms[i] + ": not change");
                } else {
                    System.out.println(algorithms[i] + ": changed");
                }
            }

//            ==========================================Text==========================================================
//            // Chạy lần 1
//            String input1 = "Hello, World!";
//            String key = "my-secret-key";
//            System.out.println("Lần 1 với input: " + input1);
//            storeResults(input1, key, firstRunResults, hash);
//
//            // Chạy lần 2 với input mới
//            String input2 = "Hello, World!";
//            System.out.println("\nLần 2 với input: " + input2);
//            String[] secondRunResults = new String[5];
//            storeResults(input2, key, secondRunResults, hash);
//
//            // So sánh kết quả của lần 1 và lần 2
//            System.out.println("\nSo sánh kết quả:");
//            String[] algorithms = {"MD5", "SHA-256", "SHA-512", "HMAC-SHA256", "HMAC-SHA512"};
//
//            for (int i = 0; i < firstRunResults.length; i++) {
//                if (firstRunResults[i].equals(secondRunResults[i])) {
//                    System.out.println(algorithms[i] + ": not change");
//                } else {
//                    System.out.println(algorithms[i] + ": changed");
//                }
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Lưu kết quả hash và HMAC vào một mảng
    private static void storeResults(String input, String key, String[] results, Hash hash) {
        results[0] = hash.hashText(input, "MD5");
        results[1] = hash.hashText(input, "SHA-256");
        results[2] = hash.hashText(input, "SHA-512");
        results[3] = hash.hmacText(input, key, "HmacSHA256");
        results[4] = hash.hmacText(input, key, "HmacSHA512");

        System.out.println("MD5: " + results[0]);
        System.out.println("SHA-256: " + results[1]);
        System.out.println("SHA-512: " + results[2]);
        System.out.println("HMAC-SHA256: " + results[3]);
        System.out.println("HMAC-SHA512: " + results[4]);
    }
    private static void storeResultsFile(String path, String key, String[] results, Hash hash) {
        results[0] = hash.hashFile(path, "MD5");
        results[1] = hash.hashFile(path, "SHA-256");
        results[2] = hash.hashFile(path, "SHA-512");
        results[3] = hash.hmacFile(path, key, "HmacSHA256");
        results[4] = hash.hmacFile(path, key, "HmacSHA512");

        System.out.println("MD5: " + results[0]);
        System.out.println("SHA-256: " + results[1]);
        System.out.println("SHA-512: " + results[2]);
        System.out.println("HMAC-SHA256: " + results[3]);
        System.out.println("HMAC-SHA512: " + results[4]);
    }


}
