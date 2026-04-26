package carrentalsystem.dao;

import carrentalsystem.models.Car;
import carrentalsystem.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDAO {
    public List<Car> getAllCars() {
        List<Car> list = new ArrayList<>();
        String sql = "SELECT * FROM cars ORDER BY car_id";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Car c = new Car();
                c.setCarId(rs.getInt("car_id"));
                c.setPlateNo(rs.getString("plate_no"));
                c.setBrand(rs.getString("brand"));
                c.setModel(rs.getString("model"));
                c.setYear(rs.getInt("year"));
                c.setFuelType(rs.getString("fuel_type"));
                c.setDailyRate(rs.getDouble("daily_rate"));
                c.setStatus(rs.getString("status"));
                list.add(c);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean addCar(Car c) {
        String sql = "INSERT INTO cars (plate_no,brand,model,year,fuel_type,daily_rate,status) VALUES (?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, c.getPlateNo());
            pst.setString(2, c.getBrand());
            pst.setString(3, c.getModel());
            pst.setInt(4, c.getYear());
            pst.setString(5, c.getFuelType());
            pst.setDouble(6, c.getDailyRate());
            pst.setString(7, c.getStatus());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean updateCar(Car c) {
        String sql = "UPDATE cars SET plate_no=?,brand=?,model=?,year=?,fuel_type=?,daily_rate=?,status=? WHERE car_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, c.getPlateNo());
            pst.setString(2, c.getBrand());
            pst.setString(3, c.getModel());
            pst.setInt(4, c.getYear());
            pst.setString(5, c.getFuelType());
            pst.setDouble(6, c.getDailyRate());
            pst.setString(7, c.getStatus());
            pst.setInt(8, c.getCarId());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean deleteCar(int carId) {
        String sql = "DELETE FROM cars WHERE car_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, carId);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}
