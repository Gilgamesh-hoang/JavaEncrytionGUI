package model.asymmetric;

import model.AbstractEncryptionAlgorithm;
import model.Constant;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class ECC extends AbstractEncryptionAlgorithm {
    private static final String PROVIDER = "BC";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Override
    public String encrypt(String plaintext, model.KeyPair key, String mode, String padding) {
        try {
            byte[] input = plaintext.getBytes();
            PublicKey pubKey = KeyFactory.getInstance("EC", PROVIDER)
                    .generatePublic(new X509EncodedKeySpec(Base64.decode(key.getPublicKey())));

            Cipher cipher = Cipher.getInstance(Constant.ECC_CIPHER, PROVIDER);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] encryptedData = cipher.doFinal(input);
            return Base64.toBase64String(encryptedData);

        } catch (Exception e) {
            throw new RuntimeException("Error during encryption", e);
        }
    }

    @Override
    public String decrypt(String encrypted, model.KeyPair key, String mode, String padding) {
        try {
            byte[] input = Base64.decode(encrypted);
            PrivateKey privKey = KeyFactory.getInstance("EC", PROVIDER)
                    .generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(key.getPrivateKey())));

            Cipher cipher = Cipher.getInstance(Constant.ECC_CIPHER, PROVIDER);
            cipher.init(Cipher.DECRYPT_MODE, privKey);
            byte[] decryptedData = cipher.doFinal(input);
            return new String(decryptedData);
        } catch (Exception e) {
            throw new RuntimeException("Error during decryption", e);
        }
    }

    @Override
    public model.KeyPair generateKey(int keyLength) throws NoSuchAlgorithmException, NoSuchProviderException {
        // Khai báo một chuỗi để lưu tên đường cong elliptic sẽ được sử dụng
        String ellipticCurve = null;
        switch (keyLength) {
            case 256:
                // Nếu độ dài khóa là 256, sử dụng đường cong "secp256r1"
                ellipticCurve = "secp256r1";
                break;
            case 384:
                ellipticCurve = "secp384r1";
                break;
            case 521:
                ellipticCurve = "secp521r1";
                break;
            default:
                throw new NoSuchAlgorithmException("Invalid key length");
        }
        try {
            // Lấy thông số của đường cong elliptic từ tên đường cong đã chọn
            ECNamedCurveParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec(ellipticCurve);

            // Tạo một KeyPairGenerator sử dụng thuật toán "EC" (Elliptic Curve) với nhà cung cấp (provider) đã định nghĩa
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", PROVIDER);

            // Khởi tạo KeyPairGenerator với thông số đường cong và một đối tượng SecureRandom để tạo số ngẫu nhiên
            keyPairGenerator.initialize(ecSpec, new SecureRandom());

            // Tạo cặp khóa (key pair) gồm khóa công khai và khóa riêng tư
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            String publicKeyStr = Base64.toBase64String(keyPair.getPublic().getEncoded());
            String privateKeyStr = Base64.toBase64String(keyPair.getPrivate().getEncoded());

            // Trả về một đối tượng KeyPair chứa chuỗi Base64 của khóa công khai và khóa riêng tư
            return new model.KeyPair(publicKeyStr, privateKeyStr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean requireKey() {
        return true;
    }

    @Override
    public String getInvalidKeyMessage() {
        return "Invalid ECC key";
    }

    @Override
    public String[] getKeyLengths() {
        return new String[]{"256", "384", "521"};
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
        return Constant.ECC_CIPHER;
    }


    public static void main(String[] args) throws Exception {
        // 1. Tạo cặp khóa ECC
        ECC alg = new ECC();
        for (String keyLength : alg.getKeyLengths()) {
            int keyLen = Integer.parseInt(keyLength);
            model.KeyPair keyPair = alg.generateKey(keyLen);
            System.out.println("Key Length: " + keyLen);
//            System.out.println("Public Key: " + keyPair.getPublicKey());
//            System.out.println("Private Key: " + keyPair.getPrivateKey());

            // 2. Dữ liệu cần mã hóa
            String originalData = "Hello, ECC with BouncyCastle!";

            // 3. Mã hóa dữ liệu bằng khóa công khai
            String encryptedData = alg.encrypt(originalData, keyPair, Constant.NO_MODE, Constant.NO_PADDING);
//            System.out.println("Dữ liệu đã mã hóa: " + encryptedData);

            // 4. Giải mã dữ liệu bằng khóa riêng
            String decryptedData = alg.decrypt(encryptedData, keyPair, Constant.NO_MODE, Constant.NO_PADDING);
//            System.out.println("Dữ liệu đã giải mã: " + decryptedData);
            System.out.println("same: " + originalData.equals(decryptedData));

        }

    }
}