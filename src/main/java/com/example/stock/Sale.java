package com.example.stock;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class Sale {
    private SimpleStringProperty orderNumber;
    private SimpleStringProperty name;
    private SimpleStringProperty type;
    private SimpleStringProperty size;
    private SimpleIntegerProperty count;
    private SimpleObjectProperty<LocalDate> date;
    private SimpleBooleanProperty inStock;

    public Sale(String orderNumber, String name, String type, String size, int count, LocalDate date, Boolean inStock) {
        this.orderNumber = new SimpleStringProperty(orderNumber);
        this.name = new SimpleStringProperty(name);
        this.type = new SimpleStringProperty(type);
        this.size = new SimpleStringProperty(size);
        this.count = new SimpleIntegerProperty(count);
        this.date = new SimpleObjectProperty<LocalDate>(date);
        this.inStock = new SimpleBooleanProperty(inStock);
    }

    public String getOrderNumber(){ return orderNumber.get(); }

    public String getName() {
        return name.get();
    }

    public String getType() {
        return type.get();
    }

    public String getSize() {
        return size.get();
    }

    public int getCount() {
        return count.get();
    }

    public LocalDate getDate() { return date.get(); }

    public Boolean getInStock() { return inStock.get(); }

    public void setOrderNumber(String orderNumber) { this.orderNumber.set(orderNumber);}

    public void setName(String name) {
        this.name.set(name);
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public void setSize(String size) {
        this.size.set(size);
    }

    public void setCount(int count) {
        this.count.set(count);
    }

    public void setDate(LocalDate date) { this.date.set(date); }

    public void setInStock(Boolean inStock) {this.inStock.set(inStock);}



    @Override
    public String toString() {
        return getName()+", "+getType()+", "+getSize()+", "+getCount()+", "+getInStock();
    }
}
