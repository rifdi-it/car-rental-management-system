package carrentalsystem.ui;

import carrentalsystem.dao.CustomerDAO;
import carrentalsystem.models.Customer;
import carrentalsystem.util.FileUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

public class CustomerForm extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private CustomerDAO dao = new CustomerDAO();

    // Palette Colors
    private final Color PRIMARY_BLUE = new Color(30, 144, 255);
    private final Color ACCENT_NAVY = new Color(11, 61, 145);
    private final Color BACKGROUND_LIGHT = new Color(245, 245, 245);

    public CustomerForm() {
        setTitle("Client Database - CRMS");
        setSize(1100, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout(10, 10));

        // --- HEADER ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("Customer Management");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(ACCENT_NAVY);
        headerPanel.add(lblTitle, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // --- TABLE ---
        model = new DefaultTableModel(
                new String[]{"ID", "Full Name", "NIC Number", "Phone", "Address", "License No", "Description", "Image Path"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        add(scrollPane, BorderLayout.CENTER);

        // --- ACTION PANEL ---
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        actionPanel.setBackground(BACKGROUND_LIGHT);

        JButton btnAdd = createStyledButton("Register New Customer", PRIMARY_BLUE);
        JButton btnView = createStyledButton("View Document", Color.DARK_GRAY);
        JButton btnDetails = createStyledButton("View Details", ACCENT_NAVY); // ⭐ Optional

        actionPanel.add(btnAdd);
        actionPanel.add(btnView);
        actionPanel.add(btnDetails);
        add(actionPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> showAddCustomerDialog());
        btnView.addActionListener(e -> viewLicenseImage());
        btnDetails.addActionListener(e -> viewDescription());

        load();
    }

    private void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(235, 235, 235));
        table.setSelectionBackground(new Color(230, 242, 255));
        table.setGridColor(new Color(230, 230, 230));

        // Hide Image Path column (index 7)
        table.getColumnModel().getColumn(7).setMinWidth(0);
        table.getColumnModel().getColumn(7).setMaxWidth(0);
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return btn;
    }

    // --- ADD CUSTOMER DIALOG ---
    private void showAddCustomerDialog() {
        JTextField name = new JTextField();
        JTextField nic = new JTextField();
        JTextField phone = new JTextField();
        JTextField addr = new JTextField();
        JTextField lic = new JTextField();

        JTextArea desc = new JTextArea(3, 20);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(desc);

        JLabel lblImgStatus = new JLabel("No document uploaded", SwingConstants.CENTER);
        lblImgStatus.setForeground(Color.GRAY);

        JButton btnUpload = new JButton("Select License Image");
        final String[] imgPath = {null};

        btnUpload.addActionListener(ev -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    File src = fc.getSelectedFile();
                    imgPath[0] = FileUtil.saveFile(src, "uploads");
                    lblImgStatus.setText("✓ " + src.getName());
                    lblImgStatus.setForeground(new Color(0, 150, 0));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error saving file.");
                }
            }
        });

        Object[] fields = {
                "Full Name:", name,
                "NIC / Passport:", nic,
                "Phone Number:", phone,
                "Home Address:", addr,
                "License Number:", lic,
                "Description / Notes:", descScroll,
                "Verification Document:", btnUpload,
                "", lblImgStatus
        };

        if (JOptionPane.showConfirmDialog(this, fields, "Register Customer",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {

            if (name.getText().trim().isEmpty() || phone.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and Phone are required.");
                return;
            }

            Customer c = new Customer();
            c.setName(name.getText());
            c.setNic(nic.getText());
            c.setPhone(phone.getText());
            c.setAddress(addr.getText());
            c.setLicenseNo(lic.getText());
            c.setDescription(desc.getText()); // ✅ SAVE DESCRIPTION
            c.setLicenseImage(imgPath[0]);

            dao.addCustomer(c);
            load();
        }
    }

    // --- VIEW IMAGE ---
    private void viewLicenseImage() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Select a customer first.");
            return;
        }

        String path = (String) model.getValueAt(r, 7);
        if (path == null || path.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No document available.");
            return;
        }

        File imgFile = new File(path);
        if (!imgFile.exists()) {
            JOptionPane.showMessageDialog(this, "File not found.");
            return;
        }

        ImageIcon icon = new ImageIcon(imgFile.getAbsolutePath());
        Image img = icon.getImage().getScaledInstance(500, -1, Image.SCALE_SMOOTH);
        JOptionPane.showMessageDialog(this, new JLabel(new ImageIcon(img)),
                "Client Verification Document", JOptionPane.PLAIN_MESSAGE);
    }

    // ⭐ VIEW DESCRIPTION
    private void viewDescription() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Select a customer first");
            return;
        }

        String details = model.getValueAt(r, 6).toString();
        JTextArea ta = new JTextArea(details, 8, 30);
        ta.setWrapStyleWord(true);
        ta.setLineWrap(true);
        ta.setEditable(false);

        JOptionPane.showMessageDialog(this, new JScrollPane(ta),
                "Customer Description", JOptionPane.INFORMATION_MESSAGE);
    }

    // --- LOAD DATA ---
    private void load() {
        model.setRowCount(0);
        List<Customer> list = dao.getAllCustomers();
        for (Customer c : list) {
            model.addRow(new Object[]{
                    c.getCustId(),
                    c.getName(),
                    c.getNic(),
                    c.getPhone(),
                    c.getAddress(),
                    c.getLicenseNo(),
                    c.getDescription(),
                    c.getLicenseImage()
            });
        }
    }
}
