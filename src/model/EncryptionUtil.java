package model;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.JTextComponent;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

public class EncryptionUtil {
    private JPanel frame;
    private EncryptionAlgorithm selectedAlgorithm;
    private JTextComponent inputData;
    private JTextComponent outputData;
    private JTextComponent inputKey;

    public EncryptionUtil(JPanel frame, EncryptionAlgorithm selectedAlgorithm, JTextComponent inputData, JTextComponent outputData, JTextComponent inputKey) {
        this.frame = frame;
        this.selectedAlgorithm = selectedAlgorithm;
        this.inputData = inputData;
        this.outputData = outputData;
        this.inputKey = inputKey;
    }

    public static String decryptAndRemovePadding(Cipher cipher, byte[] encryptedBytes) throws Exception {
        // Giải mã dữ liệu
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        // Chuyển đổi mảng byte thành chuỗi
        String decryptedString = new String(decryptedBytes);

        // Xóa các ký tự null (\0) ở cuối
        return decryptedString.replace("\0", "");
    }

    public static String generateKey(int keyLength, String algorithmName) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(algorithmName);
        keyGen.init(keyLength);
        SecretKey secretKey = keyGen.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    // Tạo secret key từ key string
    public static SecretKeySpec generateSecretKey(String algorithm, String key, int keyLength) {
        byte[] keyBytes = key.getBytes();
        if (algorithm.equals("Blowfish")) {
            if (keyLength == 448)
                keyBytes = Base64.getDecoder().decode(key);
        } else if (keyLength == 256 || keyLength == 56) {
            keyBytes = Base64.getDecoder().decode(key);
        }
        return new SecretKeySpec(keyBytes, algorithm);
    }

    // Tạo Cipher instance
    public static Cipher createCipher(String algorithm, String mode, String padding) throws Exception {
        String transformation = algorithm + "/" + mode + "/" + padding;
        return Cipher.getInstance(transformation);
    }

    // Khởi tạo Cipher cho chế độ không ECB
    public static void initCipherWithIv(Cipher cipher, int mode, SecretKeySpec secretKey, String cipherMode) throws Exception {
        byte[] iv = new byte[cipherMode.equals(Constant.GCM_MODE) ? 12 : 16]; // GCM sử dụng IV 12 byte, CBC sử dụng 16 byte
        new SecureRandom().nextBytes(iv);
        if (cipherMode.equals(Constant.GCM_MODE)) {
            GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
            cipher.init(mode, secretKey, gcmSpec);
        } else {
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(mode, secretKey, ivSpec);
        }
    }

    public EncryptionAlgorithm getSelectedAlgorithm() {
        return selectedAlgorithm;
    }

    public void setSelectedAlgorithm(EncryptionAlgorithm selectedAlgorithm) {
        this.selectedAlgorithm = selectedAlgorithm;
    }

    public static String[] getNamesAlgorithm(List<EncryptionAlgorithm> algorithmList) {
        return algorithmList.stream().map(EncryptionAlgorithm::name).toArray(String[]::new);
    }

    public void encrypt(JTextComponent keyTxt) {
        processInput(keyTxt, true);
    }

    public void decrypt(JTextComponent keyTxt) {
        processInput(keyTxt, false);
    }

    private void processInput(JTextComponent keyTxt, boolean isEncrypt) {
        String inputTxt = inputData.getText();
        String key = keyTxt.getText();
        if (invalidKey()) {
            JOptionPane.showMessageDialog(frame,
                    selectedAlgorithm.getInvalidKeyMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            String result = isEncrypt ? selectedAlgorithm.encrypt(inputTxt, key) : selectedAlgorithm.decrypt(inputTxt, key);
            outputData.setText(result);
        }
    }


    public boolean invalidKey() {
        return selectedAlgorithm.requireKey() && !selectedAlgorithm.isValidKey(inputKey.getText());
    }

    public void handleLoadKey() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file chứa key");

        int userSelection = fileChooser.showOpenDialog(frame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            BufferedReader reader = null;
            try {
                String path = fileToLoad.getAbsolutePath();

                // Ensure the file has a .txt extension
                if (!path.endsWith(".txt")) {
                    JOptionPane.showMessageDialog(frame, "File không hợp lệ! File phải có định dạng .txt", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                reader = new BufferedReader(new FileReader(fileToLoad));
                StringBuilder key = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    key.append(line);
                }
                if (!selectedAlgorithm.isValidKey(key.toString())) {
                    JOptionPane.showMessageDialog(frame,
                            selectedAlgorithm.getInvalidKeyMessage(),
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                inputKey.setText(key.toString());

            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Lỗi: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                if (reader != null)
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    public void handleSaveKey(String key) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu key");

        // Set default file name and filter for .txt files
        String fileName = String.format("%s_key_%s.txt", selectedAlgorithm.name(), System.currentTimeMillis());
        fileChooser.setSelectedFile(new File(fileName)); // Default file name
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));

        int userSelection = fileChooser.showSaveDialog(frame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String path = fileToSave.getAbsolutePath();

            // Ensure the file has a .txt extension
            if (!path.endsWith(".txt")) {
                path += ".txt";
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
                writer.write(key);
                JOptionPane.showMessageDialog(frame,
                        "Key đã được lưu!",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Lỗi: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static String padPlaintextWithZeroBytes(String plaintext, int blockSize) {
        StringBuilder builder = new StringBuilder(plaintext);
        int plaintextLength = plaintext.getBytes().length;
        if (plaintextLength % blockSize != 0) {
            int padLength = blockSize - (plaintextLength % blockSize);
            for (int i = 0; i < padLength; i++) {
                builder.append("\0"); // Add null character for padding
            }
        }
        return builder.toString();
    }
}
