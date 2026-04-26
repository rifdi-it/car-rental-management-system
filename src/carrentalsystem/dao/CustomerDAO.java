package carrentalsystem.dao;

import carrentalsystem.models.Customer;
import carrentalsystem.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    // 🔹 Fetch all customers (with description)
    public List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customers ORDER BY cust_id";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Customer c = new Customer();
                c.setCustId(rs.getInt("cust_id"));
                c.setName(rs.getString("name"));
                c.setNic(rs.getString("nic"));
                c.setPhone(rs.getString("phone"));
                c.setAddress(rs.getString("address"));
                c.setLicenseNo(rs.getString("license_no"));
                c.setLicenseImage(rs.getString("license_image"));
                c.setDescription(rs.getString("description")); // ✅ NEW
                list.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🔹 Add customer (with description)
    public boolean addCustomer(Customer c) {
        String sql = "INSERT INTO customers "
                   + "(name, nic, phone, address, license_no, license_image, description) "
                   + "VALUES (?,?,?,?,?,?,?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, c.getName());
            pst.setString(2, c.getNic());
            pst.setString(3, c.getPhone());
            pst.setString(4, c.getAddress());
            pst.setString(5, c.getLicenseNo());
            pst.setString(6, c.getLicenseImage());
            pst.setString(7, c.getDescription()); // ✅ NEW

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
