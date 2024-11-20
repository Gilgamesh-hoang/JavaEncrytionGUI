package view;

import model.Constant;
import model.EncryptionUtil;
import model.KeyJson;
import model.hash.Hash;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.concurrent.CompletableFuture;

public class HashPanel extends JPanel {
    private JComboBox selectType;
    private JTextField filePath;
    private JTextArea keyTxt;
    private JTextArea md5Txt;
    private JTextArea sha256Txt;
    private JTextArea sha512Txt;
    private JTextArea hmacTxt;
    private JButton btnSelectFile;
    private JScrollPane scrollPaneInput;
    private Hash hashAlgorithm;
    private JTextArea inputTxt;
    
    private static final String MD5 = "MD5";
    private static final String SHA256 = "SHA-256";
    private static final String SHA512 = "SHA-512";
    private static final String HMAC = "HmacSHA512";
    
    public HashPanel() {
        setLayout(null);
        showView();
        hashAlgorithm = new Hash();

    }
    void showView() {

        JLabel lblTextHocFile = new JLabel("Text or File");
        lblTextHocFile.setFont(Constant.titleFont);
        lblTextHocFile.setBounds(35, 10, 104, 23);
        this.add(lblTextHocFile);

        selectType = new JComboBox(new String[]{"Text", "File"});
        selectType.setSelectedIndex(0);
        selectType.setFont(Constant.font);
        selectType.setBounds(35, 39, 115, 29);
        this.add(selectType);

        JLabel lblFilePath_1_1 = new JLabel("MD5");
        lblFilePath_1_1.setBounds(35, 314, 60, 23);
        this.add(lblFilePath_1_1);
        lblFilePath_1_1.setFont(Constant.titleFont);

        JLabel lblFilePath_1_1_1 = new JLabel("SHA-256");
        lblFilePath_1_1_1.setBounds(35, 407, 60, 23);
        this.add(lblFilePath_1_1_1);
        lblFilePath_1_1_1.setFont(Constant.titleFont);

        JLabel lblFilePath_1_1_2 = new JLabel("SHA-512");
        lblFilePath_1_1_2.setBounds(35, 498, 60, 23);
        this.add(lblFilePath_1_1_2);
        lblFilePath_1_1_2.setFont(Constant.titleFont);

        md5Txt = new JTextArea();
        md5Txt.setLineWrap(false);
        md5Txt.setFont(Constant.font);
        md5Txt.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(md5Txt);
        scrollPane.setBounds(36, 345, 781, 40);
        this.add(scrollPane);

        sha256Txt = new JTextArea();
        sha256Txt.setText("");
        sha256Txt.setFont(Constant.font);
        sha256Txt.setEditable(false);
        JScrollPane scrollPane1 = new JScrollPane(sha256Txt);
        scrollPane1.setBounds(36, 437, 781, 40);
        this.add(scrollPane1);

        sha512Txt = new JTextArea();
        sha512Txt.setText("");
        sha512Txt.setFont(Constant.font);
        sha512Txt.setEditable(false);
        JScrollPane scrollPane2 = new JScrollPane(sha512Txt);
        scrollPane2.setBounds(36, 530, 781, 40);
        this.add(scrollPane2);

        inputTxt = new JTextArea();
        inputTxt.setWrapStyleWord(true);
        inputTxt.setText("");
        inputTxt.setLineWrap(true);
        inputTxt.setFont(Constant.font);
        scrollPaneInput = new JScrollPane(inputTxt);
        scrollPaneInput.setBounds(36, 112, 859, 120);
        this.add(scrollPaneInput);

        hmacTxt = new JTextArea();
        hmacTxt.setFont(Constant.font);
        hmacTxt.setText("");
        hmacTxt.setEditable(false);
        JScrollPane scrollPane2_1 = new JScrollPane(hmacTxt);
        scrollPane2_1.setBounds(35, 622, 781, 40);
        this.add(scrollPane2_1);

        JButton saveMD5Btn = new JButton("Save");
        saveMD5Btn.setBounds(828, 346, 67, 39);
        this.add(saveMD5Btn);

        JButton saveSHA256Btn = new JButton("Save");
        saveSHA256Btn.setBounds(828, 438, 67, 39);
        this.add(saveSHA256Btn);

        JButton saveSHA512Btn = new JButton("Save");
        saveSHA512Btn.setBounds(828, 531, 67, 39);
        this.add(saveSHA512Btn);

        JButton hashBtn = new JButton("Hash");
        hashBtn.setBounds(35, 254, 104, 39);
        this.add(hashBtn);

        JLabel lblFilePath_1 = new JLabel("HMAC-SHA512");
        lblFilePath_1.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblFilePath_1.setBounds(35, 594, 100, 23);
        this.add(lblFilePath_1);

        JButton saveHmacBtn = new JButton("Save");
        saveHmacBtn.setBounds(828, 622, 67, 39);
        this.add(saveHmacBtn);

        keyTxt = new JTextArea();
        keyTxt.setLineWrap(true);
        keyTxt.setEditable(false);
        JScrollPane scrollPane4 = new JScrollPane(keyTxt);
        scrollPane4.setBounds(175, 42, 408, 57);
        this.add(scrollPane4);

        JLabel lblKeyforHmac = new JLabel("Key (for HMAC)");
        lblKeyforHmac.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblKeyforHmac.setBounds(175, 10, 104, 23);
        this.add(lblKeyforHmac);

        JButton saveKeyBtn = new JButton("Save key");
        saveKeyBtn.setBounds(604, 43, 89, 39);
        this.add(saveKeyBtn);

        JButton loadKeyBtn = new JButton("Load key");
        loadKeyBtn.setBounds(703, 43, 89, 39);
        this.add(loadKeyBtn);

        JButton randomKeyBtn = new JButton("Random key");
        randomKeyBtn.setBounds(802, 43, 104, 39);
        this.add(randomKeyBtn);

        filePath = new JTextField();
        filePath.setEditable(false);
        filePath.setBounds(35, 121, 658, 40);
        this.add(filePath);
        filePath.setColumns(10);

        btnSelectFile = new JButton("Select file");
        btnSelectFile.setBounds(35, 181, 104, 39);
        this.add(btnSelectFile);

        selectType.addItemListener(e -> setShowFile());

        randomKeyBtn.addActionListener(e -> handleRandomKey());

        saveKeyBtn.addActionListener(e -> handleSaveKey());

        loadKeyBtn.addActionListener(e -> handleLoadKey());

        hashBtn.addActionListener(e -> handleHash());

        saveMD5Btn.addActionListener(e -> handleSaveHash(MD5));

        saveSHA256Btn.addActionListener(e -> handleSaveHash(SHA256));

        saveSHA512Btn.addActionListener(e -> handleSaveHash(SHA512));

        saveHmacBtn.addActionListener(e -> handleSaveHash(HMAC));

        btnSelectFile.addActionListener(e -> getFilePath());

        setShowFile();

    }

