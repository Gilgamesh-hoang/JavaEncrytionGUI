package view;

import model.Constant;
import model.EncryptionUtil;
import model.KeyJson;
import model.KeyPair;
import model.digital_signature.SignRSA;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

public class MainView extends JFrame {

    private JPanel contentPane;
    SignRSA signAlgorithm;
    private JButton loadPublicKeyBtn;
    private JButton randomKeyBtn;
    private JButton savePublicKeyBtn;
    private JTextArea publicKeyInput;
    private JTextArea privateKeyInput;
    private JButton savePrivateKeyBtn;
    private JButton loadPrivateKeyBtn;
    private JTextArea filePath;
    private JTextArea signatureTxt;

    public MainView() {
        this.signAlgorithm = new SignRSA();
        showView();
    }

    void showView() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(100, 100, 955, 754);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(contentPane);
        contentPane.setLayout(null);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(5, 5, 931, 702);
        tabbedPane.setFont(Constant.font);
        contentPane.add(tabbedPane);

//        JPanel basicPanel = new BasicPanel();
//        tabbedPane.addTab("Basic", null, basicPanel, null);
//
//        JPanel symmetricPanel = new SymmetricPanel();
//        tabbedPane.addTab("Symmetric", null, symmetricPanel, null);

//        JPanel AsymmPanel = new AsymmetricPanel();
//        tabbedPane.addTab("Asymmetric", null, AsymmPanel, null);

//		JPanel panel = new DigitalSignaturePanel();
//		tabbedPane.addTab("Sign", null, panel, null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        tabbedPane.addTab("Asymmetric", null, panel, null);

        JLabel lblKey = new JLabel("Public key (to verify)");
        lblKey.setFont(Constant.titleFont);
        lblKey.setBounds(46, 10, 197, 23);
        panel.add(lblKey);

        publicKeyInput = new JTextArea();
        publicKeyInput.setLineWrap(true);
        publicKeyInput.setFont(Constant.font);
        publicKeyInput.setEditable(false);
        JScrollPane jScrollPane3 = new JScrollPane(publicKeyInput);
        jScrollPane3.setBounds(46, 41, 356, 61);
        panel.add(jScrollPane3);

        JLabel lblPrivateKey = new JLabel("Private key (to sign)");
        lblPrivateKey.setFont(Constant.titleFont);
        lblPrivateKey.setBounds(520, 10, 209, 23);
        panel.add(lblPrivateKey);

        privateKeyInput = new JTextArea();
        privateKeyInput.setLineWrap(true);
        privateKeyInput.setFont(Constant.font);
        privateKeyInput.setEditable(false);
        JScrollPane jScrollPane4 = new JScrollPane(privateKeyInput);
        jScrollPane4.setBounds(520, 42, 354, 59);
        panel.add(jScrollPane4);

        randomKeyBtn = new JButton("<html><center>Random<br>key pair<br></center></html>");
        randomKeyBtn.setBounds(422, 41, 81, 61);
        randomKeyBtn.setFont(Constant.font);
        panel.add(randomKeyBtn);

        savePublicKeyBtn = new JButton("Save public key");
        savePublicKeyBtn.setBounds(46, 126, 134, 35);
        savePublicKeyBtn.setFont(Constant.font);
        panel.add(savePublicKeyBtn);

        loadPublicKeyBtn = new JButton("Load public key");
        loadPublicKeyBtn.setBounds(210, 126, 125, 35);
        loadPublicKeyBtn.setFont(Constant.font);
        panel.add(loadPublicKeyBtn);

        savePrivateKeyBtn = new JButton("Save private key");
        savePrivateKeyBtn.setFont(Constant.font);
        savePrivateKeyBtn.setBounds(579, 126, 134, 35);
        panel.add(savePrivateKeyBtn);

        loadPrivateKeyBtn = new JButton("Load private key");
        loadPrivateKeyBtn.setFont(Constant.font);
        loadPrivateKeyBtn.setBounds(740, 126, 134, 35);
        panel.add(loadPrivateKeyBtn);

