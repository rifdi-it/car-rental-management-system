package carrentalsystem.ui;

import carrentalsystem.dao.CarDAO;
import carrentalsystem.models.Car;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class CarForm extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private CarDAO carDAO = new CarDAO();
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtSearch;

    // Palette Colors
    private final Color PRIMARY_BLUE = new Color(30, 144, 255);
    private final Color SUCCESS_GREEN = new Color(50, 205, 50);
    private final Color DANGER_RED = new Color(255, 76, 76);

    public CarForm() {
        setTitle("Inventory Management - Cars");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout(10, 10));

        // --- 1. Top Panel (Search & Branding) ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("Fleet Inventory");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        topPanel.add(lblTitle, BorderLayout.WEST);

        JPanel searchWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchWrapper.setBackground(Color.WHITE);
        txtSearch = new JTextField(20);
        txtSearch.setPreferredSize(new Dimension(200, 30));
        searchWrapper.add(new JLabel("Search: "));
        searchWrapper.add(txtSearch);
        topPanel.add(searchWrapper, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // --- 2. Table Section ---
        model = new DefaultTableModel(new String[]{"ID", "Plate", "Brand", "Model", "Year", "Fuel", "Daily Rate", "Status"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        styleTable(table);
        
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // --- 3. Bottom Panel (Actions) ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        bottomPanel.setBackground(new Color(245, 245, 245));
        
        JButton btnAdd = createStyledButton("Add New Car", PRIMARY_BLUE);
        JButton btnEdit = createStyledButton("Edit Selected", Color.GRAY);
        JButton btnDelete = createStyledButton("Remove Car", DANGER_RED);

        bottomPanel.add(btnAdd);
        bottomPanel.add(btnEdit);
        bottomPanel.add(btnDelete);
        add(bottomPanel, BorderLayout.SOUTH);

        // Logic Listeners
        setupSearchLogic();
        btnAdd.addActionListener(e -> addCarDialog());
        btnDelete.addActionListener(e -> handleDelete());
        btnEdit.addActionListener(e -> handleEdit());

        loadCars();
    }

    private void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.setSelectionBackground(new Color(230, 242, 255));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(230, 230, 230));

        // Custom renderer for Status Column
        table.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isS, boolean hasF, int row, int col) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isS, hasF, row, col);
                l.setHorizontalAlignment(SwingConstants.CENTER);
                if ("Available".equals(value)) l.setForeground(new Color(0, 150, 0));
                else if ("Rented".equals(value)) l.setForeground(new Color(200, 100, 0));
                else l.setForeground(Color.GRAY);
                return l;
            }
        });
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

    private void setupSearchLogic() {
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void update() {
                String t = txtSearch.getText();
                if (t.trim().isEmpty()) sorter.setRowFilter(null);
                else sorter.setRowFilter(RowFilter.regexFilter("(?i)" + t));
            }
            public void insertUpdate(DocumentEvent e) { update(); }
            public void removeUpdate(DocumentEvent e) { update(); }
            public void changedUpdate(DocumentEvent e) { update(); }
        });
    }

    // ... Keep your existing handleDelete() and handleEdit() logic, 
    // but update the JOptionPane calls to use the clean styles.

    private void loadCars() {
        model.setRowCount(0);
        List<Car> list = carDAO.getAllCars();
        for (Car c : list) {
            model.addRow(new Object[]{c.getCarId(), c.getPlateNo(), c.getBrand(), c.getModel(), c.getYear(), c.getFuelType(), c.getDailyRate(), c.getStatus()});
        }
    }
    
    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a car to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(row);
        int id = Integer.parseInt(model.getValueAt(modelRow, 0).toString());

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this car? This action cannot be undone.", 
            "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            carDAO.deleteCar(id);
            loadCars();
        }
    }

    private void handleEdit() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a car to edit.");
            return;
        }

        int modelRow = table.convertRowIndexToModel(row);
        int id = Integer.parseInt(model.getValueAt(modelRow, 0).toString());

        // Create form fields with existing data
        JTextField tfPlate = new JTextField(model.getValueAt(modelRow, 1).toString());
        JTextField tfBrand = new JTextField(model.getValueAt(modelRow, 2).toString());
        JTextField tfModel = new JTextField(model.getValueAt(modelRow, 3).toString());
        JTextField tfYear = new JTextField(model.getValueAt(modelRow, 4).toString());
        JTextField tfFuel = new JTextField(model.getValueAt(modelRow, 5).toString());
        JTextField tfRate = new JTextField(model.getValueAt(modelRow, 6).toString());
        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"Available", "Rented", "Maintenance"});
        cbStatus.setSelectedItem(model.getValueAt(modelRow, 7).toString());

        Object[] fields = {
            "Plate Number:", tfPlate,
            "Brand:", tfBrand,
            "Model:", tfModel,
            "Year:", tfYear,
            "Fuel Type:", tfFuel,
            "Daily Rate ($):", tfRate,
            "Status:", cbStatus
        };

        int ok = JOptionPane.showConfirmDialog(this, fields, "Update Car Details", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (ok == JOptionPane.OK_OPTION) {
            Car c = new Car();
            c.setCarId(id);
            c.setPlateNo(tfPlate.getText());
            c.setBrand(tfBrand.getText());
            c.setModel(tfModel.getText());
            try { c.setYear(Integer.parseInt(tfYear.getText())); } catch(Exception e) { c.setYear(0); }
            c.setFuelType(tfFuel.getText());
            try { c.setDailyRate(Double.parseDouble(tfRate.getText())); } catch(Exception e) { c.setDailyRate(0); }
            c.setStatus(cbStatus.getSelectedItem().toString());
            
            carDAO.updateCar(c);
            loadCars();
        }
    }

    private void addCarDialog() {
        JTextField plate = new JTextField();
        JTextField brand = new JTextField();
        JTextField carModelField = new JTextField();
        JTextField year = new JTextField();
        JTextField fuel = new JTextField();
        JTextField rate = new JTextField();

        Object[] fields = {
            "Plate Number:", plate,
            "Brand:", brand,
            "Model:", carModelField,
            "Year:", year,
            "Fuel Type:", fuel,
            "Daily Rate ($):", rate
        };

        int ok = JOptionPane.showConfirmDialog(this, fields, "Add New Car to Fleet", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (ok == JOptionPane.OK_OPTION) {
            Car c = new Car();
            c.setPlateNo(plate.getText());
            c.setBrand(brand.getText());
            c.setModel(carModelField.getText());
            try { c.setYear(Integer.parseInt(year.getText())); } catch(Exception e) { c.setYear(0); }
            c.setFuelType(fuel.getText());
            try { c.setDailyRate(Double.parseDouble(rate.getText())); } catch(Exception e) { c.setDailyRate(0); }
            c.setStatus("Available");
            
            carDAO.addCar(c);
            loadCars();
        }
    }
    
}