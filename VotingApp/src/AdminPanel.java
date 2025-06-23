// AdminPanel.java - Updated with Export Buttons
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Date;
import java.text.SimpleDateFormat;

public class AdminPanel extends JFrame {
    private final List<String[]> candidates = DataStore.loadCandidates();
    private final Map<String, String> voters = DataStore.loadVoters();
    private final List<String> votedUsers = DataStore.loadVotedUsers();
    private JPanel contentPanel;
    private final Map<String, JLabel> navLabels = new HashMap<>();

    public AdminPanel() {
        setTitle("Admin Dashboard - Voting System");
        setIconImage(new ImageIcon("src/images/icon.png").getImage());
        setSize(1100, 650);
        setMinimumSize(new Dimension(900, 550));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildHeader(), BorderLayout.NORTH);
        add(buildSidebar(), BorderLayout.WEST);
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(245, 245, 245));
        add(contentPanel, BorderLayout.CENTER);

        showDashboard();
        setVisible(true);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
        header.setBackground(new Color(23, 33, 53));
        header.setBorder(new EmptyBorder(10, 25, 10, 10));

        JLabel icon = new JLabel(resizedIcon("src/images/dashboard.png", 28, 28));
        JLabel title = new JLabel(" Admin Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);

        header.add(icon);
        header.add(title);
        return header;
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(32, 38, 50));
        sidebar.setPreferredSize(new Dimension(210, getHeight()));
        sidebar.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel(" ADMIN PANEL");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setBorder(new EmptyBorder(15, 5, 25, 5));
        sidebar.add(title);

        String[][] items = {
                {"Dashboard", "dashboard.png"},
                {"Candidates", "candidates.png"},
                {"Voters", "voters.png"},
                {"Results", "results.png"},
                {"Logout", "settings.png"}
        };

        for (String[] item : items) {
            JLabel label = new JLabel(" " + item[0], resizedIcon("src/images/" + item[1], 20, 20), JLabel.LEFT);
            label.setOpaque(true);
            label.setMaximumSize(new Dimension(Short.MAX_VALUE, 42));
            label.setBackground(new Color(32, 38, 50));
            label.setForeground(Color.LIGHT_GRAY);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            label.setBorder(new EmptyBorder(10, 10, 10, 10));
            label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            label.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    highlightActiveTab(label);
                    switch (item[0]) {
                        case "Dashboard" -> showDashboard();
                        case "Candidates" -> showTablePanel("Candidates", candidates);
                        case "Voters" -> showTablePanel("Voters", new ArrayList<>(voters.keySet()));
                        case "Results" -> showTablePanel("Voted Users", votedUsers);
                        case "Logout" -> logout();
                    }
                }
                public void mouseEntered(MouseEvent e) { label.setForeground(Color.WHITE); }
                public void mouseExited(MouseEvent e) {
                    if (!label.getBackground().equals(new Color(45, 51, 63))) {
                        label.setForeground(Color.LIGHT_GRAY);
                    }
                }
            });
            navLabels.put(item[0], label);
            sidebar.add(label);
        }
        return sidebar;
    }

    private void highlightActiveTab(JLabel activeLabel) {
        for (JLabel label : navLabels.values()) {
            label.setBackground(new Color(32, 38, 50));
            label.setForeground(Color.LIGHT_GRAY);
        }
        activeLabel.setBackground(new Color(45, 51, 63));
        activeLabel.setForeground(Color.WHITE);
    }

    private void logout() {
        dispose();
        new LoginFrame();
    }

    private void showDashboard() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(buildSummaryCards(), BorderLayout.NORTH);
        contentPanel.add(buildTablesPanel(), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel buildSummaryCards() {
        JPanel summary = new JPanel(new GridLayout(1, 3, 20, 20));
        summary.setBorder(new EmptyBorder(20, 20, 10, 20));
        summary.setBackground(new Color(245, 245, 245));

        summary.add(createCard("Candidates", candidates.size(), new Color(41, 128, 185)));
        summary.add(createCard("Voters", voters.size(), new Color(39, 174, 96)));
        summary.add(createCard("Voted", votedUsers.size(), new Color(243, 156, 18)));

        return summary;
    }

    private JPanel createCard(String title, int value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        card.setPreferredSize(new Dimension(140, 90));

        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JLabel valueLabel = new JLabel(String.valueOf(value), JLabel.CENTER);
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildTablesPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 15, 15));
        panel.setBorder(new EmptyBorder(10, 20, 20, 20));
        panel.setBackground(new Color(245, 245, 245));

        List<String> notVoted = new ArrayList<>(voters.keySet());
        notVoted.removeAll(votedUsers);

        JTable votedTable = createTable(votedUsers, "Voted Users");
        JTable notVotedTable = createTable(notVoted, "Not Voted Users");

        panel.add(new JScrollPane(votedTable));
        panel.add(new JScrollPane(notVotedTable));

        return panel;
    }

    private JTable createTable(List<?> users, String columnTitle) {
        String[][] data = users.stream().map(u -> new String[]{u.toString()}).toArray(String[][]::new);
        String[] columns = {columnTitle};

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(25);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        return table;
    }

    private void showTablePanel(String title, List<?> items) {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        JLabel header = new JLabel(title);
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setBorder(new EmptyBorder(20, 25, 10, 0));
        contentPanel.add(header, BorderLayout.NORTH);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(new EmptyBorder(10, 20, 20, 20));
        wrapper.setBackground(new Color(245, 245, 245));

        JTable table;
        if (title.equals("Candidates")) {
            List<String> formatted = new ArrayList<>();
            for (String[] c : candidates) {
                formatted.add(c[0] + " (" + c[2] + ") - " + c[1] + " votes");
            }
            table = createTable(formatted, title);
        } else {
            table = createTable(items, title);
        }

        wrapper.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton addBtn = new JButton("+ Add New");
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addBtn.setForeground(Color.WHITE);
        addBtn.setBackground(new Color(52, 152, 219));
        addBtn.setFocusPainted(false);
        addBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addBtn.addActionListener(e -> handleAddButton(title));

        JButton exportBtn = new JButton("⬇ Export CSV");
        exportBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        exportBtn.setForeground(Color.WHITE);
        exportBtn.setBackground(new Color(39, 174, 96));
        exportBtn.setFocusPainted(false);
        exportBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        exportBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exportBtn.addActionListener(e -> exportToCSV(title));

        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topRight.setOpaque(false);
        topRight.add(addBtn);
        topRight.add(Box.createHorizontalStrut(10));
        topRight.add(exportBtn);
        wrapper.add(topRight, BorderLayout.NORTH);

        contentPanel.add(wrapper, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void exportToCSV(String context) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(context.toLowerCase().replace(" ", "") + ".csv"))) {
            switch (context) {
                case "Candidates" -> {
                    pw.println("Name,Party,Symbol");
                    for (String[] c : candidates) pw.println(String.join(",", c));
                }
                case "Voters" -> {
                    pw.println("Username,PasswordHash");
                    for (Map.Entry<String, String> v : voters.entrySet())
                        pw.println(v.getKey() + "," + v.getValue());
                }
                case "Voted Users" -> {
                    pw.println("Voter Username");
                    for (String user : votedUsers)
                        pw.println(user);
                }
            }
            JOptionPane.showMessageDialog(this, "✅ Exported " + context + " to CSV successfully.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "❌ Failed to export " + context + ": " + ex.getMessage());
        }
    }

    private void handleAddButton(String context) {
        String input1 = JOptionPane.showInputDialog(this, "Enter name:");
        if (input1 == null || input1.trim().isEmpty()) return;

        switch (context) {
            case "Candidates" -> {
                String party = JOptionPane.showInputDialog(this, "Enter party:");
                String symbol = JOptionPane.showInputDialog(this, "Enter symbol:");
                candidates.add(new String[]{input1, party, symbol});
                DataStore.saveCandidates(candidates);
                showTablePanel("Candidates", candidates);
            }
            case "Voters" -> {
                String password = JOptionPane.showInputDialog(this, "Enter password:");
                voters.put(input1, SecurityUtils.hashPassword(password));
                DataStore.saveVoters(voters);
                showTablePanel("Voters", new ArrayList<>(voters.keySet()));
            }
            case "Voted Users" -> {
                votedUsers.add(input1);
                DataStore.saveVotedUsers(votedUsers);
                showTablePanel("Voted Users", votedUsers);
            }
        }
    }

    private Icon resizedIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }
}
