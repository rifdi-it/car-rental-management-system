package carrentalsystem.ui;

import carrentalsystem.util.DBConnection;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ReportsForm extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblTotalRevenue, lblActiveRentals;

    // Palette Colors
    private final Color NAVY = new Color(11, 61, 145);
    private final Color BACKGROUND = new Color(245, 245, 245);

    public ReportsForm() {
        setTitle("Financial Reports & Analytics");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        mainPanel.setBackground(BACKGROUND);
        add(mainPanel);

        // --- 1. TOP CARDS PANEL ---
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        statsPanel.setOpaque(false);

        lblTotalRevenue = createStatCard("Total Revenue", "$0.00", new Color(50, 205, 50));
        lblActiveRentals = createStatCard("Active Rentals", "0", new Color(30, 144, 255));

        statsPanel.add(lblTotalRevenue);
        statsPanel.add(lblActiveRentals);
        mainPanel.add(statsPanel, BorderLayout.NORTH);

        // --- 2. CENTER TABLE PANEL ---
        model = new DefaultTableModel(new String[]{
            "ID", "Customer", "Car Model", "Rent Date", "Return Date", "Total Fee", "Status"
        }, 0);
        table = new JTable(model);
        styleTable(table);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createTitledBorder("Transaction History"));
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // --- 3. BOTTOM ACTION ---
        JButton btnExport = new JButton("Refresh Data");
        btnExport.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExport.addActionListener(e -> loadReportData());
        mainPanel.add(btnExport, BorderLayout.SOUTH);

        loadReportData();
    }

    private JLabel createStatCard(String title, String value, Color accent) {
        JLabel card = new JLabel("<html><div style='padding:15px;'>" +
                "<font size='4' color='#666666'>" + title + "</font><br>" +
                "<font size='7' color='#1C1C1C'><b>" + value + "</b></font></div></html>");
        card.setOpaque(true);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createMatteBorder(0, 8, 0, 0, accent));
        return card;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(30);
        table.setGridColor(new Color(230, 230, 230));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(NAVY);
        table.getTableHeader().setForeground(Color.WHITE);
    }

    private void loadReportData() {
        model.setRowCount(0);
        double totalRevenue = 0;
        int activeCount = 0;

        try (Connection con = DBConnection.getConnection()) {
            // Fetch Transactions
            String sql = "SELECT r.rental_id, c.name, v.brand, v.model, r.rent_date, r.return_date, r.total_amount, r.status " +
                         "FROM rental r " +
                         "JOIN customers c ON r.customer_id = c.cust_id " +
                         "JOIN cars v ON r.car_id = v.car_id " +
                         "ORDER BY r.rental_id DESC";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                double amount = rs.getDouble("total_amount");
                String status = rs.getString("status");
                
                totalRevenue += amount;
                if ("Rented".equalsIgnoreCase(status)) activeCount++;

                model.addRow(new Object[]{
                    rs.getInt("rental_id"),
                    rs.getString("name"),
                    rs.getString("brand") + " " + rs.getString("model"),
                    rs.getDate("rent_date"),
                    rs.getDate("return_date"),
                    "$" + amount,
                    status
                });
            }

            // Update Cards
            lblTotalRevenue.setText("<html><div style='padding:15px;'>" +
                    "<font size='4' color='#666666'>Total Revenue</font><br>" +
                    "<font size='7' color='#1C1C1C'><b>$" + totalRevenue + "</b></font></div></html>");
            lblActiveRentals.setText("<html><div style='padding:15px;'>" +
                    "<font size='4' color='#666666'>Active Rentals</font><br>" +
                    "<font size='7' color='#1C1C1C'><b>" + activeCount + "</b></font></div></html>");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}