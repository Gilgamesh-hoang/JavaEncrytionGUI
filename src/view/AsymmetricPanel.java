package view;

import model.*;
import model.asymmetric.ElGamal;
import model.asymmetric.RSA;

import javax.swing.*;
import java.awt.*;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import java.util.List;

public class AsymmetricPanel extends JPanel {
    private List<EncryptionAlgorithm> algorithmList;
    private EncryptionAlgorithm selectedAlgorithm;
    private JButton loadPublicKeyBtn;
    private JButton randomKeyBtn;
    private JButton savePublicKeyBtn;
    private JTextArea publicKeyInput;
    private JTextArea inputDataText;
    private JTextArea outputDataText;
    private JComboBox keyLengthList;
    private JComboBox modeList;
    private JComboBox paddingList;
    private JComboBox listAlgorithms;
    private JTextArea privateKeyInput;
    private JButton savePrivateKeyBtn;
    private JButton loadPrivateKeyBtn;

    public AsymmetricPanel() {
        setLayout(null);
        this.inputDataText = new JTextArea();
        this.outputDataText = new JTextArea();
        this.algorithmList = List.of(new RSA(), new ElGamal());
        this.selectedAlgorithm = algorithmList.get(0);
        this.keyLengthList = new JComboBox(selectedAlgorithm.getKeyLengths());
        this.modeList = new JComboBox(selectedAlgorithm.getModes());
        this.paddingList = new JComboBox(selectedAlgorithm.getPaddings());
        showView();
        setEnableComponents();
    }

