package model.asym;

import model.AbstractEncryptionAlgorithm;
import model.Constant;

public class ECC extends AbstractEncryptionAlgorithm {


//    @Override
//    public String encrypt(String plaintext, String key, int keyLength, String mode, String padding) {
//    }
//
//
//    @Override
//    public String decrypt(String encrypted, String key, int keyLength, String mode, String padding) {
//    }
//
//
//
//
//    @Override
//    public boolean requireKey() {
//        return true;
//    }
//
//    @Override
//    public boolean isValidKey(String key) {
//    }
//
//    @Override
//    public String generateKey(long keyLength) {
//    }

    @Override
    public String getInvalidKeyMessage() {
        return "Key must be 16, 24, or 32 bytes long for AES.";
    }

    @Override
    public String[] getKeyLengths() {
        return new String[]{"128", "192", "256"};
    }

    @Override
    public String[] getPaddings() {
        return new String[]{Constant.NO_PADDING, Constant.ZERO_PADDING, Constant.PKCS5_PADDING};
    }

    @Override
    public String[] getModes() {
        return new String[]{Constant.GCM_MODE, Constant.CBC_MODE, Constant.OFB_MODE, Constant.CFB_MODE, Constant.ECB_MODE};
    }

    @Override
    public String name() {
        return Constant.AES_CIPHER;
    }

    public static void main(String[] args) {
        ECC aes = new ECC();
//        String inputPath = "C:\\Users\\FPT SHOP\\Documents\\New Folder\\DES.json";
//        String enPath = "C:\\Users\\FPT SHOP\\Documents\\New Folder\\DES1.json";
//        String dePath = "C:\\Users\\FPT SHOP\\Documents\\New Folder\\DES2.json";
//        String key = aes.generateKey(128);
//        int keyLength = 128; // DES key length is 56 bits
//        String mode = Constant.GCM_MODE; // or any other mode you want to use
//        String padding = Constant.PKCS5_PADDING; // or any other padding you want to use
//
//        try {
//            aes.encryptFile(inputPath, enPath, key, keyLength, mode, padding);
//            aes.decryptFile(enPath, dePath, key, keyLength, mode, padding);
//            System.out.println("Decryption completed. Check the output file.");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }
}