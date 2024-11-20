package model;


import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.JTextComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFileChooser;
import java.awt.Container;
import java.io.*;
import java.security.NoSuchAlgorithmException;
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
            showMessage("Error", selectedAlgorithm.getInvalidKeyMessage(), JOptionPane.ERROR_MESSAGE, frame);
        } else {
            String result = isEncrypt ? selectedAlgorithm.encrypt(inputTxt, key) : selectedAlgorithm.decrypt(inputTxt, key);
            outputData.setText(result);
        }
    }


    public boolean invalidKey() {
        return selectedAlgorithm.requireKey() && !selectedAlgorithm.isValidKey(inputKey.getText());
    }


    public static KeyJson handleLoadKey(Container frame) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a key file");

        int userSelection = fileChooser.showOpenDialog(frame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileToLoad))){
                String path = fileToLoad.getAbsolutePath();

                // Ensure the file has a .dat extension
                if (!path.endsWith(".dat")) {
                    showMessage("Error", "Invalid file! The file must be in .dat format", JOptionPane.ERROR_MESSAGE, frame);
                    return null;
                }
                return (KeyJson) ois.readObject();

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                showMessage("Error", e.getMessage(), JOptionPane.ERROR_MESSAGE, frame);
            }
        }
        return null;
    }


    public void handleSaveKey(String key, String mode, String padding) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a location to save the key");

        // Set default file name and filter for .dat files
        String fileName = String.format("%s_key_%s.dat", selectedAlgorithm.name(), System.currentTimeMillis());
        fileChooser.setSelectedFile(new File(fileName)); // Default file name
        fileChooser.setFileFilter(new FileNameExtensionFilter("Key Files", "dat"));

        int userSelection = fileChooser.showSaveDialog(frame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String path = fileToSave.getAbsolutePath();

            // Ensure the file has a .dat extension
            if (!path.endsWith(".dat")) {
                path += ".dat";
            }

            // Create an instance of KeyJson
            KeyJson keyJson = new KeyJson(selectedAlgorithm.name(), key, mode, padding);

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
                oos.writeObject(keyJson);
                showMessage("Success", "Key is saved", JOptionPane.INFORMATION_MESSAGE, frame);
            } catch (IOException e) {
                e.printStackTrace();
                showMessage("Error", e.getMessage(), JOptionPane.ERROR_MESSAGE, frame);
            }
        }
    }
    public static void handleSaveKey(String key, String mode, String padding, Container frame, String algName, String fileName) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a location to save the key");

        // Set default file name and filter for .dat files
        fileChooser.setSelectedFile(new File(fileName)); // Default file name
        fileChooser.setFileFilter(new FileNameExtensionFilter("key Files", "dat"));

        int userSelection = fileChooser.showSaveDialog(frame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String path = fileToSave.getAbsolutePath();

            // Ensure the file has a .dat extension
            if (!path.endsWith(".dat")) {
                path += ".dat";
            }

            // Create an instance of KeyJson
            KeyJson keyJson = new KeyJson(algName, key, mode, padding);

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
                oos.writeObject(keyJson);
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


    public static void showMessage(String title, String message, int type, Container frame) {
        JOptionPane.showMessageDialog(frame, message, title, type);
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
