package view;

import model.*;
import model.asym.ElGamal;
import model.asym.RSA;

import javax.swing.*;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import java.util.List;

public class AsymmetricPanel extends JPanel {
    private List<EncryptionAlgorithm> algorithmList;
    private EncryptionAlgorithm selectedAlgorithm;
    private JButton loadKeyBtn;
    private JButton randomKeyBtn;
    private JButton saveKeyBtn;
    private JTextArea publicKeyInput;
    private JTextArea inputDataText;
    private JTextArea outputDataText;
    private JComboBox keyLengthList;
    private JComboBox modeList;
    private JComboBox paddingList;
    private JComboBox listAlgorithms;
    private JTextArea privateKeyInput;

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

        JLabel lblKey = new JLabel("Public key");
        lblKey.setFont(Constant.titleFont);
        lblKey.setBounds(181, 10, 104, 23);
        this.add(lblKey);

        publicKeyInput = new JTextArea();
        publicKeyInput.setLineWrap(true);
        publicKeyInput.setFont(Constant.font);
        publicKeyInput.setEditable(false);
        JScrollPane jScrollPane3 = new JScrollPane(publicKeyInput);
        jScrollPane3.setBounds(181, 41, 356, 61);
        this.add(jScrollPane3);

        JLabel lblPrivateKey = new JLabel("Private key");
        lblPrivateKey.setFont(Constant.titleFont);
        lblPrivateKey.setBounds(578, 10, 104, 23);
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
        lblChiuDiKey.setBounds(292, 122, 104, 23);
        this.add(lblChiuDiKey);

        keyLengthList.setSelectedIndex(0);
        keyLengthList.setBounds(292, 152, 104, 29);
        keyLengthList.setFont(Constant.font);
        this.add(keyLengthList);

        randomKeyBtn = new JButton("Random key");
        randomKeyBtn.setBounds(586, 152, 159, 29);
        randomKeyBtn.setFont(Constant.font);
        this.add(randomKeyBtn);

        saveKeyBtn = new JButton("Save key");
        saveKeyBtn.setBounds(452, 152, 85, 29);
        saveKeyBtn.setFont(Constant.font);
        this.add(saveKeyBtn);

        loadKeyBtn = new JButton("Load key");
        loadKeyBtn.setBounds(793, 152, 85, 29);
        loadKeyBtn.setFont(Constant.font);
        this.add(loadKeyBtn);

        JLabel lblMode = new JLabel("Mode");
        lblMode.setFont(Constant.titleFont);
        lblMode.setBounds(10, 127, 104, 23);
        this.add(lblMode);

        modeList.setSelectedIndex(0);
        modeList.setFont(Constant.font);
        modeList.setBounds(10, 154, 77, 29);
        this.add(modeList);

        JLabel lblPadding = new JLabel("Padding");
        lblPadding.setFont(Constant.titleFont);
        lblPadding.setBounds(135, 127, 104, 23);
        this.add(lblPadding);

        paddingList.setSelectedIndex(0);
        paddingList.setFont(Constant.font);
        paddingList.setBounds(135, 154, 104, 29);
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
        lblNiDungGc.setBounds(21, 233, 125, 23);
        this.add(lblNiDungGc);

        JLabel lblChnThutTon = new JLabel("Output data");
        lblChnThutTon.setFont(Constant.titleFont);
        lblChnThutTon.setBounds(557, 233, 125, 23);
        this.add(lblChnThutTon);

        inputDataText = new JTextArea();
        inputDataText.setLineWrap(true);
        inputDataText.setFont(Constant.font);

        JScrollPane jScrollPaneInputData = new JScrollPane(inputDataText);
        jScrollPaneInputData.setBounds(10, 274, 386, 380);
        this.add(jScrollPaneInputData);

        outputDataText = new JTextArea();
        outputDataText.setLineWrap(true);
        outputDataText.setEditable(false);
        outputDataText.setFont(Constant.font);

