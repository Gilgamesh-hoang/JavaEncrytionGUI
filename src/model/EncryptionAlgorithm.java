package model;

public interface EncryptionAlgorithm {
    String encrypt(String plaintext, String key);

    String decrypt(String encrypted, String key);

    String encrypt(String plaintext, String key, int keyLength, String mode, String padding);

    String decrypt(String encrypted, String key, int keyLength, String mode, String padding);

    void encryptFile(String inputPath, String outputPath, String key, int keyLength, String mode, String padding) throws Exception;

    void decryptFile(String inputPath, String outputPath, String key, int keyLength, String mode, String padding) throws Exception;

    boolean isValidKey(String keyString, long blockSize);

    boolean requireKey();

    String name();

    boolean isValidKey(String key);

    String generateKey();

    String generateKey(long keyLength);

    String getInvalidKeyMessage();

    String[] getKeyLengths();

    String[] getPaddings();

    String[] getModes();
}
