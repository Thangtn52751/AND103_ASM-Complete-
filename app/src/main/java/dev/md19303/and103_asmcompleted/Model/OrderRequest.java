package dev.md19303.and103_asmcompleted.Model;

public class OrderRequest {
    private String productId;
    private String productName;
    private String description;
    private double price;
    private double rate;
    private String wardCode;
    private int districtID;
    private int provinceID;

    // Constructor
    public OrderRequest(String productId, String productName, String description, double price, double rate, String wardCode, int districtID, int provinceID) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.rate = rate;
        this.wardCode = wardCode;
        this.districtID = districtID;
        this.provinceID = provinceID;
    }

    // Getters and Setters

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDistrictID() {
        return districtID;
    }

    public void setDistrictID(int districtID) {
        this.districtID = districtID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProvinceID() {
        return provinceID;
    }

    public void setProvinceID(int provinceID) {
        this.provinceID = provinceID;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getWardCode() {
        return wardCode;
    }

    public void setWardCode(String wardCode) {
        this.wardCode = wardCode;
    }
}
