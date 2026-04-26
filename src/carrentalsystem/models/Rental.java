package carrentalsystem.models;

import java.sql.Date;

public class Rental {
    private int rentId;
    private int custId;
    private int carId;
    private Date rentDate;
    private Date expectedReturnDate;
    private Date actualReturnDate;
    private double totalAmount;
    private double advance;
    private String status;

    public int getRentId(){ return rentId; }
    public void setRentId(int id){ this.rentId = id; }
    public int getCustId(){ return custId; }
    public void setCustId(int c){ this.custId = c; }
    public int getCarId(){ return carId; }
    public void setCarId(int c){ this.carId = c; }
    public Date getRentDate(){ return rentDate; }
    public void setRentDate(Date d){ this.rentDate = d; }
    public Date getExpectedReturnDate(){ return expectedReturnDate; }
    public void setExpectedReturnDate(Date d){ this.expectedReturnDate = d; }
    public Date getActualReturnDate(){ return actualReturnDate; }
    public void setActualReturnDate(Date d){ this.actualReturnDate = d; }
    public double getTotalAmount(){ return totalAmount; }
    public void setTotalAmount(double t){ this.totalAmount = t; }
    public double getAdvance(){ return advance; }
    public void setAdvance(double a){ this.advance = a; }
    public String getStatus(){ return status; }
    public void setStatus(String s){ this.status = s; }
}