        JButton saveSignatureBtn = new JButton("Save signature");
        saveSignatureBtn.setBounds(46, 316, 134, 35);
        saveSignatureBtn.setFont(Constant.font);
        panel.add(saveSignatureBtn);

        JButton loadSignaturetBtn = new JButton("Load signature");
        loadSignaturetBtn.setBounds(210, 316, 125, 35);
        loadSignaturetBtn.setFont(Constant.font);
        panel.add(loadSignaturetBtn);

        filePath = new JTextArea();
        filePath.setText("");
        filePath.setLineWrap(true);
        filePath.setFont(Constant.font);
        filePath.setEditable(false);
        filePath.setBounds(520, 232, 301, 59);
        panel.add(filePath);

        JButton selectFileBtn = new JButton("...");
        selectFileBtn.setFont(Constant.font);
        selectFileBtn.setBounds(831, 244, 45, 35);
        panel.add(selectFileBtn);

        signatureTxt = new JTextArea();
        signatureTxt.setLineWrap(true);
        signatureTxt.setFont(Constant.font);
        signatureTxt.setEditable(false);

        JScrollPane jScrollPane5 = new JScrollPane(signatureTxt);
        jScrollPane5.setBounds(46, 231, 354, 59);
        panel.add(jScrollPane5);

        JLabel lblSignature = new JLabel("Signature");
        lblSignature.setFont(Constant.titleFont);
        lblSignature.setBounds(46, 198, 104, 23);
        panel.add(lblSignature);

        JButton verifyBtn = new JButton("Verify signature");
        verifyBtn.setFont(Constant.font);
        verifyBtn.setBounds(740, 316, 134, 35);
        panel.add(verifyBtn);

        JButton signBtn = new JButton("Sign file");
        signBtn.setFont(Constant.font);
        signBtn.setBounds(579, 316, 134, 35);
        panel.add(signBtn);

        JLabel lblFilePath = new JLabel("File path");
        lblFilePath.setFont(Constant.titleFont);
        lblFilePath.setBounds(520, 198, 104, 23);
        panel.add(lblFilePath);

        randomKeyBtn.addActionListener(e -> handleRandomKey());

        savePublicKeyBtn.addActionListener(e -> handleSaveKey(true));

        savePrivateKeyBtn.addActionListener(e -> handleSaveKey(false));

        loadPublicKeyBtn.addActionListener(e -> handleLoadKey(true));

        loadPrivateKeyBtn.addActionListener(e -> handleLoadKey(false));

        saveSignatureBtn.addActionListener(e -> handleSaveSignature());

        loadSignaturetBtn.addActionListener(e -> handleLoadSignature());

        signBtn.addActionListener(e -> handleSign());

        verifyBtn.addActionListener(e -> handleVerify());

