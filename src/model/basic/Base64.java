package model.basic;


import model.AbstractEncryptionAlgorithm;

public class Base64 extends AbstractEncryptionAlgorithm {

    @Override
    public String encrypt(String plaintext, String key) {
        try {
            return java.util.Base64.getEncoder().encodeToString(plaintext.getBytes());
        }catch (Exception e){
            return "";
        }
    }

    @Override
    public String decrypt(String encrypted, String key) {
        return new String(java.util.Base64.getDecoder().decode(encrypted.getBytes()));
    }

    @Override
    public String name() {
        return "Base64";
    }

}