    private void showView() {
        JLabel lblNewLabel = new JLabel("Algorithms");
        lblNewLabel.setBounds(10, 10, 125, 23);
        lblNewLabel.setFont(Constant.titleFont);
        this.add(lblNewLabel);

        listAlgorithms = new JComboBox(EncryptionUtil.getNamesAlgorithm(this.algorithmList));
        listAlgorithms.setSelectedIndex(0);
        listAlgorithms.setBounds(10, 41, 134, 23);
        listAlgorithms.setFont(Constant.font);
        this.add(listAlgorithms);

        JLabel lblKey = new JLabel("Public key (for encryption)");
        lblKey.setFont(Constant.titleFont);
        lblKey.setBounds(181, 10, 197, 23);
        this.add(lblKey);

        publicKeyInput = new JTextArea();
        publicKeyInput.setLineWrap(true);
        publicKeyInput.setFont(Constant.font);
        publicKeyInput.setEditable(false);
        JScrollPane jScrollPane3 = new JScrollPane(publicKeyInput);
        jScrollPane3.setBounds(181, 41, 356, 61);
        this.add(jScrollPane3);

        JLabel lblPrivateKey = new JLabel("Private key (for decryption)");
        lblPrivateKey.setFont(Constant.titleFont);
        lblPrivateKey.setBounds(578, 10, 209, 23);
        this.add(lblPrivateKey);

        privateKeyInput = new JTextArea();
        privateKeyInput.setLineWrap(true);
        privateKeyInput.setFont(Constant.font);
        privateKeyInput.setEditable(false);
        JScrollPane jScrollPane4 = new JScrollPane(privateKeyInput);
        jScrollPane4.setBounds(579, 42, 354, 59);
        this.add(jScrollPane4);

        JLabel lblChiuDiKey = new JLabel("Key Length");
        lblChiuDiKey.setFont(Constant.titleFont);
        lblChiuDiKey.setBounds(10, 82, 104, 23);
        this.add(lblChiuDiKey);

        keyLengthList.setSelectedIndex(0);
        keyLengthList.setBounds(10, 112, 104, 29);
        keyLengthList.setFont(Constant.font);
        this.add(keyLengthList);

        randomKeyBtn = new JButton("Random key pair");
        randomKeyBtn.setBounds(280, 181, 159, 29);
        randomKeyBtn.setFont(Constant.font);
        this.add(randomKeyBtn);

        savePublicKeyBtn = new JButton("Save public key");
        savePublicKeyBtn.setBounds(181, 112, 134, 29);
        savePublicKeyBtn.setFont(Constant.font);
        this.add(savePublicKeyBtn);

        loadPublicKeyBtn = new JButton("Load public key");
        loadPublicKeyBtn.setBounds(337, 112, 125, 29);
        loadPublicKeyBtn.setFont(Constant.font);
        this.add(loadPublicKeyBtn);

        savePrivateKeyBtn = new JButton("Save private key");
        savePrivateKeyBtn.setFont(new Font("Tahoma", Font.PLAIN, 13));
        savePrivateKeyBtn.setBounds(578, 111, 134, 29);
        add(savePrivateKeyBtn);

        loadPrivateKeyBtn = new JButton("Load private key");
        loadPrivateKeyBtn.setFont(new Font("Tahoma", Font.PLAIN, 13));
        loadPrivateKeyBtn.setBounds(733, 111, 134, 29);
        add(loadPrivateKeyBtn);

        JLabel lblMode = new JLabel("Mode");
        lblMode.setFont(Constant.titleFont);
        lblMode.setBounds(154, 154, 104, 23);
        this.add(lblMode);

        modeList.setSelectedIndex(0);
        modeList.setFont(Constant.font);
        modeList.setBounds(154, 181, 77, 29);
        this.add(modeList);

        JLabel lblPadding = new JLabel("Padding");
        lblPadding.setFont(Constant.titleFont);
        lblPadding.setBounds(10, 155, 104, 23);
        this.add(lblPadding);

        paddingList.setSelectedIndex(0);
        paddingList.setFont(Constant.font);
        paddingList.setBounds(10, 182, 104, 29);
        this.add(paddingList);

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
        lblNiDungGc.setBounds(10, 251, 125, 23);
        this.add(lblNiDungGc);

        JLabel lblChnThutTon = new JLabel("Output data");
        lblChnThutTon.setFont(Constant.titleFont);
        lblChnThutTon.setBounds(526, 249, 125, 23);
        this.add(lblChnThutTon);

        inputDataText = new JTextArea();
        inputDataText.setLineWrap(true);
        inputDataText.setFont(Constant.font);

        JScrollPane jScrollPaneInputData = new JScrollPane(inputDataText);
        jScrollPaneInputData.setBounds(10, 275, 386, 380);
        this.add(jScrollPaneInputData);

        outputDataText = new JTextArea();
        outputDataText.setLineWrap(true);
        outputDataText.setEditable(false);
        outputDataText.setFont(Constant.font);

        JScrollPane jScrollPaneOutputData = new JScrollPane(outputDataText);
        jScrollPaneOutputData.setBounds(525, 275, 386, 380);
        this.add(jScrollPaneOutputData);

        listAlgorithms.addItemListener(event -> handleSelectAlgorithm());

        randomKeyBtn.addActionListener(e -> handleRandomKey());

        savePublicKeyBtn.addActionListener(e -> handleSaveKey(true));

        savePrivateKeyBtn.addActionListener(e -> handleSaveKey(false));

        loadPublicKeyBtn.addActionListener(e -> handleLoadKey(true));

        loadPrivateKeyBtn.addActionListener(e -> handleLoadKey(false));

        encryptBtn.addActionListener(e -> encrypt());

        decryptBtn.addActionListener(e -> decrypt());

    }

    public void encrypt() {
        processEncryptOrDecrypt(true);
    }

    public void decrypt() {
        processEncryptOrDecrypt(false);
    }

    private void processEncryptOrDecrypt(boolean isEncrypt) {
        String inputTxt = inputDataText.getText();

        if (inputTxt.isEmpty())
            return;

        KeyPair keyPair = new KeyPair(publicKeyInput.getText(), privateKeyInput.getText());
//        if (!selectedAlgorithm.isValidKey(keyPair)) {
//            EncryptionUtil.showMessage("Error", selectedAlgorithm.getInvalidKeyMessage(), JOptionPane.ERROR_MESSAGE,
//                    this);
//        }

        String mode = modeList.getSelectedItem().toString();
        String padding = paddingList.getSelectedItem().toString();
        try {
            String result = isEncrypt ? selectedAlgorithm.encrypt(inputTxt, keyPair, mode, padding)
                    : selectedAlgorithm.decrypt(inputTxt, keyPair, mode, padding);
            outputDataText.setText(result);
        } catch (Exception e) {
            EncryptionUtil.showMessage("Error", "Error during " + (isEncrypt ? "encrypt" : "decrypt"),
                    JOptionPane.ERROR_MESSAGE, this);
        }
    }

