import view.MainView;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
            try {
                MainView frame = new MainView();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
