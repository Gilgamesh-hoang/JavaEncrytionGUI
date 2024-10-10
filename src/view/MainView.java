package view;

import model.Constant;
import model.EncryptionAlgorithm;
import model.EncryptionUtil;
import model.KeyJson;
import model.symmetric.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

public class MainView extends JFrame {

    private JPanel contentPane;


    private List<EncryptionAlgorithm> algorithmList = List.of(new AES(), new DES(), new Blowfish(),
            new PermutationCipher(), new AffineCipher(), new VigenereCipher());
    private EncryptionAlgorithm selectedAlgorithm = algorithmList.get(0);
    private JButton loadKeyBtn;
    private JButton randomKeyBtn;
    private JButton saveKeyBtn;
    private JTextArea inputKey;
    private EncryptionUtil encryptionUtil;
    private JTextArea inputDataText;
    private JTextArea outputDataText;
    //    private String keyLength;
//    private String mode;
//    private String padding;
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


    public MainView() {
        this.inputDataText = new JTextArea();
        this.outputDataText = new JTextArea();
        this.inputKey = new JTextArea();
        this.encryptionUtil = new EncryptionUtil(null, algorithmList.get(0), inputDataText, outputDataText, inputKey);

        this.keyLengthList = new JComboBox(selectedAlgorithm.getKeyLengths());
        this.modeList = new JComboBox(selectedAlgorithm.getModes());
        this.paddingList = new JComboBox(selectedAlgorithm.getPaddings());
//        this.keyLength = selectedAlgorithm.getKeyLengths()[0];
//        this.mode = selectedAlgorithm.getModes()[0];
//        this.padding = selectedAlgorithm.getPaddings()[0];
        showView();
        setVisibility();
        setEnableComponents();
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


        JPanel panel = new SymmetricPanel();
        tabbedPane.addTab("Symmetric", null, panel, null);
        panel.setLayout(null);
        JLabel lblNewLabel = new JLabel("Algorithms");
        lblNewLabel.setBounds(10, 10, 125, 23);
        lblNewLabel.setFont(Constant.titleFont);
        panel.add(lblNewLabel);

        // Create the list of items
        listAlgorithms = new JComboBox(encryptionUtil.getNamesAlgorithm(this.algorithmList));
        listAlgorithms.setSelectedIndex(0);
        listAlgorithms.setBounds(10, 41, 134, 23);
        listAlgorithms.setFont(Constant.font);
        panel.add(listAlgorithms);

        lblKey = new JLabel("Key");
        lblKey.setFont(Constant.titleFont);
        lblKey.setBounds(290, 10, 40, 23);
        panel.add(lblKey);

        JLabel lblChiuDiKey = new JLabel("Key Length");
        lblChiuDiKey.setFont(Constant.titleFont);
        lblChiuDiKey.setBounds(656, 10, 104, 23);
        panel.add(lblChiuDiKey);

        inputKey.setLineWrap(true);
        inputKey.setFont(Constant.font);
        inputKey.setEditable(false);
        jScrollPane3 = new JScrollPane(inputKey);
        jScrollPane3.setBounds(290, 41, 356, 61);
        panel.add(jScrollPane3);

//        keyLengthList = new JComboBox(selectedAlgorithm.getKeyLengths());
        keyLengthList.setSelectedIndex(0);
        keyLengthList.setBounds(656, 40, 68, 29);
        keyLengthList.setFont(Constant.font);
        panel.add(keyLengthList);


        randomKeyBtn = new JButton("Random key");
        randomKeyBtn.setBounds(656, 79, 159, 29);
        randomKeyBtn.setFont(Constant.font);
        panel.add(randomKeyBtn);

        saveKeyBtn = new JButton("Save key");
        saveKeyBtn.setBounds(734, 38, 85, 29);
        saveKeyBtn.setFont(Constant.font);
        panel.add(saveKeyBtn);

        loadKeyBtn = new JButton("Load key");
        loadKeyBtn.setBounds(826, 38, 85, 29);
        loadKeyBtn.setFont(Constant.font);
        panel.add(loadKeyBtn);

        JLabel lblMode = new JLabel("Mode");
        lblMode.setFont(Constant.titleFont);
        lblMode.setBounds(165, 112, 104, 23);
        panel.add(lblMode);

//        modeList = new JComboBox(selectedAlgorithm.getModes());
        modeList.setSelectedIndex(0);
        modeList.setFont(Constant.font);
        modeList.setBounds(165, 139, 77, 29);
        panel.add(modeList);

        JLabel lblPadding = new JLabel("Padding");
        lblPadding.setFont(Constant.titleFont);
        lblPadding.setBounds(290, 112, 104, 23);
        panel.add(lblPadding);

//        paddingList = new JComboBox(selectedAlgorithm.getPaddings());
        paddingList.setSelectedIndex(0);
        paddingList.setFont(Constant.font);
        paddingList.setBounds(290, 139, 104, 29);
        panel.add(paddingList);

        JLabel lblTextHocFile = new JLabel("Text or File");
        lblTextHocFile.setFont(Constant.titleFont);
        lblTextHocFile.setBounds(10, 112, 104, 23);
        panel.add(lblTextHocFile);

        JComboBox selectType = new JComboBox(new String[]{"Text", "File"});
        selectType.setSelectedIndex(0);
        selectType.setFont(Constant.font);
        selectType.setBounds(10, 139, 115, 29);
        panel.add(selectType);


        JButton encryptBtn = new JButton("Encrypt");
        encryptBtn.setBounds(411, 413, 104, 35);
        encryptBtn.setFont(Constant.font);
        panel.add(encryptBtn);

        JButton decryptBtn = new JButton("Decrypt");
        decryptBtn.setBounds(411, 469, 104, 35);
        decryptBtn.setFont(Constant.font);
        panel.add(decryptBtn);

        JLabel lblNiDungGc = new JLabel("Input data");
        lblNiDungGc.setFont(Constant.titleFont);
        lblNiDungGc.setBounds(21, 233, 125, 23);
        panel.add(lblNiDungGc);

        JLabel lblChnThutTon = new JLabel("Output data");
        lblChnThutTon.setFont(Constant.titleFont);
        lblChnThutTon.setBounds(557, 233, 125, 23);
        panel.add(lblChnThutTon);

        inputDataText = new JTextArea();
        inputDataText.setLineWrap(true);
        inputDataText.setFont(Constant.font);

        JScrollPane jScrollPane = new JScrollPane(inputDataText);
        jScrollPane.setBounds(10, 274, 386, 380);
        panel.add(jScrollPane);

        outputDataText = new JTextArea();
        outputDataText.setLineWrap(true);
        outputDataText.setEditable(false);
        outputDataText.setFont(Constant.font);

        JScrollPane jScrollPane2 = new JScrollPane(outputDataText);
        jScrollPane2.setBounds(525, 274, 386, 380);
        panel.add(jScrollPane2);

        lblSA = new JLabel("Number a");
        lblSA.setFont(Constant.titleFont);
        lblSA.setBounds(190, 16, 38, 23);
        panel.add(lblSA);

        lblSB = new JLabel("Number b");
        lblSB.setFont(Constant.titleFont);
        lblSB.setBounds(289, 16, 38, 23);
        panel.add(lblSB);

        numberA = new JTextField();
        numberA.setBounds(173, 45, 68, 25);
        panel.add(numberA);
        numberA.setColumns(10);
        numberA.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();  // Ignore non-digit input
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
                    e.consume();  // Ignore non-digit input
                }
            }
        });
        panel.add(numberB);

        listAlgorithms.addItemListener(event -> {
            int selectedIndex = listAlgorithms.getSelectedIndex();
            encryptionUtil.setSelectedAlgorithm(algorithmList.get(selectedIndex));
            selectedAlgorithm = algorithmList.get(selectedIndex);
            setEnableComponents();
            setVisibility();
        });

        randomKeyBtn.addActionListener(e -> {
            handleRandomKey();
        });

        saveKeyBtn.addActionListener(e -> {
            handleSaveKey();
        });
        loadKeyBtn.addActionListener(e -> {
            handleLoadKey();
        });
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

