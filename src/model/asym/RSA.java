package model.asym;

import model.AbstractEncryptionAlgorithm;
import model.Constant;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class RSA extends AbstractEncryptionAlgorithm {

    @Override
    public String encrypt(String plaintext, model.KeyPair key, String mode, String padding) {
        try {
            PublicKey publicKey = getPublicKey(key.getPublicKey());
            Cipher cipher = Cipher.getInstance(Constant.RSA_CIPHER + "/" + mode + "/" + padding);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    @Override
    public String decrypt(String encrypted, model.KeyPair key, String mode, String padding) {
        try {
            PrivateKey privateKey = getPrivateKey(key.getPrivateKey());
            Cipher cipher = Cipher.getInstance(Constant.RSA_CIPHER + "/" + mode + "/" + padding);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encrypted));
            String res = new String(decryptedBytes);
            // remove null character at the end of string
            res = res.replaceAll("\0", "");
            return res;
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
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


    public int getPublicKeyLength(PublicKey publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(Constant.RSA_CIPHER);
        RSAPublicKeySpec publicKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
        return publicKeySpec.getModulus().bitLength();  // Độ dài khóa (số bit)
    }


    public int getPrivateKeyLength(PrivateKey privateKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(Constant.RSA_CIPHER);
        RSAPrivateKeySpec privateKeySpec = keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);
        return privateKeySpec.getModulus().bitLength();  // Độ dài khóa (số bit)
    }

    @Override
    public int getKeyLength(String key) {
        try {
            key = key.split(Constant.SPLIT_KEY)[0];
            PublicKey publicKey = getPublicKey(key);
            return getPublicKeyLength(publicKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean requireKey() {
        return true;
    }

    @Override
    public boolean isValidKey(model.KeyPair keyPair) {
        try {

            if (keyPair == null || keyPair.getPublicKey().isBlank() || keyPair.getPrivateKey().isBlank()) {
                return false;
            }

            PublicKey publicKey = getPublicKey(keyPair.getPublicKey());
            PrivateKey privateKey = getPrivateKey(keyPair.getPrivateKey());

            int pubKeyLength = getPublicKeyLength(publicKey);
            int priKeyLength = getPrivateKeyLength(privateKey);

            // Kiểm tra độ dài khóa có hợp lệ không
            if (pubKeyLength != priKeyLength || !Arrays.asList(this.getKeyLengths()).contains(String.valueOf(pubKeyLength))) {
                return false;
            }

            // Dữ liệu cần kiểm tra
            String testData = "Hello, RSA!";
            String cipherText = encrypt(testData, keyPair, Constant.ECB_MODE, Constant.NO_PADDING);
            String decryptedText = decrypt(cipherText, keyPair, Constant.ECB_MODE, Constant.NO_PADDING);
            return testData.equals(decryptedText);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public model.KeyPair generateKey(int keyLength) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(Constant.RSA_CIPHER);
            keyPairGenerator.initialize(keyLength);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();
            return new model.KeyPair(Base64.getEncoder().encodeToString(publicKey.getEncoded()),
                    Base64.getEncoder().encodeToString(privateKey.getEncoded()));
        } catch (Exception e) {
            throw new RuntimeException("Key generation failed", e);
        }
    }

    @Override
    public String getInvalidKeyMessage() {
        return "Invalid RSA key";
    }

    @Override
    public String[] getKeyLengths() {
        return new String[]{"1024", "2048", "4096"};
    }

    @Override
    public String[] getPaddings() {
        return new String[]{Constant.NO_PADDING, Constant.OAEP_PADDING, Constant.PKCS1_PADDING};
    }

    @Override
    public String[] getModes() {
        return new String[]{Constant.ECB_MODE};
    }

    @Override
    public String name() {
        return Constant.RSA_CIPHER;
    }


    public static void main(String[] args) throws Exception {
        RSA alg = new RSA();
        String plaintext = "Hello, World!hôm nay ăn cái gì vậy";
        String[] keyLengths = alg.getKeyLengths();
        String[] paddings = alg.getPaddings();
        String[] modes = alg.getModes();

        for (String keyLength : keyLengths) {
            int keyLengthInBits = Integer.parseInt(keyLength);
            model.KeyPair key = alg.generateKey(keyLengthInBits);
            System.out.println("key valid: " + alg.isValidKey(key));

            for (String padding : paddings) {
                for (String mode : modes) {
                    System.out.printf("key: %s,Key Length: %s,Padding: %s, Mode: %s \n", "key", keyLength, padding, mode);
                    try {
                        String encrypted = alg.encrypt(plaintext, key, mode, padding);
//                            System.out.println("Encrypted: " + encrypted);
                        String decrypted = alg.decrypt(encrypted, key, mode, padding);
//                            System.out.println("Decrypted: " + decrypted);
//                            System.out.println("=====================================");
                        if (!decrypted.equals(plaintext)) {
                            System.out.println("not equal");
                            System.out.printf("key: %s,Key Length: %s,Padding: %s, Mode: %s \n", key, keyLength, padding, mode);
                            return;
                        }
                    } catch (Exception e) {
                        System.out.println("Error with Key Length: " + keyLength + ", Padding: " + padding + ", Mode: " + mode);
                        e.printStackTrace();
                        System.out.println("=====================================");
                        return;
                    }
                }
            }
        }

    }
}