        JScrollPane jScrollPaneOutputData = new JScrollPane(outputDataText);
        jScrollPaneOutputData.setBounds(525, 274, 386, 380);
        this.add(jScrollPaneOutputData);

        listAlgorithms.addItemListener(event -> handleSelectAlgorithm());

        randomKeyBtn.addActionListener(e -> handleRandomKey());
        saveKeyBtn.addActionListener(e -> handleSaveKey());

        loadKeyBtn.addActionListener(e -> handleLoadKey());

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

        if (inputTxt.isEmpty()) return;

        KeyPair keyPair = new KeyPair(publicKeyInput.getText(), privateKeyInput.getText());
        if (!selectedAlgorithm.isValidKey(keyPair)) {
            EncryptionUtil.showMessage("Error", selectedAlgorithm.getInvalidKeyMessage(), JOptionPane.ERROR_MESSAGE, this);
        }

        String mode = modeList.getSelectedItem().toString();
        String padding = paddingList.getSelectedItem().toString();
        try {
            String result = isEncrypt ? selectedAlgorithm.encrypt(inputTxt, keyPair, mode, padding)
                    : selectedAlgorithm.decrypt(inputTxt, keyPair, mode, padding);
            outputDataText.setText(result);
        } catch (Exception e) {
            EncryptionUtil.showMessage("Error", "Error during " + (isEncrypt ? "encrypt" : "decrypt"), JOptionPane.ERROR_MESSAGE, this);
        }
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
            }
            selectedAlgorithm = alg;
            setEnableComponents();
            listAlgorithms.setSelectedItem(alg.name());

            String[] keyParts = keyJson.getKey().split(Constant.SPLIT_KEY);
            KeyPair keyPair = new KeyPair(keyParts[0], keyParts[1]);
            if (!selectedAlgorithm.isValidKey(keyPair)) {
                EncryptionUtil.showMessage("Error", selectedAlgorithm.getInvalidKeyMessage(), JOptionPane.ERROR_MESSAGE, this);
                return;
            }
            publicKeyInput.setText(keyPair.getPublicKey());
            privateKeyInput.setText(keyPair.getPrivateKey());
        }

        try {
            keyLengthList.setSelectedItem(String.valueOf(selectedAlgorithm.getKeyLength(keyJson.getKey())));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (keyJson.getMode() != null && Arrays.asList(selectedAlgorithm.getModes()).contains(keyJson.getMode())) {
            modeList.setSelectedItem(keyJson.getMode());
        }
        if (keyJson.getPadding() != null
                && Arrays.asList(selectedAlgorithm.getPaddings()).contains(keyJson.getPadding())) {
            paddingList.setSelectedItem(keyJson.getPadding());
        }

    }

    private void handleSaveKey() {
        KeyPair keyPair = new KeyPair(publicKeyInput.getText(), privateKeyInput.getText());
        if (!selectedAlgorithm.isValidKey(keyPair)) {
            EncryptionUtil.showMessage("Error", selectedAlgorithm.getInvalidKeyMessage(), JOptionPane.ERROR_MESSAGE, this);
        } else {
            String key = keyPair.getPublicKey() + Constant.SPLIT_KEY + keyPair.getPrivateKey();
            EncryptionUtil.handleSaveKey(key, modeList.getSelectedItem().toString(),
                    paddingList.getSelectedItem().toString(), this, selectedAlgorithm.name());
        }
    }

    private void handleRandomKey() {
        try {
            KeyPair keyPair = selectedAlgorithm.generateKey(Integer.parseInt(keyLengthList.getSelectedItem().toString()));
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
        loadKeyBtn.setEnabled(isKeyRequired);
        saveKeyBtn.setEnabled(isKeyRequired);
        randomKeyBtn.setEnabled(isKeyRequired);
        publicKeyInput.setEnabled(isKeyRequired);
        publicKeyInput.setText("");
        privateKeyInput.setEnabled(isKeyRequired);
        privateKeyInput.setText("");

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
