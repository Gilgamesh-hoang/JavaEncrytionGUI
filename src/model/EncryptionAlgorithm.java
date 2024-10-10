package model;

public interface EncryptionAlgorithm {
    String encrypt(String plaintext, String key);

    String decrypt(String encrypted, String key);

    String encrypt(String plaintext, String key, int keyLength, String mode, String padding);

    String decrypt(String encrypted, String key, int keyLength, String mode, String padding);

    boolean isValidKey(String keyString, int blockSize);

    boolean requireKey();

    String name();

    boolean isValidKey(String key);

    String generateKey();

    String generateKey(int keyLength);

    String getInvalidKeyMessage();

    String[] getKeyLengths();

    String[] getPaddings();

    String[] getModes();
}
