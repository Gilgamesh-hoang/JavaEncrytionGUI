package model.digital_signature;

import model.Constant;
import model.KeyPair;
import model.asymmetric.RSA;

import java.io.*;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class SignRSA {
    // Ký một tệp bằng khóa riêng
    public String signFile(File file, String privateKeyBase64) throws Exception {
        // Đọc khóa riêng từ chuỗi Base64
        PrivateKey privateKey = getPrivateKey(privateKeyBase64);

        Signature signature = Signature.getInstance("SHA256withRSA");

        // Khởi tạo Signature với Private Key
        signature.initSign(privateKey);

        // Đọc dữ liệu từ tệp và cập nhật vào Signature
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                signature.update(buffer, 0, len);
            }
        }

        // Trả về chữ ký dưới dạng mảng byte
        return Base64.getEncoder().encodeToString(signature.sign());
    }

    public KeyPair generateKeyPair() {
        RSA rsa = new RSA();
        return rsa.generateKey(2048);
    }

    // Lưu chữ ký vào một file
    private static void saveSignatureToFile(byte[] signature, String signatureFilePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(signatureFilePath)) {
            fos.write(signature);
        }
    }

    // Xác thực chữ ký điện tử
    public boolean verifySignature(File file, String signatureBase64, String publicKeyBase64) throws Exception {
        PublicKey publicKey = getPublicKey(publicKeyBase64);

        byte[] signature = Base64.getDecoder().decode(signatureBase64);

        Signature sig = Signature.getInstance("SHA256withRSA");

        // Khởi tạo Signature với Public Key
        sig.initVerify(publicKey);

        // Đọc dữ liệu từ file và cập nhật vào Signature
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                sig.update(buffer, 0, len);
            }
        }

        // Xác thực chữ ký
        return sig.verify(signature);
    }

    // Phương thức ký text
    private static String signText(String text, PrivateKey privateKey) throws Exception {
        // Sử dụng thuật toán SHA256 với RSA
        Signature signature = Signature.getInstance("SHA256withRSA");

        // Khởi tạo Signature với khóa riêng
        signature.initSign(privateKey);

        // Cập nhật dữ liệu (text) cần ký
        signature.update(text.getBytes("UTF-8"));

        // Tạo chữ ký và mã hóa nó thành chuỗi Base64
        byte[] digitalSignature = signature.sign();
        return Base64.getEncoder().encodeToString(digitalSignature);
    }

    // Phương thức xác thực chữ ký của text
    private static boolean verifySignature(String text, String signature, PublicKey publicKey) throws Exception {
        // Khởi tạo Signature với thuật toán SHA256 với RSA
        Signature sig = Signature.getInstance("SHA256withRSA");

        // Khởi tạo Signature với khóa công khai
        sig.initVerify(publicKey);

        // Cập nhật dữ liệu (text) cần xác thực
        sig.update(text.getBytes("UTF-8"));

        // Giải mã chữ ký từ chuỗi Base64 và kiểm tra
        byte[] digitalSignature = Base64.getDecoder().decode(signature);
        return sig.verify(digitalSignature);
    }

    // Đọc chữ ký từ file
    private static byte[] readSignatureFromFile(String signatureFilePath) throws IOException {
        return java.nio.file.Files.readAllBytes(new File(signatureFilePath).toPath());
    }

    private PublicKey getPublicKey(String base64PublicKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64PublicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(Constant.RSA_CIPHER);
        return keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
    }

    private PrivateKey getPrivateKey(String base64PrivateKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64PrivateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(Constant.RSA_CIPHER);
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
    }

    private java.security.KeyPair getKeyPair() throws Exception {
        File file = new File("sign/key.dat");
        java.security.KeyPair keyPair = null;
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                keyPair = (java.security.KeyPair) ois.readObject();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            RSA rsa = new RSA();
            KeyPair key = rsa.generateKey(2048);
            keyPair = new java.security.KeyPair(getPublicKey(key.getPublicKey()), getPrivateKey(key.getPrivateKey()));
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(keyPair);
                oos.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        return keyPair;
    }

    public static void main(String[] args) throws Exception {
        // Tạo cặp khóa (hoặc lấy từ nguồn khác)
//        java.security.KeyPair keyPair = getKeyPair();


        // File cần ký
//        File file = new File("sign/a.txt");
//
//        // Ký tệp và lưu chữ ký
//        byte[] signature = signFile(file, keyPair.getPrivate());
//        saveSignatureToFile(signature, "sign/signature.sig");
//
//        System.out.println("Tệp đã được ký thành công.");

        // File và chữ ký
//        File file = new File("sign/a.txt");
//        byte[] signature = readSignatureFromFile("sign/signature.sig");
//
//        // Xác thực chữ ký
//        boolean isVerified = verifySignature(file, signature, keyPair.getPublic());
//        if (isVerified) {
//            System.out.println("same.");
//        } else {
//            System.out.println("not same.");
//        }

        // Văn bản cần xác thực
//        String text = "zxc";
//
//        // Thực hiện ký
//        String signature = signText(text, keyPair.getPrivate());
//        System.out.println("sign: " + signature);
//
//        // Kiểm tra chữ ký
//        boolean isVerified = verifySignature(text, signature, keyPair.getPublic());
//        if (isVerified) {
//            System.out.println("same");
//        } else {
//            System.out.println("not same");
//        }
    }


}
