import javax.swing.*;

public class VotingSystemApp {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
