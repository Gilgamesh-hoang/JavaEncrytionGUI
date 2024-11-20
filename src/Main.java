import view.MainView;

import java.awt.EventQueue;


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
//jlink --module-path $JAVA_HOME/jmods  --add-modules java.base,java.desktop,java.security.sasl,java.security.jgss,java.naming,java.xml.crypto --output E:\workspace\ATTT\out\artifacts\ATTT_jar\custom-jre2 --strip-debug --no-header-files --no-man-pages --compress=2