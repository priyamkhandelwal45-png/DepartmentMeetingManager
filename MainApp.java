import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainApp {
    private static JFrame mainFrame;
    private static CardLayout cardLayout;
    private static JPanel cardPanel;
    private static DefaultTableModel tableModel;
    private static JTable meetingTable;
    private static JMenuBar menuBar;

    private static String currentUsername = "admin";
    private static String currentPassword = "admin123";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            mainFrame = new JFrame("Department Meeting Schedule Manager");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setSize(750, 520);
            mainFrame.setLocationRelativeTo(null);

            menuBar = createMenuBar();
            mainFrame.setJMenuBar(menuBar);
            menuBar.setVisible(false);

            cardLayout = new CardLayout();
            cardPanel = new JPanel(cardLayout);

            cardPanel.add(createLoginPanel(), "START");
            cardPanel.add(createDashboardPanel(), "DASHBOARD");
            cardPanel.add(createScheduleMeetingPanel(), "SCHEDULE");
            cardPanel.add(createViewMeetingPanel(), "VIEW");
            cardPanel.add(createChangePasswordPanel(), "PASSWORD");

            mainFrame.add(cardPanel);
            cardLayout.show(cardPanel, "START");
            mainFrame.setVisible(true);
        });
    }

    private static JMenuBar createMenuBar() {
        JMenuBar mb = new JMenuBar();

        JMenu navigationMenu = new JMenu("Navigate");
        JMenuItem dashItem = new JMenuItem("Dashboard");
        JMenuItem scheduleItem = new JMenuItem("Schedule Meeting");
        JMenuItem viewItem = new JMenuItem("View Meetings");
        JMenuItem passItem = new JMenuItem("Change Password");
        JMenuItem logoutItem = new JMenuItem("Logout");

        dashItem.addActionListener(e -> cardLayout.show(cardPanel, "DASHBOARD"));
        scheduleItem.addActionListener(e -> cardLayout.show(cardPanel, "SCHEDULE"));
        viewItem.addActionListener(e -> {
            refreshTableData();
            cardLayout.show(cardPanel, "VIEW");
        });
        passItem.addActionListener(e -> cardLayout.show(cardPanel, "PASSWORD"));
        logoutItem.addActionListener(e -> {
            menuBar.setVisible(false);
            cardLayout.show(cardPanel, "LOGIN");
        });

        navigationMenu.add(dashItem);
        navigationMenu.add(scheduleItem);
        navigationMenu.add(viewItem);
        navigationMenu.addSeparator();
        navigationMenu.add(passItem);
        navigationMenu.add(logoutItem);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About System");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(mainFrame,
                "Department Meeting Schedule Manager v1.0\nDeveloped with Java Swing & MySQL",
                "About", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);

        mb.add(navigationMenu);
        mb.add(helpMenu);
        return mb;
    }

    private static JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 247, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel heading = new JLabel("Meeting Manager Login", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(15);
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(15);

        JButton loginBtn = new JButton("STARTad");
        loginBtn.setBackground(new Color(41, 128, 185));
        loginBtn.setForeground(Color.WHITE);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(heading, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; panel.add(userLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(passLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(passField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(loginBtn, gbc);

        loginBtn.addActionListener(e -> {
            String user = userField.getText().trim();
            String pass = new String(passField.getPassword()).trim();

            if (user.equals(currentUsername) && pass.equals(currentPassword)) {
                userField.setText("");
                passField.setText("");
                menuBar.setVisible(true);
                cardLayout.show(cardPanel, "DASHBOARD");
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Invalid Username or Password!", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private static JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Department Meeting Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        panel.add(title, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new GridLayout(4, 1, 12, 12));
        JButton scheduleBtn = new JButton("Schedule New Meeting");
        JButton viewBtn = new JButton("View / Manage Meetings");
        JButton passBtn = new JButton("Change Password");
        JButton logoutBtn = new JButton("Logout");

        scheduleBtn.setFont(new Font("Arial", Font.PLAIN, 15));
        viewBtn.setFont(new Font("Arial", Font.PLAIN, 15));
        passBtn.setFont(new Font("Arial", Font.PLAIN, 15));
        logoutBtn.setFont(new Font("Arial", Font.PLAIN, 15));

        btnPanel.add(scheduleBtn);
        btnPanel.add(viewBtn);
        btnPanel.add(passBtn);
        btnPanel.add(logoutBtn);

        JPanel centerContainer = new JPanel(new GridBagLayout());
        centerContainer.add(btnPanel);
        panel.add(centerContainer, BorderLayout.CENTER);

        scheduleBtn.addActionListener(e -> cardLayout.show(cardPanel, "SCHEDULE"));
        viewBtn.addActionListener(e -> {
            refreshTableData();
            cardLayout.show(cardPanel, "VIEW");
        });
        passBtn.addActionListener(e -> cardLayout.show(cardPanel, "PASSWORD"));
        logoutBtn.addActionListener(e -> {
            menuBar.setVisible(false);
            cardLayout.show(cardPanel, "LOGIN");
        });

        return panel;
    }

    private static JPanel createScheduleMeetingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(7, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 80, 20, 80));

        JTextField idField = new JTextField();
        JTextField titleField = new JTextField();
        JComboBox<String> deptBox = new JComboBox<>(new String[]{"Computer Science", "Information Technology", "Mechanical", "Administration", "Academic Council"});
        JTextField dateField = new JTextField("2026-09-10");
        JTextField timeField = new JTextField("10:00 AM");
        JTextField roomField = new JTextField();

        form.add(new JLabel("Meeting ID:")); form.add(idField);
        form.add(new JLabel("Meeting Title:")); form.add(titleField);
        form.add(new JLabel("Department:")); form.add(deptBox);
        form.add(new JLabel("Date (YYYY-MM-DD):")); form.add(dateField);
        form.add(new JLabel("Time:")); form.add(timeField);
        form.add(new JLabel("Room / Venue:")); form.add(roomField);

        JButton saveBtn = new JButton("Save Meeting");
        JButton backBtn = new JButton("Back to Dashboard");

        form.add(saveBtn);
        form.add(backBtn);

        panel.add(new JLabel("Schedule a New Meeting", SwingConstants.CENTER), BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);

        saveBtn.addActionListener(e -> {
            if (idField.getText().trim().isEmpty() || titleField.getText().trim().isEmpty() || roomField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, "Please fill in all required fields.", "Validation Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Meeting meeting = new Meeting(idField.getText().trim(), titleField.getText().trim(), (String) deptBox.getSelectedItem(), dateField.getText().trim(), timeField.getText().trim(), roomField.getText().trim());
            boolean success = MeetingDataStore.addMeeting(meeting);
            if (success) {
                JOptionPane.showMessageDialog(mainFrame, "Meeting Scheduled Successfully in Database!");
                idField.setText("");
                titleField.setText("");
                roomField.setText("");
                cardLayout.show(cardPanel, "DASHBOARD");
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Failed to save meeting. Check database connection or duplicate ID.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backBtn.addActionListener(e -> cardLayout.show(cardPanel, "DASHBOARD"));

        return panel;
    }

    private static JPanel createViewMeetingPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel header = new JLabel("All Scheduled Meetings", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(header, BorderLayout.NORTH);

        String[] columns = {"Meeting ID", "Title", "Department", "Date", "Time", "Venue"};
        tableModel = new DefaultTableModel(columns, 0);
        meetingTable = new JTable(tableModel);
        panel.add(new JScrollPane(meetingTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton deleteBtn = new JButton("Cancel/Delete Selected");
        JButton backBtn = new JButton("Back to Dashboard");

        bottomPanel.add(deleteBtn);
        bottomPanel.add(backBtn);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        deleteBtn.addActionListener(e -> {
            int selectedRow = meetingTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(mainFrame, "Please select a meeting row to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                int confirm = JOptionPane.showConfirmDialog(mainFrame, "Are you sure you want to cancel this meeting?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    String meetingId = (String) tableModel.getValueAt(selectedRow, 0);
                    boolean deleted = MeetingDataStore.deleteMeeting(meetingId);
                    if (deleted) {
                        refreshTableData();
                        JOptionPane.showMessageDialog(mainFrame, "Meeting deleted successfully from Database.");
                    } else {
                        JOptionPane.showMessageDialog(mainFrame, "Failed to delete meeting from Database.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        backBtn.addActionListener(e -> cardLayout.show(cardPanel, "DASHBOARD"));

        return panel;
    }

    private static JPanel createChangePasswordPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(40, 90, 40, 90));

        JPasswordField oldPassField = new JPasswordField();
        JPasswordField newPassField = new JPasswordField();
        JButton updateBtn = new JButton("Update Password");
        JButton backBtn = new JButton("Back to Dashboard");

        form.add(new JLabel("Old Password:")); form.add(oldPassField);
        form.add(new JLabel("New Password:")); form.add(newPassField);
        form.add(updateBtn); form.add(backBtn);

        panel.add(new JLabel("Change Account Password", SwingConstants.CENTER), BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);

        updateBtn.addActionListener(e -> {
            String oldP = new String(oldPassField.getPassword()).trim();
            String newP = new String(newPassField.getPassword()).trim();

            if (oldP.equals(currentPassword)) {
                if (newP.isEmpty()) {
                    JOptionPane.showMessageDialog(mainFrame, "New password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    currentPassword = newP;
                    JOptionPane.showMessageDialog(mainFrame, "Password changed successfully!");
                    oldPassField.setText("");
                    newPassField.setText("");
                    cardLayout.show(cardPanel, "DASHBOARD");
                }
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Incorrect old password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backBtn.addActionListener(e -> cardLayout.show(cardPanel, "DASHBOARD"));

        return panel;
    }

    private static void refreshTableData() {
        tableModel.setRowCount(0);
        List<Meeting> list = MeetingDataStore.getAllMeetings();
        for (Meeting m : list) {
            tableModel.addRow(new Object[]{
                    m.getId(),
                    m.getTitle(),
                    m.getDepartment(),
                    m.getDate(),
                    m.getTime(),
                    m.getRoom()
            });
        }
    }
}