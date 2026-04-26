package carrentalsystem.dao;

import carrentalsystem.util.DBConnection;
import java.sql.*;

public class RentalDAO {
    
    public boolean returnCar(int rentId, Date actualReturnDate, double extraCharges) {
    String getRental = "SELECT car_id FROM rentals WHERE rent_id = ?";
    String updateRental = "UPDATE rentals SET actual_return_date = ?, status = 'Returned', total_amount = total_amount + ? WHERE rent_id = ?";
    String updateCar = "UPDATE cars SET status='Available' WHERE car_id = ?";

    try (Connection con = DBConnection.getConnection()) {
        con.setAutoCommit(false);

        int carId = -1;
        try (PreparedStatement pst = con.prepareStatement(getRental)) {
            pst.setInt(1, rentId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                carId = rs.getInt("car_id");
            } else {
                return false; // Rental ID not found
            }
        }

        try (PreparedStatement pst2 = con.prepareStatement(updateRental)) {
            pst2.setDate(1, actualReturnDate);
            pst2.setDouble(2, extraCharges);
            pst2.setInt(3, rentId);
            pst2.executeUpdate();
        }

        try (PreparedStatement pst3 = con.prepareStatement(updateCar)) {
            pst3.setInt(1, carId);
            pst3.executeUpdate(); // Car status updated to Available
        }

        con.commit();
        return true;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

}