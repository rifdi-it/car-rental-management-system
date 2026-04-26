package carrentalsystem.models;

public class Car {
    private int carId;
    private String plateNo;
    private String brand;
    private String model;
    private int year;
    private String fuelType;
    private double dailyRate;
    private String status;

    public int getCarId(){ return carId; }
    public void setCarId(int id){ this.carId = id; }
    public String getPlateNo(){ return plateNo; }
    public void setPlateNo(String p){ this.plateNo = p; }
    public String getBrand(){ return brand; }
    public void setBrand(String b){ this.brand = b; }
    public String getModel(){ return model; }
    public void setModel(String m){ this.model = m; }
    public int getYear(){ return year; }
    public void setYear(int y){ this.year = y; }
    public String getFuelType(){ return fuelType; }
    public void setFuelType(String f){ this.fuelType = f; }
    public double getDailyRate(){ return dailyRate; }
    public void setDailyRate(double r){ this.dailyRate = r; }
    public String getStatus(){ return status; }
    public void setStatus(String s){ this.status = s; }
}
