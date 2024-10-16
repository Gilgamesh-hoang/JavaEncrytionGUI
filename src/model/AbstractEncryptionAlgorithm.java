package model;

public abstract class AbstractEncryptionAlgorithm implements EncryptionAlgorithm {
    @Override
    public String decrypt(String encrypted, String key) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public String encrypt(String plaintext, String key) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public String encrypt(String plaintext, String key, int keyLength, String mode, String padding) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public String decrypt(String encrypted, String key, int keyLength, String mode, String padding) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean requireKey() {
        return false;
    }

    @Override
    public boolean isValidKey(String key) {
        return false;
    }

    @Override
    public String generateKey() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void encryptFile(String inputPath, String outputPath, String key, int keyLength, String mode, String padding) throws Exception {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void decryptFile(String inputPath, String outputPath, String key, int keyLength, String mode, String padding) throws Exception {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean isValidKey(String keyString, int blockSize) {
        throw new UnsupportedOperationException("Not implemented");
    }


    @Override
    public String generateKey(int keyLength) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public String getInvalidKeyMessage() {
        return "";
    }

    @Override
    public String[] getKeyLengths() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public String[] getPaddings() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public String[] getModes() {
        throw new UnsupportedOperationException("Not implemented");
    }
}