    void getFilePath() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose a file");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            filePath.setText(path);
        }
    }

    private void handleSaveHash(String hashType) {
        String content = "";
        switch (hashType) {
            case MD5 -> content = md5Txt.getText();
            case SHA256 -> content = sha256Txt.getText();
            case SHA512 -> content = sha512Txt.getText();
            case HMAC -> content = hmacTxt.getText();
        }
        if (content.isBlank()) {
            EncryptionUtil.showMessage("Error", "No content to save", JOptionPane.ERROR_MESSAGE, this);
            return;
        }

        String fileName = String.format("hash_%s_%d.txt", hashType, System.currentTimeMillis());
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a location to save the hash");

        fileChooser.setSelectedFile(new File(fileName)); // Default file name
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String path = fileToSave.getAbsolutePath();

            if (!path.endsWith(".txt")) {
                path += ".txt";
            }

            try(BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
                writer.write(content);
                EncryptionUtil.showMessage("Success", "File saved successfully", JOptionPane.INFORMATION_MESSAGE, this);
            } catch (Exception e) {
                EncryptionUtil.showMessage("Error", "Failed to save file", JOptionPane.ERROR_MESSAGE, this);
            }
        }
    }

    private void handleHash() {
        if (selectType.getSelectedItem().equals("Text")) {
            String input = inputTxt.getText();
            if (input.isBlank()) {
                EncryptionUtil.showMessage("Error", "Input is empty. Please enter some text before hashing.", JOptionPane.ERROR_MESSAGE, this);
                return;
            }

            CompletableFuture.runAsync(() -> md5Txt.setText(hashAlgorithm.hashText(input, MD5)));

            CompletableFuture.runAsync(() -> sha256Txt.setText(hashAlgorithm.hashText(input, SHA256)));

            CompletableFuture.runAsync(() -> sha512Txt.setText(hashAlgorithm.hashText(input, SHA512)));

            CompletableFuture.runAsync(() -> {
                String key = keyTxt.getText();
                if (key.isBlank()) {
                    EncryptionUtil.showMessage("Warning", "Key is empty. Please generate a key before hashing with HMAC-SHA512.", JOptionPane.PLAIN_MESSAGE, this);
                    return;
                }
                hmacTxt.setText(hashAlgorithm.hmacText(input, key, HMAC));
            });

        } else {
            String path = filePath.getText();
            if (path.isBlank()) {
                EncryptionUtil.showMessage("Error", "File path is empty. Please select a file before hashing.", JOptionPane.ERROR_MESSAGE, this);
                return;
            }

            File file = new File(path);
            if (!file.exists()) {
                EncryptionUtil.showMessage("Error", "File does not exist. Please select a valid file before hashing.", JOptionPane.ERROR_MESSAGE, this);
                return;
            }

            CompletableFuture.runAsync(() -> md5Txt.setText(hashAlgorithm.hashFile(path, MD5)));

            CompletableFuture.runAsync(() -> sha256Txt.setText(hashAlgorithm.hashFile(path, SHA256)));

            CompletableFuture.runAsync(() -> sha512Txt.setText(hashAlgorithm.hashFile(path, SHA512)));

            CompletableFuture.runAsync(() -> {
                String key = keyTxt.getText();
                if (key.isBlank()) {
                    EncryptionUtil.showMessage("Warning", "Key is empty. Please generate a key before hashing with HMAC-SHA512.", JOptionPane.PLAIN_MESSAGE, this);
                    return;
                }
                hmacTxt.setText(hashAlgorithm.hmacFile(path, keyTxt.getText(), HMAC));
            });

        }
    }


    private void handleRandomKey() {
        try {
            String key = hashAlgorithm.generateKey();
            keyTxt.setText(key);
        } catch (Exception e) {
            EncryptionUtil.showMessage("Error", "Key generation failed", JOptionPane.ERROR_MESSAGE, this);
        }
    }

    void handleLoadKey() {
        KeyJson keyJson = EncryptionUtil.handleLoadKey(this);
        if (keyJson == null || keyJson.getKey().isBlank()) {
            return;
        }
        String key = keyJson.getKey();
        if (key.isBlank()) {
            EncryptionUtil.showMessage("Error", "Key is empty. Please generate a key before loading.",
                    JOptionPane.ERROR_MESSAGE, this);
            return;
        }
        keyTxt.setText(keyJson.getKey());
    }

    private void handleSaveKey() {
        String key = keyTxt.getText();
        if (key.isBlank()) {
            EncryptionUtil.showMessage("Error", "Key is empty. Please generate a key or load a key before saving.", JOptionPane.ERROR_MESSAGE, this);
            return;
        }
        String fileName = String.format("key_hash_%d.dat",System.currentTimeMillis());
        EncryptionUtil.handleSaveKey(key, null, null, this, null, fileName);
    }

    void setShowFile() {
        if (selectType.getSelectedItem().equals("Text")) {
            scrollPaneInput.setVisible(true);
            filePath.setVisible(false);
            btnSelectFile.setVisible(false);
        } else {
            scrollPaneInput.setVisible(false);
            filePath.setVisible(true);
            btnSelectFile.setVisible(true);
        }
        md5Txt.setText("");
        sha256Txt.setText("");
        sha512Txt.setText("");
        hmacTxt.setText("");
    }
}
