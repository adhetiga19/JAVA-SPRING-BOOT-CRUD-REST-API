package com.example.restapi.Model;

import javax.persistence.*;

@Entity(name = "Tbl_Product")
public class ProductModel {
    private String productCode;
    private String productName;
    private long productPrice;
    private String storeName;
    private String brandName;
    private String categoryName;
    private String productDescription;

    public ProductModel() {
    }

    public ProductModel(String productCode, String productName, long productPrice, String storeName, String brandName, String categoryName, String productDescription) {
        this.productCode = productCode;
        this.productName = productName;
        this.productPrice = productPrice;
        this.storeName = storeName;
        this.brandName = brandName;
        this.categoryName = categoryName;
        this.productDescription = productDescription;
    }

    @Id
    public String getProductCode() {
        return productCode;
    }
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getProductPrice() {
        return productPrice;
    }
    public void setProductPrice(long productPrice) {
        this.productPrice = productPrice;
    }

    public String getStoreName() {
        return storeName;
    }
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getBrandName() {
        return brandName;
    }
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getProductDescription() {
        return productDescription;
    }
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
}
