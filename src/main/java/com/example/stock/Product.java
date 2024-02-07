package com.example.stock;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Product {
    private final SimpleStringProperty productName;
    private final SimpleStringProperty productType;
    private final SimpleStringProperty productSize;
    private final SimpleIntegerProperty productCount;

    public Product(String productName, String productType, String productSize, int productCount) {
        this.productName = new SimpleStringProperty(productName);
        this.productType = new SimpleStringProperty(productType);
        this.productSize = new SimpleStringProperty(productSize);
        this.productCount = new SimpleIntegerProperty(productCount);
    }


    public String getProductName() {
        return productName.get();
    }

    public void setProductName(String productName) {
        this.productName.set(productName);
    }

    public String getProductType() {
        return productType.get();
    }

    public void setProductType(String productType) {
        this.productType.set(productType);
    }

    public String getProductSize() {
        return productSize.get();
    }

    public void setProductSize(String productSize) {
        this.productSize.set(productSize);
    }

    public int getProductCount() {
        return productCount.get();
    }

    public void setProductCount(int productCount) {
        this.productCount.set(productCount);
    }

    @Override
    public String toString() {
        return getProductName()+getProductType()+getProductSize()+getProductCount();
    }
}