package model;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
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

    public static void closeStream(CipherInputStream cis, CipherOutputStream cos, BufferedInputStream bis, BufferedOutputStream bos) {
        if (cos != null) {
            try {
                cos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (cis != null) {
            try {
                cis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (bis != null) {
            try {
                bis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (bos != null) {
            try {
                bos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    public void encrypt(String key) {
        processInput(key, true);
    }

    public void decrypt(String key) {
        processInput(key, false);
    }

    private void processInput(String key, boolean isEncrypt) {
        String inputTxt = inputData.getText();
        if (invalidKey()) {
            showMessage("Lỗi", selectedAlgorithm.getInvalidKeyMessage(), JOptionPane.ERROR_MESSAGE, frame);
        } else {
            String result = isEncrypt ? selectedAlgorithm.encrypt(inputTxt, key) : selectedAlgorithm.decrypt(inputTxt, key);
            outputData.setText(result);
        }
    }


    public boolean invalidKey() {
        return selectedAlgorithm.requireKey() && !selectedAlgorithm.isValidKey(inputKey.getText());
    }

    public KeyJson handleLoadKey() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file chứa key");

        int userSelection = fileChooser.showOpenDialog(frame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String path = fileToLoad.getAbsolutePath();

                // Ensure the file has a .json extension
                if (!path.endsWith(".json")) {
                    showMessage("Error", "File không hợp lệ! File phải có định dạng .json", JOptionPane.ERROR_MESSAGE, frame);
                    return null;
                }

                // Đọc và parse JSON file thành đối tượng KeyJson bằng Jackson
                KeyJson keyJson = objectMapper.readValue(new File(path), KeyJson.class);

                // Sau khi parse xong, cập nhật thông tin trong giao diện
//                inputKey.setText(keyJson.getKey());
                // Bạn có thể cập nhật các trường khác trên giao diện nếu cần
                return keyJson;

            } catch (IOException e) {
                e.printStackTrace();
                showMessage("Error", e.getMessage(), JOptionPane.ERROR_MESSAGE, frame);
            }
        }
        return null;
    }


    public void handleSaveKey(String key, String mode, String padding) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu key");

        // Set default file name and filter for .json files
        String fileName = String.format("%s_key_%s.json", selectedAlgorithm.name(), System.currentTimeMillis());
        fileChooser.setSelectedFile(new File(fileName)); // Default file name
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON Files", "json"));

        int userSelection = fileChooser.showSaveDialog(frame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String path = fileToSave.getAbsolutePath();

            // Ensure the file has a .json extension
            if (!path.endsWith(".json")) {
                path += ".json";
            }

            // Create an instance of KeyJson
            KeyJson keyJson = new KeyJson(selectedAlgorithm.name(), key, mode, padding);

            // Create an ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
                String json = objectMapper.writeValueAsString(keyJson);
                writer.write(json);
                showMessage("Success", "Key is saved", JOptionPane.INFORMATION_MESSAGE, frame);
            } catch (IOException e) {
                e.printStackTrace();
                showMessage("Error", e.getMessage(), JOptionPane.ERROR_MESSAGE, frame);
            }
        }
    }

    public static void validatePath(String inputPath, String outputPath) {
        File inputFile = new File(inputPath);
        if (inputFile.isDirectory()) {
            throw new RuntimeException("Input file is a directory.");
        }

        if (!inputFile.exists()) {
            throw new RuntimeException("Input file not found.");
        }

        File outputFile = new File(outputPath);
        if (outputFile.isDirectory()) {
            throw new RuntimeException("Output file is a directory.");
        }
    }

    public static byte[] padByteArrayWithZeroBytes(byte[] inputBytes, int blockSize) {
        int paddingLength = blockSize - (inputBytes.length % blockSize);
        if (paddingLength == blockSize) {
            return inputBytes; // Không cần padding nếu đã vừa khít
        }

        byte[] paddedBytes = new byte[inputBytes.length + paddingLength];
        System.arraycopy(inputBytes, 0, paddedBytes, 0, inputBytes.length);

        // Các byte thêm vào cuối sẽ là 0x00, vì mảng `paddedBytes` đã được khởi tạo với giá trị 0
        return paddedBytes;
    }

    public static void showMessage(String title, String message, int type, Container frame) {
        JOptionPane.showMessageDialog(frame, message, title, type);
    }

    public static byte[] removeZeroPadding(byte[] data, int blockSize) {
        int length = data.length;
        int paddingStart = length;

        // Find where the padding starts
        for (int i = length - 1; i >= length - blockSize; i--) {
            if (data[i] != 0) {
                break;
            }
            paddingStart--;
        }

        // Create a new array without the padding
        byte[] result = new byte[paddingStart];
        System.arraycopy(data, 0, result, 0, paddingStart);

        return result;
    }

    public static byte[] removeZeroPadding(byte[] data) {
        int i = data.length - 1;

        // Tìm vị trí byte không phải là 0 cuối cùng
        while (i >= 0 && data[i] == 0) {
            i--;
        }

        // Trả về mảng không chứa các byte padding 0
        return Arrays.copyOf(data, i + 1);
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
