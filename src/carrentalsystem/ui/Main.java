package carrentalsystem.ui;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // set Nimbus look and feel if available
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) { }

        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
