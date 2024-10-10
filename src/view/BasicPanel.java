package view;

import model.Constant;
import model.EncryptionAlgorithm;
import model.EncryptionUtil;
import model.KeyJson;
import model.basic.Base64;
import model.basic.Caesar;
import model.basic.XOR;

import javax.swing.*;
import java.util.List;

public class BasicPanel extends JPanel {
    private final List<EncryptionAlgorithm> algorithmList;
    private JButton loadKeyBtn;
    private JButton randomKeyBtn;
    private JButton saveKeyBtn;
    private JTextArea inputKey;
    private EncryptionUtil encryptionUtil;
    private JTextArea inputDataText;
    private JTextArea outputDataText;

    public BasicPanel() {
        this.algorithmList = List.of(new XOR(), new Base64(), new Caesar());
        this.inputDataText = new JTextArea();
        this.outputDataText = new JTextArea();
        this.inputKey = new JTextArea();
        this.encryptionUtil = new EncryptionUtil(this, algorithmList.get(0), inputDataText, outputDataText, inputKey);
        showView();
        setEnableComponents();
    }

    public void showView() {

        this.setLayout(null);
        JLabel lblNewLabel = new JLabel("Algorithms");
        lblNewLabel.setBounds(10, 10, 125, 23);
        lblNewLabel.setFont(Constant.titleFont);
        this.add(lblNewLabel);

        JComboBox listAlgorithms = new JComboBox(EncryptionUtil.getNamesAlgorithm(algorithmList));
        listAlgorithms.setSelectedIndex(0);
        listAlgorithms.setBounds(10, 41, 125, 23);
        listAlgorithms.setFont(Constant.font);
        this.add(listAlgorithms);

        JLabel lblKey = new JLabel("Key");
        lblKey.setFont(Constant.titleFont);
        lblKey.setBounds(165, 10, 40, 23);
        this.add(lblKey);


        inputKey.setLineWrap(true);
        inputKey.setFont(Constant.font);


        JScrollPane jScrollPane3 = new JScrollPane(inputKey);
        jScrollPane3.setBounds(165, 40, 358, 53);
        this.add(jScrollPane3);

        randomKeyBtn = new JButton("Random key");
        randomKeyBtn.setBounds(557, 38, 180, 29);
        randomKeyBtn.setFont(Constant.font);
        this.add(randomKeyBtn);

        saveKeyBtn = new JButton("Save key");
        saveKeyBtn.setBounds(557, 77, 85, 29);
        saveKeyBtn.setFont(Constant.font);
        this.add(saveKeyBtn);

        loadKeyBtn = new JButton("Load key");
        loadKeyBtn.setBounds(652, 77, 85, 29);
        loadKeyBtn.setFont(Constant.font);
        this.add(loadKeyBtn);


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

        inputDataText.setLineWrap(true);
        inputDataText.setFont(Constant.font);

        JScrollPane jScrollPane = new JScrollPane(inputDataText);
        jScrollPane.setBounds(10, 274, 386, 380);
        this.add(jScrollPane);

        outputDataText.setLineWrap(true);
        outputDataText.setEditable(false);
        outputDataText.setFont(Constant.font);

        JScrollPane jScrollPane2 = new JScrollPane(outputDataText);
        jScrollPane2.setBounds(525, 274, 386, 380);
        this.add(jScrollPane2);

        listAlgorithms.addItemListener(event -> {
            int selectedIndex = listAlgorithms.getSelectedIndex();
            encryptionUtil.setSelectedAlgorithm(algorithmList.get(selectedIndex));
            setEnableComponents();
        });

        randomKeyBtn.addActionListener(e -> {
            inputKey.setText(encryptionUtil.getSelectedAlgorithm().generateKey());
            encryptionUtil.encrypt(inputKey.getText());
        });

        saveKeyBtn.addActionListener(e -> {
            String key = inputKey.getText();
            if (encryptionUtil.invalidKey()) {
                JOptionPane.showMessageDialog(this,
                        encryptionUtil.getSelectedAlgorithm().getInvalidKeyMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                encryptionUtil.handleSaveKey(key, null, null);
            }
        });
        loadKeyBtn.addActionListener(e -> {
            KeyJson keyJson = encryptionUtil.handleLoadKey();
            if (keyJson == null) {
                return;
            }

            if (keyJson.getAlgorithm() != null) {
                algorithmList.stream().filter(algorithm -> algorithm.name().equals(keyJson.getAlgorithm()))
                        .findFirst().ifPresent(encryptionAlgorithm -> {
                            encryptionUtil.setSelectedAlgorithm(encryptionAlgorithm);
                            setEnableComponents();
                            listAlgorithms.setSelectedItem(encryptionAlgorithm.name());
                        });
            }
            inputKey.setText(keyJson.getKey());
            encryptionUtil.encrypt(inputKey.getText());
        });
        encryptBtn.addActionListener(e -> encryptionUtil.encrypt(inputKey.getText()));
        decryptBtn.addActionListener(e -> encryptionUtil.decrypt(inputKey.getText()));
    }

    void setEnableComponents() {
        loadKeyBtn.setEnabled(encryptionUtil.getSelectedAlgorithm().requireKey());
        saveKeyBtn.setEnabled(encryptionUtil.getSelectedAlgorithm().requireKey());
        randomKeyBtn.setEnabled(encryptionUtil.getSelectedAlgorithm().requireKey());
        inputKey.setEnabled(encryptionUtil.getSelectedAlgorithm().requireKey());
        inputKey.setText("");
    }
}
