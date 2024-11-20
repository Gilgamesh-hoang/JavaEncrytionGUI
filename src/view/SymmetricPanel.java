package view;

import model.Constant;
import model.EncryptionAlgorithm;
import model.EncryptionUtil;
import model.KeyJson;
import model.symmetric.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class SymmetricPanel extends JPanel {
    private List<EncryptionAlgorithm> algorithmList ;
    private EncryptionAlgorithm selectedAlgorithm ;
    private JButton loadKeyBtn;
    private JButton randomKeyBtn;
    private JButton saveKeyBtn;
    private JTextArea inputKey;
    private JTextArea inputDataText;
    private JTextArea outputDataText;
    private JComboBox keyLengthList;
    private JComboBox modeList;
    private JComboBox paddingList;
    private JTextField numberA;
    private JTextField numberB;
    private JLabel lblSB;
    private JLabel lblSA;
    private JLabel lblKey;
    private JScrollPane jScrollPane3;
    private JComboBox listAlgorithms;
    private JButton inputDataFile;
    private JComboBox selectType;
    private JScrollPane jScrollPaneOutputData;
    private JScrollPane jScrollPaneInputData;
    private JTextArea inputPath;
    private JTextArea outputPath;
    private JButton outputDataFile;
    private EncryptionUtil encryptionUtil;

    public SymmetricPanel() {
        this.algorithmList = List.of(
                new AES(),
                new DES(),
                new Blowfish(),
                new PermutationCipher(),
                new AffineCipher(),
                new VigenereCipher());
        this.selectedAlgorithm = algorithmList.get(0);
        this.inputDataText = new JTextArea();
        this.outputDataText = new JTextArea();
        this.inputKey = new JTextArea();
        this.keyLengthList = new JComboBox(selectedAlgorithm.getKeyLengths());
        this.modeList = new JComboBox(selectedAlgorithm.getModes());
        this.paddingList = new JComboBox(selectedAlgorithm.getPaddings());
        this.encryptionUtil = new EncryptionUtil(this, algorithmList.get(0), inputDataText, outputDataText, inputKey);
        this.setLayout(null);
        showView();
        setVisibility();
        setEnableComponents();
    }

    private void showView() {
        JLabel lblNewLabel = new JLabel("Algorithms");
        lblNewLabel.setBounds(10, 10, 125, 23);
        lblNewLabel.setFont(Constant.titleFont);
        this.add(lblNewLabel);

        // Create the list of items
        listAlgorithms = new JComboBox(EncryptionUtil.getNamesAlgorithm(this.algorithmList));
        listAlgorithms.setSelectedIndex(0);
        listAlgorithms.setBounds(10, 41, 134, 23);
        listAlgorithms.setFont(Constant.font);
        this.add(listAlgorithms);

        lblKey = new JLabel("Key");
        lblKey.setFont(Constant.titleFont);
        lblKey.setBounds(290, 10, 40, 23);
        this.add(lblKey);

        JLabel lblChiuDiKey = new JLabel("Key Length");
        lblChiuDiKey.setFont(Constant.titleFont);
        lblChiuDiKey.setBounds(656, 10, 104, 23);
        this.add(lblChiuDiKey);

        inputKey.setLineWrap(true);
        inputKey.setFont(Constant.font);
        inputKey.setEditable(false);
        jScrollPane3 = new JScrollPane(inputKey);
        jScrollPane3.setBounds(290, 41, 356, 61);
        this.add(jScrollPane3);

        keyLengthList.setSelectedIndex(0);
        keyLengthList.setBounds(656, 40, 68, 29);
        keyLengthList.setFont(Constant.font);
        this.add(keyLengthList);

        randomKeyBtn = new JButton("Random key");
        randomKeyBtn.setBounds(656, 79, 159, 29);
        randomKeyBtn.setFont(Constant.font);
        this.add(randomKeyBtn);

        saveKeyBtn = new JButton("Save key");
        saveKeyBtn.setBounds(734, 38, 85, 29);
        saveKeyBtn.setFont(Constant.font);
        this.add(saveKeyBtn);

        loadKeyBtn = new JButton("Load key");
        loadKeyBtn.setBounds(826, 38, 85, 29);
        loadKeyBtn.setFont(Constant.font);
        this.add(loadKeyBtn);

        JLabel lblMode = new JLabel("Mode");
        lblMode.setFont(Constant.titleFont);
        lblMode.setBounds(165, 112, 104, 23);
        this.add(lblMode);

        modeList.setSelectedIndex(0);
        modeList.setFont(Constant.font);
        modeList.setBounds(165, 139, 77, 29);
        this.add(modeList);

        JLabel lblPadding = new JLabel("Padding");
        lblPadding.setFont(Constant.titleFont);
        lblPadding.setBounds(290, 112, 104, 23);
        this.add(lblPadding);

        paddingList.setSelectedIndex(0);
        paddingList.setFont(Constant.font);
        paddingList.setBounds(290, 139, 104, 29);
        this.add(paddingList);

        JLabel lblTextHocFile = new JLabel("Text or File");
        lblTextHocFile.setFont(Constant.titleFont);
        lblTextHocFile.setBounds(10, 112, 104, 23);
        this.add(lblTextHocFile);

        selectType = new JComboBox(new String[]{"Text", "File"});
        selectType.setSelectedIndex(0);
        selectType.setFont(Constant.font);
        selectType.setBounds(10, 139, 115, 29);
        this.add(selectType);

        JButton encryptBtn = new JButton("Encrypt");
        encryptBtn.setBounds(411, 413, 104, 35);
        encryptBtn.setFont(Constant.font);
        this.add(encryptBtn);

        JButton decryptBtn = new JButton("Decrypt");
        decryptBtn.setBounds(411, 469, 104, 35);
        decryptBtn.setFont(Constant.font);
        this.add(decryptBtn);

        JLabel lblNiDungGc = new JLabel("Input data");
        lblNiDungGc.setFont(Constant.titleFont);
        lblNiDungGc.setBounds(21, 233, 125, 23);
        this.add(lblNiDungGc);

        JLabel lblChnThutTon = new JLabel("Output data");
        lblChnThutTon.setFont(Constant.titleFont);
        lblChnThutTon.setBounds(557, 233, 125, 23);
        this.add(lblChnThutTon);

        inputDataText = new JTextArea();
        inputDataText.setLineWrap(true);
        inputDataText.setFont(Constant.font);

        jScrollPaneInputData = new JScrollPane(inputDataText);
        jScrollPaneInputData.setBounds(10, 274, 386, 380);
        this.add(jScrollPaneInputData);

        outputDataText = new JTextArea();
        outputDataText.setLineWrap(true);
        outputDataText.setEditable(false);
        outputDataText.setFont(Constant.font);

        jScrollPaneOutputData = new JScrollPane(outputDataText);
        jScrollPaneOutputData.setBounds(525, 274, 386, 380);
        this.add(jScrollPaneOutputData);

        lblSA = new JLabel("Number a");
        lblSA.setFont(Constant.titleFont);
        lblSA.setBounds(173, 16, 77, 23);
        this.add(lblSA);

        lblSB = new JLabel("Number b");
        lblSB.setFont(Constant.titleFont);
        lblSB.setBounds(280, 16, 68, 23);
        this.add(lblSB);

        numberA = new JTextField();
        numberA.setBounds(173, 45, 68, 25);
        this.add(numberA);
        numberA.setColumns(10);
        numberA.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume(); // Ignore non-digit input
                }
            }
        });
        numberB = new JTextField();
        numberB.setColumns(10);
        numberB.setBounds(280, 44, 68, 25);
        numberB.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume(); // Ignore non-digit input
                }
            }
        });
        this.add(numberB);

        inputDataFile = new JButton("...");
        inputDataFile.setBounds(290, 322, 40, 35);
        this.add(inputDataFile);

        inputPath = new JTextArea();
        inputPath.setText("");
        inputPath.setLineWrap(true);
        inputPath.setFont(new Font("Tahoma", Font.PLAIN, 13));
        inputPath.setEditable(false);
        inputPath.setBounds(10, 293, 270, 80);
        this.add(inputPath);

        outputPath = new JTextArea();
        outputPath.setText("");
        outputPath.setLineWrap(true);
        outputPath.setFont(new Font("Tahoma", Font.PLAIN, 13));
        outputPath.setEditable(false);
        outputPath.setBounds(551, 293, 264, 80);
        this.add(outputPath);

        outputDataFile = new JButton("...");
        outputDataFile.setBounds(839, 322, 40, 35);
        this.add(outputDataFile);

        inputDataFile.addActionListener(e -> getFilePath(true));
        outputDataFile.addActionListener(e -> getFilePath(false));

        selectType.addItemListener(e -> setShowFile());

        listAlgorithms.addItemListener(event -> handleSelectAlgorithm());

        randomKeyBtn.addActionListener(e -> handleRandomKey());

        saveKeyBtn.addActionListener(e -> handleSaveKey());

        loadKeyBtn.addActionListener(e -> handleLoadKey());

        encryptBtn.addActionListener(e -> {
            if (selectedAlgorithm.name().equals(Constant.AFFINE_CIPHER)) {
                encrypt(numberA.getText() + "," + numberB.getText());
            } else {
                encrypt(inputKey.getText());
            }

        });

        decryptBtn.addActionListener(e -> {
            if (selectedAlgorithm.name().equals(Constant.AFFINE_CIPHER)) {
                decrypt(numberA.getText() + "," + numberB.getText());
            } else {
                decrypt(inputKey.getText());
            }
        });

        setShowFile();
    }

    private void handleSelectAlgorithm() {
        int selectedIndex = listAlgorithms.getSelectedIndex();
        encryptionUtil.setSelectedAlgorithm(algorithmList.get(selectedIndex));
        selectedAlgorithm = algorithmList.get(selectedIndex);
        outputPath.setText("");
        outputDataText.setText("");
        setEnableComponents();
        setVisibility();
    }

    void getFilePath(boolean isInput) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose a file");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            if (isInput) {
                File file = new File(path);
                // check file must less than 100kb
                if (file.length() > 100 * 1024 && selectedAlgorithm.name().equals(Constant.PERMUTATION_CIPHER)) {
                    EncryptionUtil.showMessage("Error", "With Permutation cipher, File size must be less than 100KB", JOptionPane.ERROR_MESSAGE, this);
                    return;
                }

                inputPath.setText(path);
                String output = path.substring(0, path.lastIndexOf(".")) + "_" + System.currentTimeMillis()
                        + path.substring(path.lastIndexOf("."));
                outputPath.setText(output);
            } else {
                outputPath.setText(path);
            }
        }

    }

    private void handleSaveKey() {
        if (selectedAlgorithm.name().equals(Constant.AFFINE_CIPHER)) {
            if (!selectedAlgorithm.isValidKey(numberA.getText() + "," + numberB.getText())) {
                EncryptionUtil.showMessage("Error", selectedAlgorithm.getInvalidKeyMessage(), JOptionPane.ERROR_MESSAGE, this);
            } else {
                encryptionUtil.handleSaveKey(numberA.getText() + "," + numberB.getText(), null, null);
            }
        } else if (selectedAlgorithm.name().equals(Constant.VIGENERE_CIPHER)) {
            if (!selectedAlgorithm.isValidKey(inputKey.getText())) {
                EncryptionUtil.showMessage("Error", selectedAlgorithm.getInvalidKeyMessage(), JOptionPane.ERROR_MESSAGE, this);

            } else {
                encryptionUtil.handleSaveKey(inputKey.getText(), null, null);
            }
        } else if (selectedAlgorithm.name().equals(Constant.PERMUTATION_CIPHER)) {
            if (!selectedAlgorithm.isValidKey(inputKey.getText(), inputDataText.getText().length())) {
                EncryptionUtil.showMessage("Error", selectedAlgorithm.getInvalidKeyMessage(), JOptionPane.ERROR_MESSAGE, this);

            } else {
                encryptionUtil.handleSaveKey(inputKey.getText(), null, null);
            }
        } else {
            if (!selectedAlgorithm.isValidKey(inputKey.getText())) {
                EncryptionUtil.showMessage("Error", selectedAlgorithm.getInvalidKeyMessage(), JOptionPane.ERROR_MESSAGE, this);

            } else {
                encryptionUtil.handleSaveKey(inputKey.getText(), modeList.getSelectedItem().toString(),
                        paddingList.getSelectedItem().toString());
            }
        }

    }

    private void handleRandomKey() {
        String key = null;

        if (selectedAlgorithm.name().equals(Constant.AFFINE_CIPHER)) {
            key = selectedAlgorithm.generateKey();
            numberA.setText(key.split(",")[0]);
            numberB.setText(key.split(",")[1]);
        } else if (selectedAlgorithm.name().equals(Constant.PERMUTATION_CIPHER)) {
            if (selectType.getSelectedItem().equals("File")) {
                if (inputPath.getText().isBlank()) {
                    EncryptionUtil.showMessage("Error", "Please input data first", JOptionPane.ERROR_MESSAGE, this);
                    return;
                }
                File file = new File(inputPath.getText());
                // check file must less than 100kb
                if (file.length() > 100 * 1024) {
                    EncryptionUtil.showMessage("Error", "With Permutation cipher, File size must be less than 100KB", JOptionPane.ERROR_MESSAGE, this);
                    return;
                }
                showDialog("Generating key", "Please wait...", JOptionPane.INFORMATION_MESSAGE);
                key = selectedAlgorithm.generateKey(file.length());
                inputKey.setText(key);
            } else {
                if (inputDataText.getText().isBlank()) {
                    EncryptionUtil.showMessage("Error", "Please input data first", JOptionPane.ERROR_MESSAGE, this);
                    return;
                }
                key = selectedAlgorithm.generateKey((long)inputDataText.getText().length());
                inputKey.setText(key);
            }

        } else {
            key = selectedAlgorithm.generateKey(Long.parseLong(keyLengthList.getSelectedItem().toString()));
            inputKey.setText(key);
        }
    }

    private void showDialog(String title, String message, int messageType) {
        JOptionPane optionPane = new JOptionPane(message, messageType, JOptionPane.DEFAULT_OPTION, null, null, null);

        // Tạo JDialog từ JOptionPane
        JDialog dialog = optionPane.createDialog(title);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setModal(false);  // Cho phép người dùng tương tác với các cửa sổ khác

        // Hiển thị JDialog
        dialog.setVisible(true);
        // Sử dụng Timer để đóng JDialog sau 3 giây
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                dialog.dispose();  // Đóng JDialog
            }
        }, 3000);
    }

    void handleLoadKey() {
        KeyJson keyJson = EncryptionUtil.handleLoadKey(this);
        if (keyJson == null || keyJson.getKey().isBlank()) {
            return;
        }

        if (keyJson.getAlgorithm() != null) {
            EncryptionAlgorithm alg = null;
            for (EncryptionAlgorithm encryptionAlgorithm : algorithmList) {
                if (encryptionAlgorithm.name().equals(keyJson.getAlgorithm())) {
                    alg = encryptionAlgorithm;
                    break;
                }
            }
            if (alg == null) {
                EncryptionUtil.showMessage("Error", "Cannot find the algorithm with the name: " + keyJson.getAlgorithm()
                        + ". Please check the file again.", JOptionPane.ERROR_MESSAGE, this);

                return;
            } else {
                selectedAlgorithm = alg;
                setEnableComponents();
                listAlgorithms.setSelectedItem(alg.name());
            }

            if (keyJson.getAlgorithm().equals(Constant.AFFINE_CIPHER)) {
                if (!selectedAlgorithm.isValidKey(keyJson.getKey())) {
                    EncryptionUtil.showMessage("Error", "Key is invalid", JOptionPane.ERROR_MESSAGE, this);
                    return;
                }
                String[] keys = keyJson.getKey().split(",");
                numberA.setText(keys[0]);
                numberB.setText(keys[1]);
            } else if (selectedAlgorithm.name().equals(Constant.PERMUTATION_CIPHER)) {
                inputKey.setText(keyJson.getKey());
            } else {
                if (!selectedAlgorithm.isValidKey(keyJson.getKey())) {
                    EncryptionUtil.showMessage("Error", "Key is invalid", JOptionPane.ERROR_MESSAGE, this);
                    return;
                }
                inputKey.setText(keyJson.getKey());
            }

        }

        if (!keyJson.getAlgorithm().equals(Constant.AFFINE_CIPHER) && !keyJson.getAlgorithm().equals(Constant.PERMUTATION_CIPHER)) {
            keyLengthList.setSelectedItem(Base64.getDecoder().decode(keyJson.getKey()).length * 8+"");
        }

        if (keyJson.getMode() != null && Arrays.asList(selectedAlgorithm.getModes()).contains(keyJson.getMode())) {
            modeList.setSelectedItem(keyJson.getMode());
        }
        if (keyJson.getPadding() != null
                && Arrays.asList(selectedAlgorithm.getPaddings()).contains(keyJson.getPadding())) {
            paddingList.setSelectedItem(keyJson.getPadding());
        }

    }

    public void encrypt(String key) {
        if (selectType.getSelectedItem().equals("File")) {
            try {
                File file = new File(inputPath.getText());
                if (!file.exists()) {
                    EncryptionUtil.showMessage("Error", "File not found", JOptionPane.ERROR_MESSAGE, this);
                    return;
                }
            } catch (Exception e) {
                EncryptionUtil.showMessage("Error", "Please input data first", JOptionPane.ERROR_MESSAGE, this);
                return;
            }
            processFileEncryptionOrDecryption(key, true);
        } else {
            processTextEncryptionOrDecryption(key, true);
        }
    }

    public void decrypt(String key) {
        if (selectType.getSelectedItem().equals("File")) {
            try {
                File file = new File(inputPath.getText());
                if (!file.exists()) {
                    EncryptionUtil.showMessage("Error", "File not found", JOptionPane.ERROR_MESSAGE, this);
                    return;
                }
            } catch (Exception e) {
                EncryptionUtil.showMessage("Error", "Please input data first", JOptionPane.ERROR_MESSAGE, this);
                return;
            }
            processFileEncryptionOrDecryption(key, false);
        } else {
            processTextEncryptionOrDecryption(key, false);
        }
    }

    private void processFileEncryptionOrDecryption(String key, boolean isEncrypt) {
        String inputFilePath = this.inputPath.getText();
        String outputFilePath = this.outputPath.getText();

        if (inputFilePath.isBlank() || outputFilePath.isBlank()) return;


        File file = new File(inputFilePath);
        // check file must less than 100kb
        if (file.length() > 100 * 1024 && selectedAlgorithm.name().equals(Constant.PERMUTATION_CIPHER)) {
            EncryptionUtil.showMessage("Error", "With Permutation cipher, File size must be less than 100KB", JOptionPane.ERROR_MESSAGE, this);
            return;
        }
        if (!validateKey(key, file.length())) return;

        try {

            if (selectedAlgorithm.name().equals(Constant.AES_CIPHER) ||
                    selectedAlgorithm.name().equals(Constant.DES_CIPHER) ||
                    selectedAlgorithm.name().equals(Constant.BLOWFISH_CIPHER)) {
                String mode = modeList.getSelectedItem().toString();
                String padding = paddingList.getSelectedItem().toString();
                int keyLength = Integer.parseInt(keyLengthList.getSelectedItem().toString());

                if (isEncrypt) {
                    selectedAlgorithm.encryptFile(inputFilePath, outputFilePath, key, keyLength, mode, padding);
                    EncryptionUtil.showMessage("Success", "Encrypted file successfully", JOptionPane.INFORMATION_MESSAGE, this);
                } else {
                    selectedAlgorithm.decryptFile(inputFilePath, outputFilePath, key, keyLength, mode, padding);
                    EncryptionUtil.showMessage("Success", "Decrypted file successfully", JOptionPane.INFORMATION_MESSAGE, this);
                }
            } else {
                if (isEncrypt) {
                    selectedAlgorithm.encryptFile(inputFilePath, outputFilePath, key, 0, null, null);
                    EncryptionUtil.showMessage("Success", "Encrypted file successfully", JOptionPane.INFORMATION_MESSAGE, this);
                } else {
                    selectedAlgorithm.decryptFile(inputFilePath, outputFilePath, key, 0, null, null);
                    EncryptionUtil.showMessage("Success", "Decrypted file successfully", JOptionPane.INFORMATION_MESSAGE, this);
                }
            }
        } catch (Exception e) {
            EncryptionUtil.showMessage("Error", e.getMessage(), JOptionPane.ERROR_MESSAGE, this);
        }
    }

    private boolean validateKey(String key, long dataLength) {
        if (key.isBlank()) {
            EncryptionUtil.showMessage("Error", "Key is empty", JOptionPane.ERROR_MESSAGE, this);
            return false;
        }
        if (selectedAlgorithm.name().equals(Constant.PERMUTATION_CIPHER)) {
            if (!selectedAlgorithm.isValidKey(key, dataLength)) {
                EncryptionUtil.showMessage("Error", selectedAlgorithm.getInvalidKeyMessage(),
                        JOptionPane.ERROR_MESSAGE, this);
                return false;
            }
        } else if (!selectedAlgorithm.isValidKey(key)) {
            EncryptionUtil.showMessage("Error", selectedAlgorithm.getInvalidKeyMessage(),
                    JOptionPane.ERROR_MESSAGE, this);
            return false;
        }
        return true;
    }

    private void processTextEncryptionOrDecryption(String key, boolean isEncrypt) {
        String inputTxt = inputDataText.getText();
        String algorithmName = selectedAlgorithm.name();

        if (inputTxt.isEmpty()) return;

        if (!validateKey(key, inputTxt.length())) return;

        String result = "";

        if (algorithmName.equals(Constant.AES_CIPHER) ||
                algorithmName.equals(Constant.DES_CIPHER) ||
                algorithmName.equals(Constant.BLOWFISH_CIPHER)) {

            String mode = modeList.getSelectedItem().toString();
            String padding = paddingList.getSelectedItem().toString();
            int keyLength = Integer.parseInt(keyLengthList.getSelectedItem().toString());

            result = isEncrypt
                    ? selectedAlgorithm.encrypt(inputTxt, key, keyLength, mode, padding)
                    : selectedAlgorithm.decrypt(inputTxt, key, keyLength, mode, padding);
        } else {
            result = isEncrypt ? selectedAlgorithm.encrypt(inputTxt, key) : selectedAlgorithm.decrypt(inputTxt, key);
        }
        outputDataText.setText(result);
    }

    void setShowFile() {
        if (selectType.getSelectedItem().equals("Text")) {
            jScrollPaneInputData.setVisible(true);
            jScrollPaneOutputData.setVisible(true);
            inputDataFile.setVisible(false);
            outputDataFile.setVisible(false);
        } else {
            jScrollPaneInputData.setVisible(false);
            jScrollPaneOutputData.setVisible(false);
            inputDataFile.setVisible(true);
            outputDataFile.setVisible(true);
        }
    }

    void setVisibility() {
        hideAllComponents();

        switch (selectedAlgorithm.name()) {
            case Constant.AFFINE_CIPHER:
                showAffineCipherComponents();
                break;
            case Constant.VIGENERE_CIPHER:
                showVigenereCipherComponents();
                break;
            case Constant.PERMUTATION_CIPHER:
                showPermutationCipherComponents();
                break;
            default:
                showDefaultComponents();
                break;
        }
    }

    void hideAllComponents() {
        jScrollPane3.setVisible(false);
        lblKey.setVisible(false);
        keyLengthList.setEnabled(false);
        modeList.setEnabled(false);
        paddingList.setEnabled(false);
        lblSA.setVisible(false);
        lblSB.setVisible(false);
        numberA.setVisible(false);
        numberB.setVisible(false);
    }

    void showAffineCipherComponents() {
        lblSA.setVisible(true);
        lblSB.setVisible(true);
        numberA.setVisible(true);
        numberB.setVisible(true);
        keyLengthList.setModel(new DefaultComboBoxModel<>());
        modeList.setModel(new DefaultComboBoxModel<>());
        paddingList.setModel(new DefaultComboBoxModel<>());
    }

    void showVigenereCipherComponents() {
        keyLengthList.setEnabled(true);
        jScrollPane3.setVisible(true);
        lblKey.setVisible(true);
    }

    void showPermutationCipherComponents() {
        jScrollPane3.setVisible(true);
        lblKey.setVisible(true);
        keyLengthList.setModel(new DefaultComboBoxModel<>());
        modeList.setModel(new DefaultComboBoxModel<>());
        paddingList.setModel(new DefaultComboBoxModel<>());
    }

    void showDefaultComponents() {
        keyLengthList.setEnabled(true);
        modeList.setEnabled(true);
        paddingList.setEnabled(true);
        jScrollPane3.setVisible(true);
        lblKey.setVisible(true);
    }

    void setEnableComponents() {
        boolean isKeyRequired = selectedAlgorithm.requireKey();
        loadKeyBtn.setEnabled(isKeyRequired);
        saveKeyBtn.setEnabled(isKeyRequired);
        randomKeyBtn.setEnabled(isKeyRequired);
        inputKey.setEnabled(isKeyRequired);
        inputKey.setText("");

        try {
            keyLengthList.setModel(new DefaultComboBoxModel<>(selectedAlgorithm.getKeyLengths()));
        } catch (UnsupportedOperationException e) {
        }

        try {
            modeList.setModel(new DefaultComboBoxModel<>(selectedAlgorithm.getModes()));
        } catch (UnsupportedOperationException e) {
        }

        try {
            paddingList.setModel(new DefaultComboBoxModel<>(selectedAlgorithm.getPaddings()));
        } catch (UnsupportedOperationException e) {
        }
    }
}