    void handleLoadKey(boolean isPublic) {
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
            }
            selectedAlgorithm = alg;
            setEnableComponents();
            listAlgorithms.setSelectedItem(alg.name());

            String key = keyJson.getKey();
            if (key.isBlank()) {
                EncryptionUtil.showMessage("Error", selectedAlgorithm.getInvalidKeyMessage(), JOptionPane.ERROR_MESSAGE,
                        this);
                return;
            }

            if (isPublic) {
                publicKeyInput.setText(keyJson.getKey());
            } else {
                privateKeyInput.setText(keyJson.getKey());
            }

        }

//        try {
//            keyLengthList.setSelectedItem(String.valueOf(selectedAlgorithm.getKeyLength(keyJson.getKey())));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        if (keyJson.getMode() != null && Arrays.asList(selectedAlgorithm.getModes()).contains(keyJson.getMode())) {
            modeList.setSelectedItem(keyJson.getMode());
        }
        if (keyJson.getPadding() != null
                && Arrays.asList(selectedAlgorithm.getPaddings()).contains(keyJson.getPadding())) {
            paddingList.setSelectedItem(keyJson.getPadding());
        }

    }

    private void handleSaveKey(boolean isPublic) {
        if (isPublic) {
            String key = publicKeyInput.getText();
            if (key.isBlank()) {
                EncryptionUtil.showMessage("Error", selectedAlgorithm.getInvalidKeyMessage(), JOptionPane.ERROR_MESSAGE,
                        this);
            } else {
                String fileName = String.format("%s_public-key_%s.json", selectedAlgorithm.name(), System.currentTimeMillis());
                EncryptionUtil.handleSaveKey(key, modeList.getSelectedItem().toString(),
                        paddingList.getSelectedItem().toString(), this, selectedAlgorithm.name(), fileName);
            }
        } else {
            String key = privateKeyInput.getText();
            if (key.isBlank()) {
                EncryptionUtil.showMessage("Error", selectedAlgorithm.getInvalidKeyMessage(), JOptionPane.ERROR_MESSAGE,
                        this);
            } else {
                String fileName = String.format("%s_private-key_%s.json", selectedAlgorithm.name(), System.currentTimeMillis());
                EncryptionUtil.handleSaveKey(key, modeList.getSelectedItem().toString(),
                        paddingList.getSelectedItem().toString(), this, selectedAlgorithm.name(), fileName);
            }
        }

    }

    private void handleRandomKey() {
        try {
            KeyPair keyPair = selectedAlgorithm
                    .generateKey(Integer.parseInt(keyLengthList.getSelectedItem().toString()));
            publicKeyInput.setText(keyPair.getPublicKey());
            privateKeyInput.setText(keyPair.getPrivateKey());
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            EncryptionUtil.showMessage("Error", "Key generation failed", JOptionPane.ERROR_MESSAGE, this);
        }
    }

    private void handleSelectAlgorithm() {
        int selectedIndex = listAlgorithms.getSelectedIndex();
        selectedAlgorithm = algorithmList.get(selectedIndex);
        setEnableComponents();
    }

    void setEnableComponents() {
        boolean isKeyRequired = selectedAlgorithm.requireKey();
        loadPublicKeyBtn.setEnabled(isKeyRequired);
        savePublicKeyBtn.setEnabled(isKeyRequired);
        randomKeyBtn.setEnabled(isKeyRequired);
        publicKeyInput.setEnabled(isKeyRequired);
//        publicKeyInput.setText("");
        privateKeyInput.setEnabled(isKeyRequired);
//        privateKeyInput.setText("");

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
