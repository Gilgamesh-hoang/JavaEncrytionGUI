package model.asym;

import model.AbstractEncryptionAlgorithm;
import model.Constant;
import org.bouncycastle.jce.interfaces.ElGamalPrivateKey;
import org.bouncycastle.jce.interfaces.ElGamalPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.DHParameterSpec;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class ElGamal extends AbstractEncryptionAlgorithm {
    private static final String PROVIDER = "BC";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Override
    public String encrypt(String plaintext, model.KeyPair key, String mode, String padding) {
        try {
            byte[] input = plaintext.getBytes();
            PublicKey pubKey = KeyFactory.getInstance(Constant.ELGAMAL_CIPHER, PROVIDER)
                    .generatePublic(new X509EncodedKeySpec(Base64.decode(key.getPublicKey())));
            Cipher cipher = Cipher.getInstance(Constant.ELGAMAL_CIPHER, PROVIDER);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] cipherText = cipher.doFinal(input);
            return Base64.toBase64String(cipherText);
        } catch (Exception e) {
            throw new RuntimeException("Error during encryption", e);
        }
    }

    @Override
    public String decrypt(String encrypted, model.KeyPair key, String mode, String padding) {
        try {
            byte[] input = Base64.decode(encrypted);
            PrivateKey privKey = KeyFactory.getInstance(Constant.ELGAMAL_CIPHER, PROVIDER)
                    .generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(key.getPrivateKey())));
            Cipher cipher = Cipher.getInstance(Constant.ELGAMAL_CIPHER, PROVIDER);
            cipher.init(Cipher.DECRYPT_MODE, privKey);
            byte[] plainText = cipher.doFinal(input);
            return new String(plainText);
        } catch (Exception e) {
            throw new RuntimeException("Error during decryption", e);
        }
    }

    @Override
    public model.KeyPair generateKey(int keyLength) throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(Constant.ELGAMAL_CIPHER, PROVIDER);
        keyPairGenerator.initialize(keyLength);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        String publicKeyStr = Base64.toBase64String(keyPair.getPublic().getEncoded());
        String privateKeyStr = Base64.toBase64String(keyPair.getPrivate().getEncoded());
        return new model.KeyPair(publicKeyStr, privateKeyStr);
    }

    @Override
    public boolean requireKey() {
        return true;
    }

    @Override
    public boolean isValidKey(model.KeyPair keyPair) {
        try {
            // Kiểm tra đầu vào null hoặc rỗng
            if (keyPair == null || keyPair.getPublicKey().isBlank() || keyPair.getPrivateKey().isBlank()) {
                return false;
            }

            // Lấy khóa riêng và khóa công khai
            PublicKey pubKey = KeyFactory.getInstance(Constant.ELGAMAL_CIPHER, PROVIDER)
                    .generatePublic(new X509EncodedKeySpec(Base64.decode(keyPair.getPublicKey())));
            PrivateKey privKey = KeyFactory.getInstance(Constant.ELGAMAL_CIPHER, PROVIDER)
                    .generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(keyPair.getPrivateKey())));
            ElGamalPublicKey publicKey = (ElGamalPublicKey) pubKey;
            ElGamalPrivateKey privateKey = (ElGamalPrivateKey) privKey;


            // Lấy các tham số g, p từ khóa công khai
            DHParameterSpec params = publicKey.getParams();
            BigInteger g = params.getG();
            BigInteger p = params.getP();

            // Kiểm tra tính hợp lệ của Y
            BigInteger y = publicKey.getY();
            BigInteger x = privateKey.getX();

            // Kiểm tra điều kiện Y = g^X mod p
            BigInteger calculatedY = g.modPow(x, p);
            if (!calculatedY.equals(y)) {
                return false; // Khóa công khai không hợp lệ
            }

            // Mã hóa và giải mã dữ liệu thử nghiệm
            String testData = "Hello, RSA!";
            String cipherText = encrypt(testData, keyPair, Constant.NO_MODE, Constant.NO_PADDING);
            String decryptedText = decrypt(cipherText, keyPair, Constant.NO_MODE, Constant.NO_PADDING);

            // So sánh dữ liệu sau mã hóa và giải mã
            return testData.equals(decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int getKeyLength(String key) {
        return 2048;
    }

    @Override
    public String getInvalidKeyMessage() {
        return "Invalid ElGamal key";
    }

    @Override
    public String[] getKeyLengths() {
        return new String[]{"2048"};
    }

    @Override
    public String[] getPaddings() {
        return new String[]{Constant.NO_PADDING};
    }

    @Override
    public String[] getModes() {
        return new String[]{Constant.NO_MODE};
    }

    @Override
    public String name() {
        return Constant.ELGAMAL_CIPHER;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException {
        ElGamal alg = new ElGamal();
        String plaintext = "Hello, World!hôm nay ăn cái gì vậy";

        model.KeyPair key = alg.generateKey(2048);
        System.out.println("key valid: " + alg.isValidKey(key));
        String encrypted = alg.encrypt(plaintext, key, null, null);
        System.out.println("Encrypted: " + encrypted);
        String decrypted = alg.decrypt(encrypted, key, null, null);
        System.out.println("Decrypted: " + decrypted);
        System.out.println("same: " + decrypted.equals(plaintext));
    }
}