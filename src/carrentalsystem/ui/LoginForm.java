package carrentalsystem.ui;

import carrentalsystem.dao.UserDAO;
import carrentalsystem.models.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginForm extends JFrame {
    public static String loggedRole = "";
    private JTextField txtUser;
    private JPasswordField txtPass;

    // Defined Colors from your Palette
    private final Color PRIMARY_NAVY = new Color(11, 61, 145);
    private final Color ACTION_BLUE = new Color(30, 144, 255);
    private final Color BACKGROUND_LIGHT = new Color(245, 245, 245);
    private final Color TEXT_DARK = new Color(28, 28, 28);

    public LoginForm() {
        setTitle("Car Rental - Secure Login");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Main Container with Padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 20));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        add(mainPanel);

        // Header Section
        JLabel lblTitle = new JLabel("Welcome Back", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(PRIMARY_NAVY);
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // Form Section (Grid Layout for alignment)
        JPanel formPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        formPanel.setBackground(Color.WHITE);

        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUser.setForeground(TEXT_DARK);
        txtUser = new JTextField();
        txtUser.setPreferredSize(new Dimension(0, 35));

        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPass.setForeground(TEXT_DARK);
        txtPass = new JPasswordField();

        formPanel.add(lblUser);
        formPanel.add(txtUser);
        formPanel.add(lblPass);
        formPanel.add(txtPass);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button Section
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton btnLogin = new JButton("Login");
        styleButton(btnLogin, ACTION_BLUE, Color.WHITE);

        JButton btnRegister = new JButton("Register");
        styleButton(btnRegister, Color.LIGHT_GRAY, TEXT_DARK);

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Logic
        btnLogin.addActionListener(e -> handleLogin());
        btnRegister.addActionListener(e -> new RegisterForm().setVisible(true));

        // UX: Press Enter to Login
        getRootPane().setDefaultButton(btnLogin);
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

    private void handleLogin() {
        String u = txtUser.getText().trim();
        String p = new String(txtPass.getPassword());

        if (u.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = UserDAO.authenticate(u, p);
        if (user != null) {
            loggedRole = user.getRole();
            JOptionPane.showMessageDialog(this, "Login Successful! Welcome, " + user.getUsername());
            new Dashboard(user).setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}