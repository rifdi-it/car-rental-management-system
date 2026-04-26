package carrentalsystem.ui;

import carrentalsystem.models.User;
import carrentalsystem.util.DBConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;

public class Dashboard extends JFrame {
    private JLabel lblTotalCars, lblRented, lblCustomers;
    private User currentUser;

    // Palette Colors
    private final Color SIDEBAR_NAVY = new Color(11, 61, 145);
    private final Color BG_LIGHT = new Color(245, 245, 245);
    private final Color ACCENT_BLUE = new Color(30, 144, 255);
    private final Color TEXT_WHITE = Color.WHITE;

    public Dashboard(User user) {
        this.currentUser = user;
        setTitle("CRMS - Professional Dashboard");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // Main Container
        JPanel mainContainer = new JPanel(new BorderLayout());
        add(mainContainer);

        // --- 1. SIDEBAR ---
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(250, 700));
        sidebar.setBackground(SIDEBAR_NAVY);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(20, 10, 20, 10));

        JLabel logo = new JLabel("CAR RENTAL");
        logo.setForeground(TEXT_WHITE);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(logo);
        sidebar.add(Box.createRigidArea(new Dimension(0, 40)));

        // Sidebar Buttons
        JButton btnManageCars = createNavButton("Manage Cars");
        JButton btnManageCust = createNavButton("Customers");
        JButton btnRent = createNavButton("Rent a Car");
        JButton btnReturn = createNavButton("Return Car");
        JButton btnUsers = createNavButton("User Management");

        sidebar.add(btnManageCars);
        sidebar.add(btnManageCust);
        sidebar.add(btnRent);
        sidebar.add(btnReturn);
        sidebar.add(btnUsers);
        
        mainContainer.add(sidebar, BorderLayout.WEST);

        // --- 2. MAIN CONTENT AREA ---
        JPanel contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(BG_LIGHT);
        mainContainer.add(contentArea, BorderLayout.CENTER);

        // Header with User Info
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 70));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        
        JLabel lblWelcome = new JLabel("Welcome, " + user.getUsername() + " (" + user.getRole() + ")");
        lblWelcome.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblWelcome.setBorder(new EmptyBorder(0, 20, 0, 0));
        header.add(lblWelcome, BorderLayout.WEST);
        contentArea.add(header, BorderLayout.NORTH);

        // Dashboard Stats (Cards)
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 25));
        statsPanel.setBackground(BG_LIGHT);
        
        lblTotalCars = createStatCard("TOTAL CARS", new Color(70, 130, 180));
        lblRented = createStatCard("ACTIVE RENTALS", new Color(255, 165, 0)); // Orange
        lblCustomers = createStatCard("TOTAL CUSTOMERS", new Color(50, 205, 50)); // Lime Green

        statsPanel.add(lblTotalCars);
        statsPanel.add(lblRented);
        statsPanel.add(lblCustomers);
        contentArea.add(statsPanel, BorderLayout.CENTER);

        // Logic
        btnManageCars.addActionListener(e -> new CarForm().setVisible(true));
        btnManageCust.addActionListener(e -> new CustomerForm().setVisible(true));
        btnRent.addActionListener(e -> new RentForm().setVisible(true));
        btnReturn.addActionListener(e -> new ReturnForm().setVisible(true));
        btnUsers.addActionListener(e -> new RegisterForm().setVisible(true));

        if (!"admin".equalsIgnoreCase(user.getRole())) {
            btnUsers.setVisible(false);
        }

        loadStats();
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(230, 45));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(SIDEBAR_NAVY);
        btn.setForeground(TEXT_WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }

    private JLabel createStatCard(String title, Color accent) {
        JLabel lbl = new JLabel("<html><div style='padding:10px;'><font size='4' color='gray'>" + title + "</font><br><br><font size='6'><b>0</b></font></div></html>");
        lbl.setPreferredSize(new Dimension(220, 120));
        lbl.setOpaque(true);
        lbl.setBackground(Color.WHITE);
        lbl.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, accent)); // Side accent bar
        return lbl;
    }

    private void loadStats() {
        try (Connection con = DBConnection.getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT COUNT(*) AS cnt FROM cars");
            if (rs.next()) updateCardValue(lblTotalCars, "TOTAL CARS", rs.getInt("cnt"));

            rs = st.executeQuery("SELECT COUNT(*) AS cnt FROM cars WHERE status='Rented'");
            if (rs.next()) updateCardValue(lblRented, "ACTIVE RENTALS", rs.getInt("cnt"));

            rs = st.executeQuery("SELECT COUNT(*) AS cnt FROM customers");
            if (rs.next()) updateCardValue(lblCustomers, "TOTAL CUSTOMERS", rs.getInt("cnt"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Auto-refresh stats whenever the Dashboard gains focus
        this.addWindowFocusListener(new java.awt.event.WindowAdapter() {
        @Override
        public void windowGainedFocus(java.awt.event.WindowEvent e) {
        loadStats(); // reload stats whenever the window is focused
    }
});

    }
    
    

    private void updateCardValue(JLabel card, String title, int value) {
        card.setText("<html><div style='padding:10px;'><font size='4' color='gray'>" + title + "</font><br><br><font size='6'><b>" + value + "</b></font></div></html>");
    }
}