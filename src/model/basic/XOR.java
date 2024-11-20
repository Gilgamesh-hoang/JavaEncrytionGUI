package model.basic;

import model.AbstractEncryptionAlgorithm;

import java.util.Base64;
import java.util.Random;

public class XOR  extends AbstractEncryptionAlgorithm {
    @Override
    public String encrypt(String plaintext, String key) {
        // Define XOR key
        // Any character value will work
        char xorKey = key.charAt(0);

        // Define String to store encrypted/decrypted String
        StringBuilder outputString = new StringBuilder();

        // calculate length of input string
        int len = plaintext.length();

        // perform XOR operation of key
        // with every caracter in string
        for (int i = 0; i < len; i++) {
            outputString.append(Character.toString((char) (plaintext.charAt(i) ^ xorKey)));
        }

//        return outputString.toString();
        return Base64.getEncoder().encodeToString(outputString.toString().getBytes());

    }

    @Override
    public String decrypt(String encrypted, String key) {
        //enc is same as dec
        encrypted = new String(Base64.getDecoder().decode(encrypted));
//        return encrypt(encrypted, key);
        return new String(Base64.getDecoder().decode(encrypt(encrypted, key)));
    }

    @Override
    public boolean requireKey() {
        return true;
    }

    @Override
    public boolean isValidKey(String key) {
        return key.length() == 1;
    }

    @Override
    public String generateKey() {
        return String.valueOf((char) (new Random().nextInt(26) + 'a'));
    }

    @Override
    public String getInvalidKeyMessage() {
        return "Invalid key! Key must be a character!";
    }

    @Override
    public String name() {
        return "XOR";
    }

}
