package carrentalsystem.ui;

import carrentalsystem.dao.RentalDAO;
import carrentalsystem.util.DBConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class ReturnForm extends JFrame {
    private JTextField txtRentId, txtExtra;
    private JLabel lblInfo;
    private JButton btnReturn, btnFetch;
    private RentalDAO dao = new RentalDAO();

    // Palette Colors
    private final Color ACTION_ORANGE = new Color(255, 165, 0);
    private final Color PRIMARY_NAVY = new Color(11, 61, 145);

    public ReturnForm() {
        setTitle("Finalize Return - CRMS");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel);

        // --- 1. Top Search Section ---
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setBackground(Color.WHITE);
        
        txtRentId = new JTextField();
        btnFetch = new JButton("Check ID");
        styleButton(btnFetch, Color.LIGHT_GRAY, Color.BLACK);
        
        topPanel.add(new JLabel("Rental ID:"), BorderLayout.WEST);
        topPanel.add(txtRentId, BorderLayout.CENTER);
        topPanel.add(btnFetch, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // --- 2. Information Display Area ---
        lblInfo = new JLabel("<html><body style='text-align: center;'>Enter a Rental ID to see details</body></html>", SwingConstants.CENTER);
        lblInfo.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        lblInfo.setOpaque(true);
        lblInfo.setBackground(new Color(250, 250, 250));
        mainPanel.add(lblInfo, BorderLayout.CENTER);

        // --- 3. Bottom Action Section ---
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1, 5, 10));
        bottomPanel.setBackground(Color.WHITE);

        JPanel extraPanel = new JPanel(new BorderLayout(10, 0));
        extraPanel.setBackground(Color.WHITE);
        txtExtra = new JTextField("0.00");
        extraPanel.add(new JLabel("Damage / Late Charges (Rs):"), BorderLayout.WEST);
        extraPanel.add(txtExtra, BorderLayout.CENTER);

        btnReturn = new JButton("Process Car Return");
        styleButton(btnReturn, ACTION_ORANGE, Color.WHITE);
        btnReturn.setEnabled(false); // Enable only after verification

        bottomPanel.add(extraPanel);
        bottomPanel.add(btnReturn);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Logic Listeners
        btnFetch.addActionListener(e -> fetchRentalInfo());
        btnReturn.addActionListener(e -> processReturn());
    }

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

private void fetchRentalInfo() {
    String idText = txtRentId.getText().trim();
    if (idText.isEmpty()) return;

    try (Connection con = DBConnection.getConnection()) {
        // UPDATED: Joined with 'rentals' table
        String sql = "SELECT r.*, c.name, v.brand, v.model FROM rentals r " +
                     "JOIN customers c ON r.cust_id = c.cust_id " +
                     "JOIN cars v ON r.car_id = v.car_id " +
                     "WHERE r.rent_id = ? AND r.status = 'Rented'";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, Integer.parseInt(idText));
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            lblInfo.setText("<html><div style='padding:10px;'><b>Customer:</b> " + rs.getString("name") + 
                            "<br><b>Vehicle:</b> " + rs.getString("brand") + " " + rs.getString("model") + 
                            "<br><b>Due Date:</b> " + rs.getDate("expected_return_date") + "</div></html>");
            btnReturn.setEnabled(true);
        } else {
            lblInfo.setText("<html><font color='red'>ID not found or already returned.</font></html>");
            btnReturn.setEnabled(false);
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error fetching data.");
    }
}

    private void processReturn() {
        int rentId = Integer.parseInt(txtRentId.getText());
        double extra;
        try { extra = Double.parseDouble(txtExtra.getText()); } catch(Exception ex) { extra = 0; }

        if (dao.returnCar(rentId, Date.valueOf(LocalDate.now()), extra)) {
            JOptionPane.showMessageDialog(this, "Car returned successfully! Inventory updated.", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error during return processing.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}