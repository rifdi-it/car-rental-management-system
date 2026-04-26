package carrentalsystem.ui;

import carrentalsystem.dao.CarDAO;
import carrentalsystem.dao.CustomerDAO;
import carrentalsystem.models.Car;
import carrentalsystem.models.Customer;
import carrentalsystem.util.DBConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class RentForm extends JFrame {
    private JComboBox<String> cbCar, cbCust;
    private JTextField txtDays, txtAdvance;
    private JLabel lblTotalDisplay;
    private CarDAO carDAO = new CarDAO();
    private CustomerDAO custDAO = new CustomerDAO();

    private final Color ACCENT_BLUE = new Color(30, 144, 255);
    private final Color NAVY = new Color(11, 61, 145);

    public RentForm() {
        setTitle("New Rental Transaction");
        setSize(550, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel);

        // --- 1. Form Section ---
        JPanel formGrid = new JPanel(new GridLayout(4, 2, 10, 15));
        formGrid.setBackground(Color.WHITE);

        cbCust = new JComboBox<>();
        cbCar = new JComboBox<>();
        txtDays = new JTextField("1");
        txtAdvance = new JTextField("0");

        formGrid.add(new JLabel("Select Customer:"));
        formGrid.add(cbCust);
        formGrid.add(new JLabel("Select Vehicle:"));
        formGrid.add(cbCar);
        formGrid.add(new JLabel("Duration (Days):"));
        formGrid.add(txtDays);
        formGrid.add(new JLabel("Advance Payment (Rs):"));
        formGrid.add(txtAdvance);

        mainPanel.add(formGrid, BorderLayout.CENTER);

        // --- 2. Summary & Action Section ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);

        lblTotalDisplay = new JLabel("Estimated Total: Rs0.00", SwingConstants.CENTER);
        lblTotalDisplay.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotalDisplay.setForeground(new Color(0, 128, 128)); // Teal
        lblTotalDisplay.setBorder(new EmptyBorder(10, 0, 15, 0));
        bottomPanel.add(lblTotalDisplay, BorderLayout.NORTH);

        JButton btnRent = new JButton("Confirm Rental & Print Invoice");
        btnRent.setBackground(ACCENT_BLUE);
        btnRent.setForeground(Color.WHITE);
        btnRent.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRent.setFocusPainted(false);
        btnRent.setPreferredSize(new Dimension(0, 45));
        bottomPanel.add(btnRent, BorderLayout.CENTER);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Listeners for auto-calculating total
        txtDays.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) { calculatePreview(); }
        });
        cbCar.addActionListener(e -> calculatePreview());

        loadCustomers();
        loadCars();
        btnRent.addActionListener(e -> rentCar());
    }

    private void calculatePreview() {
        try {
            String carSel = (String) cbCar.getSelectedItem();
            if (carSel != null) {
                double rate = Double.parseDouble(carSel.split(":")[2]);
                int days = Integer.parseInt(txtDays.getText());
                lblTotalDisplay.setText("Estimated Total: Rs" + (rate * days));
            }
        } catch (Exception e) {
            lblTotalDisplay.setText("Estimated Total: --");
        }
    }

private void rentCar() {
    String custSel = (String) cbCust.getSelectedItem();
    String carSel = (String) cbCar.getSelectedItem();

    if (custSel == null || carSel == null) {
        JOptionPane.showMessageDialog(this, "Please select both a customer and a car.");
        return;
    }

    int custId = Integer.parseInt(custSel.split(":")[0]);
    int carId = Integer.parseInt(carSel.split(":")[0]);
    int days = Integer.parseInt(txtDays.getText());
    double rate = Double.parseDouble(carSel.split(":")[2]);
    double total = rate * days;
    double advance = Double.parseDouble(txtAdvance.getText());

    LocalDate rentDate = LocalDate.now();
    LocalDate returnDate = rentDate.plusDays(days);

    try (Connection con = DBConnection.getConnection()) {
        con.setAutoCommit(false);

        // 1. Insert rental
        String sql = "INSERT INTO rentals(cust_id, car_id, rent_date, expected_return_date, total_amount, advance, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pst.setInt(1, custId);
        pst.setInt(2, carId);
        pst.setDate(3, Date.valueOf(rentDate));
        pst.setDate(4, Date.valueOf(returnDate));
        pst.setDouble(5, total);
        pst.setDouble(6, advance);
        pst.setString(7, "Rented");
        pst.executeUpdate();

        // Get generated rental ID
        ResultSet rs = pst.getGeneratedKeys();
        int rentalId = 0;
        if (rs.next()) rentalId = rs.getInt(1);

        // 2. Update car status
        String updateCar = "UPDATE cars SET status='Rented' WHERE car_id=?";
        PreparedStatement pstCar = con.prepareStatement(updateCar);
        pstCar.setInt(1, carId);
        pstCar.executeUpdate();

        con.commit();

        // 3. Generate invoice PDF
        String customerName = custSel.split(":")[1];
        String carName = carSel.split(":")[1]; // brand + model
        String filename = "Invoice_" + rentalId + ".pdf";
        InvoiceUtil.createSimpleInvoice(filename,
                String.valueOf(rentalId),
                customerName,
                carName,
                rentDate.toString(),
                returnDate.toString(),
                total);

        // 4. Open PDF automatically
        try {
            if (java.awt.Desktop.isDesktopSupported()) {
                java.awt.Desktop.getDesktop().open(new java.io.File(filename));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invoice generated but could not open automatically.\nFile: " + filename);
        }

        JOptionPane.showMessageDialog(this, "Rental Processed Successfully!\nInvoice saved: " + filename);
        this.dispose();

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
    }
}

    private void loadCustomers() {
        List<Customer> list = custDAO.getAllCustomers();
        for (Customer c : list) cbCust.addItem(c.getCustId() + ":" + c.getName());
    }

    private void loadCars() {
        List<Car> list = carDAO.getAllCars();
        for (Car c : list) {
            if ("Available".equalsIgnoreCase(c.getStatus())) {
                cbCar.addItem(c.getCarId() + ":" + c.getBrand() + " " + c.getModel() + ":" + c.getDailyRate());
            }
        }
    }
}