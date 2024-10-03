package view;

import model.Constant;
import model.EncryptionAlgorithm;
import model.EncryptionUtil;
import model.symmetric.AES;
import model.symmetric.Blowfish;
import model.symmetric.DES;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.List;

public class MainView extends JFrame {

    private JPanel contentPane;


    private List<EncryptionAlgorithm> algorithmList = List.of(new AES(), new DES(), new Blowfish());
    private EncryptionAlgorithm selectedAlgorithm = algorithmList.get(0);
    private JButton loadKeyBtn;
    private JButton randomKeyBtn;
    private JButton saveKeyBtn;
    private JTextArea inputKey;
    private EncryptionUtil encryptionUtil;
    private JTextArea inputDataText;
    private JTextArea outputDataText;


    public MainView() {
        this.inputDataText = new JTextArea();
        this.outputDataText = new JTextArea();
        this.inputKey = new JTextArea();
        this.encryptionUtil = new EncryptionUtil(null, algorithmList.get(0), inputDataText, outputDataText, inputKey);
        showView();
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
//        tabbedPane.addTab("Cơ bản", null, basicPanel, null);


        JPanel panel = new SymmetricPanel();
        tabbedPane.addTab("Đối xứng", null, panel, null);
        panel.setLayout(null);
        JLabel lblNewLabel = new JLabel("Chọn thuật toán");
        lblNewLabel.setBounds(10, 10, 125, 23);
        lblNewLabel.setFont(Constant.titleFont);
        panel.add(lblNewLabel);

        // Create the list of items
        JComboBox listAlgorithms = new JComboBox(encryptionUtil.getNamesAlgorithm(this.algorithmList));
        listAlgorithms.setSelectedIndex(0);
        listAlgorithms.setBounds(10, 41, 238, 23);
        listAlgorithms.setFont(Constant.font);
        panel.add(listAlgorithms);

        JLabel lblKey = new JLabel("Key");
        lblKey.setFont(Constant.titleFont);
        lblKey.setBounds(290, 10, 40, 23);
        panel.add(lblKey);

        JLabel lblChiuDiKey = new JLabel("Chiều dài key");
        lblChiuDiKey.setFont(Constant.titleFont);
        lblChiuDiKey.setBounds(656, 10, 104, 23);
        panel.add(lblChiuDiKey);

        JComboBox keyLengthList = new JComboBox(selectedAlgorithm.getKeyLengths());
        keyLengthList.setSelectedIndex(0);
        keyLengthList.setBounds(656, 40, 68, 29);
        keyLengthList.setFont(Constant.font);
        panel.add(keyLengthList);

        inputKey.setLineWrap(true);
        inputKey.setFont(Constant.font);
        JScrollPane jScrollPane3 = new JScrollPane(inputKey);
        jScrollPane3.setBounds(290, 41, 356, 61);
        panel.add(jScrollPane3);

        randomKeyBtn = new JButton("Tạo key ngẫu nhiên");
        randomKeyBtn.setBounds(656, 79, 159, 29);
        randomKeyBtn.setFont(Constant.font);
        panel.add(randomKeyBtn);

        saveKeyBtn = new JButton("Lưu key");
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

        JComboBox modeList = new JComboBox(selectedAlgorithm.getModes());
        modeList.setSelectedIndex(0);
        modeList.setFont(Constant.font);
        modeList.setBounds(165, 139, 77, 29);
        panel.add(modeList);

        JLabel lblPadding = new JLabel("Padding");
        lblPadding.setFont(Constant.titleFont);
        lblPadding.setBounds(290, 112, 104, 23);
        panel.add(lblPadding);

        JComboBox paddingList = new JComboBox(selectedAlgorithm.getPaddings());
        paddingList.setSelectedIndex(0);
        paddingList.setFont(Constant.font);
        paddingList.setBounds(290, 139, 104, 29);
        panel.add(paddingList);

        JLabel lblTextHocFile = new JLabel("Text hoặc File");
        lblTextHocFile.setFont(Constant.titleFont);
        lblTextHocFile.setBounds(10, 112, 104, 23);
        panel.add(lblTextHocFile);

        JComboBox selectType = new JComboBox(new String[]{"Text", "File"});
        selectType.setSelectedIndex(0);
        selectType.setFont(Constant.font);
        selectType.setBounds(10, 139, 115, 29);
        panel.add(selectType);


        JButton encryptBtn = new JButton("Mã hóa");
        encryptBtn.setBounds(411, 413, 104, 35);
        encryptBtn.setFont(Constant.font);
        panel.add(encryptBtn);

        JButton decryptBtn = new JButton("Giải mã");
        decryptBtn.setBounds(411, 469, 104, 35);
        decryptBtn.setFont(Constant.font);
        panel.add(decryptBtn);

        JLabel lblNiDungGc = new JLabel("Nội dung gốc");
        lblNiDungGc.setFont(Constant.titleFont);
        lblNiDungGc.setBounds(21, 233, 125, 23);
        panel.add(lblNiDungGc);

        JLabel lblChnThutTon = new JLabel("Kết quả");
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
        


        listAlgorithms.addItemListener(event -> {
            int selectedIndex = listAlgorithms.getSelectedIndex();
            encryptionUtil.setSelectedAlgorithm(algorithmList.get(selectedIndex));
            setEnableComponents();
        });

        randomKeyBtn.addActionListener(e -> {
        });

        saveKeyBtn.addActionListener(e -> {
//            if (encryptionUtil.invalidKey()) {
//                JOptionPane.showMessageDialog(this,
//                        encryptionUtil.getSelectedAlgorithm().getInvalidKeyMessage(),
//                        "Lỗi",
//                        JOptionPane.ERROR_MESSAGE);
//            } else {
//                encryptionUtil.handleSaveKey(key);
//            }
        });
        loadKeyBtn.addActionListener(e -> encryptionUtil.handleLoadKey());


//		JPanel menuAsym = new AsymmetricPanel();
//		tabbedPane.addTab("Bất đối xứng", null, menuAsym, null);
//
//		JPanel menuHash = new HashPanel();
//		tabbedPane.addTab("Hash", null, menuHash, null);
//
//		JPanel menuSign = new SignPanel();
//		tabbedPane.addTab("Chữ ký điện tử", null, menuSign, null);

    }