        selectFileBtn.addActionListener(e -> getFilePath());
//
//		JPanel menuHash = new HashPanel();
//		tabbedPane.addTab("Hash", null, menuHash, null);
//

    }

    private void handleVerify() {
        String path = filePath.getText();
        if (path.isBlank()) {
            EncryptionUtil.showMessage("Error", "Please select a file to verify", JOptionPane.ERROR_MESSAGE, this);
            return;
        }

        File file = new File(path);
        if (!file.exists()) {
            EncryptionUtil.showMessage("Error", "File not found", JOptionPane.ERROR_MESSAGE, this);
            return;
        }

        String publicKey = publicKeyInput.getText();
        if (publicKey.isBlank()) {
            EncryptionUtil.showMessage("Error", "Please load a public key to verify", JOptionPane.ERROR_MESSAGE, this);
            return;
        }

        String signature = signatureTxt.getText();
        if (signature.isBlank()) {
            EncryptionUtil.showMessage("Error", "Please load a signature to verify", JOptionPane.ERROR_MESSAGE, this);
            return;
        }

        try {
            boolean result = signAlgorithm.verifySignature(file, signature, publicKey);
            String message = result ? "Signature is valid" : "Signature is invalid";
            EncryptionUtil.showMessage("Result", message, JOptionPane.INFORMATION_MESSAGE, this);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void handleSign() {
        String path =filePath.getText();
        if (path.isBlank()) {
            EncryptionUtil.showMessage("Error", "Please select a file to sign", JOptionPane.ERROR_MESSAGE, this);
            return;
        }

        File file = new File(path);
        if (!file.exists()) {
            EncryptionUtil.showMessage("Error", "File not found", JOptionPane.ERROR_MESSAGE, this);
            return;
        }

        String privateKey = privateKeyInput.getText();
        if (privateKey.isBlank()) {
            EncryptionUtil.showMessage("Error", "Please load a private key to sign", JOptionPane.ERROR_MESSAGE, this);
            return;
        }

        try {
            String signature = signAlgorithm.signFile(file, privateKey);
            signatureTxt.setText(signature);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void handleLoadSignature() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a signature file");

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            String path = fileToLoad.getAbsolutePath();

            // Ensure the file has a .json extension
            if (!path.endsWith(".sig")) {
                EncryptionUtil.showMessage("Error","Invalid file! The file must be in .sig format", JOptionPane.ERROR_MESSAGE, this);
            }

            try (FileInputStream fis = new FileInputStream(path)) {
                byte[] data = new byte[(int) fileToLoad.length()];
                fis.read(data);
                signatureTxt.setText(new String(data));
            } catch (IOException e) {
                e.printStackTrace();
                EncryptionUtil.showMessage("Error", e.getMessage(), JOptionPane.ERROR_MESSAGE, this);
            }
        }
    }

    private void handleSaveSignature() {
        String signature = signatureTxt.getText();
        if (signature.isBlank()) {
            EncryptionUtil.showMessage("Error", "Signature is empty. Please sign a file before saving.",
                    JOptionPane.ERROR_MESSAGE, this);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a location to save the key");

        String fileName = String.format("signature_%d.sig", System.currentTimeMillis());

        fileChooser.setSelectedFile(new File(fileName)); // Default file name
        fileChooser.setFileFilter(new FileNameExtensionFilter("Signature Files", "sig"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String path = fileToSave.getAbsolutePath();

            // Ensure the file has a .json extension
            if (!path.endsWith(".sig")) {
                path += ".sig";
            }

            try (FileOutputStream fos = new FileOutputStream(path)) {
                fos.write(signature.getBytes());
                fos.flush();
                EncryptionUtil.showMessage("Success", "Signature is saved", JOptionPane.INFORMATION_MESSAGE, this);
            } catch (IOException e) {
                e.printStackTrace();
                EncryptionUtil.showMessage("Error", e.getMessage(), JOptionPane.ERROR_MESSAGE, this);
            }
        }

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

    private void handleRandomKey() {
        try {
            KeyPair keyPair = signAlgorithm.generateKeyPair();
            publicKeyInput.setText(keyPair.getPublicKey());
            privateKeyInput.setText(keyPair.getPrivateKey());
        } catch (Exception e) {
            EncryptionUtil.showMessage("Error", "Key generation failed", JOptionPane.ERROR_MESSAGE, this);
        }
    }

    private void handleSaveKey(boolean isPublic) {
        String key = isPublic ? publicKeyInput.getText() : privateKeyInput.getText();

        if (key.isBlank()) {
            EncryptionUtil.showMessage(
                    "Error", "Key is empty. Please generate a key or load a key before saving.",
                    JOptionPane.ERROR_MESSAGE, this
            );
            return;
        }

        String keyType = isPublic ? "public" : "private";
        String fileName = String.format("%s_%s-key_%s.json",
                "Digital-Signature", keyType, System.currentTimeMillis());

        EncryptionUtil.handleSaveKey(key, null, null, this, null, fileName);
    }

    private void handleLoadKey(boolean isPublic) {
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

        if (isPublic) {
            publicKeyInput.setText(keyJson.getKey());
        } else {
            privateKeyInput.setText(keyJson.getKey());
        }


    }
}
