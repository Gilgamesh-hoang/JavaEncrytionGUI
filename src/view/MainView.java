package view;

import model.Constant;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MainView extends JFrame {

    private JPanel contentPane;

    public MainView() {
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

        JPanel basicPanel = new BasicPanel();
        tabbedPane.addTab("Basic", null, basicPanel, null);

        JPanel symmetricPanel = new SymmetricPanel();
        tabbedPane.addTab("Symmetric", null, symmetricPanel, null);

        JPanel asymmPanel = new AsymmetricPanel();
        tabbedPane.addTab("Asymmetric", null, asymmPanel, null);

		JPanel digitalSignaturePanel = new DigitalSignaturePanel();
		tabbedPane.addTab("Digital Signature", null, digitalSignaturePanel, null);

		JPanel menuHash = new HashPanel();
		tabbedPane.addTab("Hash", null, menuHash, null);

    }
}
