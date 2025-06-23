import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

public class LoginFrame extends JFrame {
    private final JTextField user = new JTextField(20);
    private final JPasswordField pass = new JPasswordField(20);
    private int attempts = 0;

    public LoginFrame() {
        setTitle("Secure Voting Login");
        setSize(420, 520);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setIconImage(new ImageIcon("src/images/icon.png").getImage());

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBorder(new EmptyBorder(30, 40, 30, 40));
        wrapper.setBackground(new Color(250, 250, 250));

        ImageIcon rawIcon = new ImageIcon("src/images/vote.png");
        Image scaled = rawIcon.getImage().getScaledInstance(110, 110, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaled));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        wrapper.add(logoLabel);

        wrapper.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel header = new JLabel("Login to Continue");
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setForeground(new Color(40, 40, 60));
        wrapper.add(header);

        wrapper.add(Box.createRigidArea(new Dimension(0, 30)));
        wrapper.add(buildField("Username", user));
        wrapper.add(Box.createRigidArea(new Dimension(0, 20)));
        wrapper.add(buildField("Password", pass));
        wrapper.add(Box.createRigidArea(new Dimension(0, 30)));

        JButton loginButton = createButton("Login", new Color(41, 128, 185));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(e -> {
            if (!handleAdmin()) handleVoter();
        });
        wrapper.add(loginButton);

        add(wrapper);
        setVisible(true);
        user.requestFocus();
    }

    private JPanel buildField(String labelText, JTextField field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(250, 250, 250));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(60, 60, 60));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(field);
        return panel;
    }

    private JButton createButton(String text, Color baseColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(baseColor);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(200, 40));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(baseColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(baseColor);
            }
        });
        return btn;
    }

    private boolean handleAdmin() {
        if (user.getText().equals(DataStore.ADMIN_USERNAME)
                && new String(pass.getPassword()).equals(DataStore.ADMIN_PASSWORD)) {
            dispose();
            new AdminPanel();
            return true;
        }
        return false;
    }

    private void handleVoter() {
        String uname = user.getText().trim();
        String pwd = SecurityUtils.hashPassword(new String(pass.getPassword()));
        Map<String, String> voters = DataStore.loadVoters();

        if (voters.containsKey(uname) && voters.get(uname).equals(pwd)) {
            dispose();
            new VotingPanel(uname);
        } else {
            attempts++;
            JOptionPane.showMessageDialog(this, "Invalid credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            if (attempts >= 3) {
                JOptionPane.showMessageDialog(this, "Too many failed attempts. Exiting.");
                System.exit(0);
            }
        }
    }
}