//		JPanel menuAsym = new AsymmetricPanel();
//		tabbedPane.addTab("Bất đối xứng", null, menuAsym, null);
//
//		JPanel menuHash = new HashPanel();
//		tabbedPane.addTab("Hash", null, menuHash, null);
//
//		JPanel menuSign = new SignPanel();
//		tabbedPane.addTab("Chữ ký điện tử", null, menuSign, null);

    }

    private void handleSaveKey() {
        if (selectedAlgorithm.name().equals(Constant.AFFINE_CIPHER)) {
            if (!selectedAlgorithm.isValidKey(numberA.getText() + "," + numberB.getText())) {
                showInvalidKeyMessage(selectedAlgorithm.getInvalidKeyMessage());
            } else {
                encryptionUtil.handleSaveKey(numberA.getText() + "," + numberB.getText(), null, null);
            }
            encrypt(numberA.getText() + "," + numberB.getText());
        } else if (selectedAlgorithm.name().equals(Constant.VIGENERE_CIPHER)) {
            if (!selectedAlgorithm.isValidKey(inputKey.getText())) {
                showInvalidKeyMessage(selectedAlgorithm.getInvalidKeyMessage());
            } else {
                encryptionUtil.handleSaveKey(inputKey.getText(), null, null);
            }
            encrypt(numberA.getText() + "," + numberB.getText());
        } else if (selectedAlgorithm.name().equals(Constant.PERMUTATION_CIPHER)) {
            if (!selectedAlgorithm.isValidKey(inputKey.getText(), inputDataText.getText().length())) {
                showInvalidKeyMessage(selectedAlgorithm.getInvalidKeyMessage());
            } else {
                encryptionUtil.handleSaveKey(inputKey.getText(), null, null);
            }
        } else {
            if (!selectedAlgorithm.isValidKey(inputKey.getText())) {
                showInvalidKeyMessage(selectedAlgorithm.getInvalidKeyMessage());
            } else {
                encryptionUtil.handleSaveKey(inputKey.getText(), modeList.getSelectedItem().toString(), paddingList.getSelectedItem().toString());
            }
            encrypt(inputKey.getText());
        }

    }

    private void handleRandomKey() {
        String key = null;
        if (selectedAlgorithm.name().equals(Constant.AFFINE_CIPHER)) {
            key = selectedAlgorithm.generateKey();
            numberA.setText(key.split(",")[0]);
            numberB.setText(key.split(",")[1]);
        } else if (selectedAlgorithm.name().equals(Constant.PERMUTATION_CIPHER)) {
            if (inputDataText.getText().isBlank()) {
                showInvalidKeyMessage("Please input data first");
            } else {
                key = selectedAlgorithm.generateKey(inputDataText.getText().length());
                inputKey.setText(key);
            }
        } else {
            key = selectedAlgorithm.generateKey(Integer.parseInt(keyLengthList.getSelectedItem().toString()));
            inputKey.setText(key);

        }
        encrypt(key);
    }

    void handleLoadKey() {
        KeyJson keyJson = encryptionUtil.handleLoadKey();
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
                showInvalidKeyMessage("Cannot find the algorithm with the name: " + keyJson.getAlgorithm() + ". Please check the file again.");
                return;
            } else {
                selectedAlgorithm = alg;
                setEnableComponents();
                listAlgorithms.setSelectedItem(alg.name());
            }

            if (keyJson.getAlgorithm().equals(Constant.AFFINE_CIPHER)) {
                if (!selectedAlgorithm.isValidKey(keyJson.getKey())) {
                    showInvalidKeyMessage("Key is invalid");
                    return;
                }
                String[] keys = keyJson.getKey().split(",");
                numberA.setText(keys[0]);
                numberB.setText(keys[1]);
            } else if (selectedAlgorithm.name().equals(Constant.PERMUTATION_CIPHER)) {
                inputKey.setText(keyJson.getKey());
            } else {
                if (!selectedAlgorithm.isValidKey(keyJson.getKey())) {
                    showInvalidKeyMessage("Key is invalid");
                    return;
                }
                inputKey.setText(keyJson.getKey());
            }

        }
        if (keyJson.getMode() != null && Arrays.asList(selectedAlgorithm.getModes()).contains(keyJson.getMode())) {
            modeList.setSelectedItem(keyJson.getMode());
        }
        if (keyJson.getPadding() != null && Arrays.asList(selectedAlgorithm.getPaddings()).contains(keyJson.getPadding())) {
            paddingList.setSelectedItem(keyJson.getPadding());
        }
        encrypt(inputKey.getText());

    }

    public void encrypt(String key) {
        String inputTxt = inputDataText.getText();
        if (inputTxt.isEmpty()) {
            return;
        }

        if (selectedAlgorithm.name().equals(Constant.PERMUTATION_CIPHER)) {
            if (!selectedAlgorithm.isValidKey(key, inputTxt.length())) {
                showInvalidKeyMessage(selectedAlgorithm.getInvalidKeyMessage());
                return;
            }
        } else if (!selectedAlgorithm.isValidKey(key)) {
            showInvalidKeyMessage(selectedAlgorithm.getInvalidKeyMessage());
            return;
        }
        String result = "";
        if (selectedAlgorithm.name().equals(Constant.AFFINE_CIPHER)) {
            result = selectedAlgorithm.encrypt(inputTxt, numberA.getText() + "," + numberB.getText());
        } else if (selectedAlgorithm.name().equals(Constant.AES_CIPHER) ||
                selectedAlgorithm.name().equals(Constant.DES_CIPHER) ||
                selectedAlgorithm.name().equals(Constant.BLOWFISH_CIPHER)) {
            result = selectedAlgorithm.encrypt(inputTxt, key, Integer.parseInt(keyLengthList.getSelectedItem().toString()),
                    modeList.getSelectedItem().toString(), paddingList.getSelectedItem().toString());
        } else {
            result = selectedAlgorithm.encrypt(inputTxt, key);
        }
        outputDataText.setText(result);

    }

    public void decrypt(String key) {
        String inputTxt = inputDataText.getText();
        if (inputTxt.isEmpty()) {
            return;
        }
        if (selectedAlgorithm.name().equals(Constant.PERMUTATION_CIPHER)) {
            if (!selectedAlgorithm.isValidKey(key, inputTxt.length())) {
                showInvalidKeyMessage(selectedAlgorithm.getInvalidKeyMessage());
                return;
            }
        } else if (!selectedAlgorithm.isValidKey(key)) {
            showInvalidKeyMessage(selectedAlgorithm.getInvalidKeyMessage());
            return;
        }
        String result = "";
        if (selectedAlgorithm.name().equals(Constant.AFFINE_CIPHER)) {
            result = selectedAlgorithm.decrypt(inputTxt, numberA.getText() + "," + numberB.getText());
        } else if (selectedAlgorithm.name().equals(Constant.AES_CIPHER) ||
                selectedAlgorithm.name().equals(Constant.DES_CIPHER) ||
                selectedAlgorithm.name().equals(Constant.BLOWFISH_CIPHER)) {
            result = selectedAlgorithm.decrypt(inputTxt, key, Integer.parseInt(keyLengthList.getSelectedItem().toString()),
                    modeList.getSelectedItem().toString(), paddingList.getSelectedItem().toString());
        } else {
            result = selectedAlgorithm.decrypt(inputTxt, key);
        }
        outputDataText.setText(result);
    }

    private void showInvalidKeyMessage(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE);
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
    }

    void showVigenereCipherComponents() {
        keyLengthList.setEnabled(true);
        jScrollPane3.setVisible(true);
        lblKey.setVisible(true);
    }

    void showPermutationCipherComponents() {
        jScrollPane3.setVisible(true);
        lblKey.setVisible(true);
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

        // Update the models of the JComboBoxes
        setComboBoxModel(keyLengthList, selectedAlgorithm.getKeyLengths());
        setComboBoxModel(modeList, selectedAlgorithm.getModes());
        setComboBoxModel(paddingList, selectedAlgorithm.getPaddings());
    }

    void setComboBoxModel(JComboBox comboBox, String[] items) {
        try {
            comboBox.setModel(new DefaultComboBoxModel<>(items));
        } catch (UnsupportedOperationException e) {
            // UnsupportedOperationException is ignored because...
        }
    }
}
