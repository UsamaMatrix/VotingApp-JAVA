import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

public class VotingPanel extends JFrame {
    private final String username;
    private final List<String[]> candidates;

    public VotingPanel(String user) {
        this.username = user;
        this.candidates = DataStore.loadCandidates();
        List<String> voted = DataStore.loadVotedUsers();

        setTitle("Cast Your Vote");
        setIconImage(new ImageIcon("src/images/icon.png").getImage());
        setSize(620, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 30, 30, 30));

        JLabel title = new JLabel(
                voted.contains(username)
                        ? "⚠️ You have already voted."
                        : "🗳️ Select Your Candidate",
                SwingConstants.CENTER
        );
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(voted.contains(username)
                ? new Color(192, 57, 43)
                : new Color(33, 47, 61));
        mainPanel.add(title);
        mainPanel.add(Box.createVerticalStrut(12));

        if (!voted.contains(username)) {
            JLabel iconLabel = new JLabel(new ImageIcon(
                    new ImageIcon("src/images/vote.png").getImage()
                            .getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
            iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            mainPanel.add(iconLabel);
            mainPanel.add(Box.createVerticalStrut(18));
        }

        if (!voted.contains(username)) {
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
            buttonPanel.setBackground(Color.WHITE);

            for (int i = 0; i < candidates.size(); i++) {
                JPanel card = createCandidateCard(candidates.get(i), i);
                buttonPanel.add(card);
                buttonPanel.add(Box.createVerticalStrut(15));
            }

            JScrollPane scrollPane = new JScrollPane(buttonPanel);
            scrollPane.setBorder(null);
            scrollPane.setPreferredSize(new Dimension(540, 480));
            scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
            scrollPane.getVerticalScrollBar().setUnitIncrement(10);

            mainPanel.add(scrollPane);
        }

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createCandidateCard(String[] candidate, int index) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(520, 100));

        // Load candidate image safely
        String imagePath = "src/images/" + candidate[2].toLowerCase() + ".png";
        ImageIcon scaledIcon = new ImageIcon(
                new ImageIcon(new File(imagePath).exists() ? imagePath : "src/images/vote.png")
                        .getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));
        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setBorder(new EmptyBorder(10, 12, 10, 0));

        // Name + Vote Count
        JLabel nameLabel = new JLabel("<html><b>" + candidate[0] + "</b><br>Votes: " + candidate[1] + "</html>");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameLabel.setForeground(new Color(40, 55, 71));

        // Vote button
        JButton voteButton = new JButton("Vote");
        voteButton.setBackground(new Color(52, 152, 219));
        voteButton.setForeground(Color.WHITE);
        voteButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        voteButton.setFocusPainted(false);
        voteButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        voteButton.setPreferredSize(new Dimension(90, 40));

        voteButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                voteButton.setBackground(new Color(41, 128, 185));
            }

            public void mouseExited(MouseEvent e) {
                voteButton.setBackground(new Color(52, 152, 219));
            }
        });

        voteButton.addActionListener(e -> castVote(index));

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setOpaque(false);
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        infoPanel.add(nameLabel, BorderLayout.CENTER);
        infoPanel.add(voteButton, BorderLayout.EAST);

        card.add(imageLabel, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);
        return card;
    }

    private void castVote(int index) {
        String[] c = candidates.get(index);
        try {
            int voteCount = Integer.parseInt(c[1].trim());
            c[1] = String.valueOf(voteCount + 1);
            DataStore.saveCandidates(candidates);
            DataStore.markUserVoted(username);
            JOptionPane.showMessageDialog(this, "✅ Vote cast successfully.");
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "❌ Invalid vote count for candidate: " + c[0],
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
