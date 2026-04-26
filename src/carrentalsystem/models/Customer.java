package carrentalsystem.models;

public class Customer {
    private int custId;
    private String name;
    private String nic;
    private String phone;
    private String address;
    private String licenseNo;
    private String licenseImage;

    public int getCustId(){ return custId; }
    public void setCustId(int id){ this.custId = id; }
    public String getName(){ return name; }
    public void setName(String n){ this.name = n; }
    public String getNic(){ return nic; }
    public void setNic(String n){ this.nic = n; }
    public String getPhone(){ return phone; }
    public void setPhone(String p){ this.phone = p; }
    public String getAddress(){ return address; }
    public void setAddress(String a){ this.address = a; }
    public String getLicenseNo(){ return licenseNo; }
    public void setLicenseNo(String l){ this.licenseNo = l; }
    public String getLicenseImage(){ return licenseImage; }
    public void setLicenseImage(String path){ this.licenseImage = path; }
    private String description;

public String getDescription() {
    return description;
}

public void setDescription(String description) {
    this.description = description;
}

}
