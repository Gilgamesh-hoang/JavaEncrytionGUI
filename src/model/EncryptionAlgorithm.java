package model;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;


// This interface is not optimized, so it should be divided into smaller interfaces and apply additional design patterns
public interface EncryptionAlgorithm {
    String encrypt(String plaintext, String key);

    String decrypt(String encrypted, String key);

    String encrypt(String plaintext, String key, int keyLength, String mode, String padding);

    String decrypt(String encrypted, String key, int keyLength, String mode, String padding);

    String encrypt(String plaintext, KeyPair key, String mode, String padding);

    String decrypt(String encrypted, KeyPair key, String mode, String padding);

    void encryptFile(String inputPath, String outputPath, String key, int keyLength, String mode, String padding) throws Exception;

    void decryptFile(String inputPath, String outputPath, String key, int keyLength, String mode, String padding) throws Exception;

    boolean isValidKey(String keyString, long blockSize);

    boolean isValidKey(KeyPair keyPair);

    boolean requireKey();

    String name();

    boolean isValidKey(String key);

    String generateKey();

    String generateKey(long keyLength);

    KeyPair generateKey(int keyLength) throws NoSuchAlgorithmException, NoSuchProviderException;

    String getInvalidKeyMessage();

    String[] getKeyLengths();

    String[] getPaddings();

    String[] getModes();

    int getKeyLength(String key);
}
