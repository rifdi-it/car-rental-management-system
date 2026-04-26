package carrentalsystem.ui;

import carrentalsystem.models.User;
import carrentalsystem.dao.UserDAO;
import carrentalsystem.util.PasswordUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RegisterForm extends JFrame {
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JComboBox<String> cbRole;
    private JButton btnSave;
    private JButton btnCancel;

    // Consistency with Palette
    private final Color PRIMARY_NAVY = new Color(11, 61, 145);
    private final Color SUCCESS_GREEN = new Color(50, 205, 50); // Lime Green from your palette
    private final Color TEXT_DARK = new Color(28, 28, 28);

    public RegisterForm() {
        setTitle("Create Account - CRMS");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        // Main Container
        JPanel mainPanel = new JPanel(new BorderLayout(10, 20));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        add(mainPanel);

        // Header
        JLabel lblTitle = new JLabel("Register New User", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(PRIMARY_NAVY);
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Form Section
        JPanel formPanel = new JPanel(new GridLayout(6, 1, 2, 2));
        formPanel.setBackground(Color.WHITE);

        txtUser = new JTextField();
        txtPass = new JPasswordField();
        cbRole = new JComboBox<>(new String[]{"admin", "staff"});
        
        // Styling the ComboBox
        cbRole.setBackground(Color.WHITE);
        cbRole.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        formPanel.add(createLabel("Username"));
        formPanel.add(txtUser);
        formPanel.add(createLabel("Password"));
        formPanel.add(txtPass);
        formPanel.add(createLabel("Account Role"));
        formPanel.add(cbRole);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button Section
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        btnSave = new JButton("Register");
        styleButton(btnSave, SUCCESS_GREEN, Color.WHITE);

        btnCancel = new JButton("Cancel");
        styleButton(btnCancel, new Color(220, 220, 220), TEXT_DARK);

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Listeners
        btnSave.addActionListener(e -> handleRegistration());
        btnCancel.addActionListener(e -> this.dispose());
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_DARK);
        return label;
    }

    private void styleButton(JButton button, Color bg, Color fg) {
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorderPainted(false);
        button.setOpaque(true);
    }

    private void handleRegistration() {
        String u = txtUser.getText().trim();
        String p = new String(txtPass.getPassword());
        String r = cbRole.getSelectedItem().toString();

        if (u.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Registration Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = new User();
        user.setUsername(u);
        user.setPassword(PasswordUtil.hash(p));
        user.setRole(r);

        if (UserDAO.register(user)) {
            JOptionPane.showMessageDialog(this, "User registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error saving user. Username might already exist.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}