    private void handleLoadKey() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file chứa key");

        int userSelection = fileChooser.showOpenDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            BufferedReader reader = null;
            try {
                String path = fileToLoad.getAbsolutePath();

                // Ensure the file has a .txt extension
                if (!path.endsWith(".txt")) {
                    JOptionPane.showMessageDialog(this, "File không hợp lệ! File phải có định dạng .txt", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                reader = new BufferedReader(new FileReader(fileToLoad));
                StringBuilder key = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    key.append(line);
                }
                if (!selectedAlgorithm.isValidKey(key.toString())) {
                    JOptionPane.showMessageDialog(this,
                            selectedAlgorithm.getInvalidKeyMessage(),
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                inputKey.setText(key.toString());

            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

    private void handleSaveKey(String key) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu key");

        // Set default file name and filter for .txt files
        String fileName = String.format("%s_key_%s.txt", selectedAlgorithm.name().toLowerCase(), System.currentTimeMillis());
        fileChooser.setSelectedFile(new File(fileName)); // Default file name
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String path = fileToSave.getAbsolutePath();

            // Ensure the file has a .txt extension
            if (!path.endsWith(".txt")) {
                path += ".txt";
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
                writer.write(key);
                JOptionPane.showMessageDialog(this,
                        "Key đã được lưu!",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    void setEnableComponents() {
//        loadKeyBtn.setEnabled(selectedAlgorithm.requireKey());
//        saveKeyBtn.setEnabled(selectedAlgorithm.requireKey());
//        randomKeyBtn.setEnabled(selectedAlgorithm.requireKey());
//        inputKey.setEnabled(selectedAlgorithm.requireKey());
//        inputKey.setText("");
    }
}
