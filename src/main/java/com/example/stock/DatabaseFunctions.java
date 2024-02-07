package com.example.stock;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseFunctions{
    String dbName = "stock";
    String user = "postgres";
    String password = "1234";
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    private List<Product> products = new ArrayList<>();
    private List<Sale> sales = new ArrayList<>();

    AlertWindows alertWindows = new AlertWindows();

    public Connection connectToDb() {

        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+dbName,user,password);
        } catch (Exception e) {
            alertWindows.errorAlertWindow("Couldn't connect to database");
        }
        return conn;
    }

    public List<Product> retrieveData(String sql){
        conn = connectToDb();
        try {
            preparedStatement = conn.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String productName = resultSet.getString("productName");
                String productType = resultSet.getString("productType");
                String productSize = resultSet.getString("productSize");
                int productCount = resultSet.getInt("productCount");
                products.add(new Product(productName, productType, productSize, productCount));
             }
            resultSet.close();
            preparedStatement.close();
            conn.close();
        }catch (Exception exception) {
            alertWindows.errorAlertWindow("An error occured!");
        }
        return products;
    }

    public List<Product> displayAllData(){
        retrieveData("SELECT * FROM products");
        return products;
    }

    public List<Sale> retrieveSales(String sql){
        conn = connectToDb();
        try {
            preparedStatement = conn.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String orderNumber = resultSet.getString("orderNumber");
                String name = resultSet.getString("productname");
                String type = resultSet.getString("producttype");
                String size = resultSet.getString("productsize");
                int count = resultSet.getInt("productcount");
                Date date = resultSet.getDate("orderdate");
                LocalDate localDate = date.toLocalDate();
                Boolean stock = resultSet.getBoolean("inStock");
                sales.add(new Sale(orderNumber,name,type,size,count,localDate,stock));
            }
            resultSet.close();
            preparedStatement.close();
            conn.close();
            System.out.println(products);
        }catch (Exception exception) {
            alertWindows.errorAlertWindow("An error occured!");
            System.out.println(exception);
        }
        return sales;
    }

    public List<Sale> displayAllSale(){
        retrieveSales("SELECT * FROM sales");
        return sales;
    }

    public List<Product> displayFilteredData(String text){
        retrieveData("SELECT * FROM products WHERE productName ILIKE '%"+text+"%'");
        return products;
    }

    public List<Sale> displayFilteredSales(String text){
        retrieveSales("SELECT * FROM sales WHERE ordernumber ILIKE '%"+text+"%'");
        return sales;
    }

    public void addData(String sql, ActionEvent event, Stage stage, Scene scene){
        conn = connectToDb();
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);
            conn.close();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void removeData(String sql){
        conn = connectToDb();
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);
            conn.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void addStockToDb(String nameText, String typeText, String sizeText, int countText, ActionEvent event, Stage stage, Scene scene){
        conn = connectToDb();
        boolean existInDb = false;
        try{
            preparedStatement = conn.prepareStatement("SELECT * FROM products");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String productName = resultSet.getString("productName");
                String productType = resultSet.getString("productType");
                String productSize = resultSet.getString("productSize");
                int productCount = resultSet.getInt("productCount");
                if (productName.equals(nameText) && productType.equals(typeText) && productSize.equals(sizeText)){
                    existInDb = true;
                    int totalCount = productCount + countText;
                    addData("UPDATE products SET productCount = "+totalCount+" WHERE productName = '"+productName+"' AND " +
                            "productType = '"+productType+"' AND productSize = '"+productSize+"';", event, stage, scene);
                }
            }
            resultSet.close();
            preparedStatement.close();
            conn.close();
            alertWindows.confirmationAlertWindow(event, stage,scene,"Stock added. Do you want to add more stock?","add-stock-window.fxml","main-window.fxml");
        }catch (Exception e){
            alertWindows.errorAlertWindow("An error occured");
        }
        if (existInDb == false) {
            addData("INSERT INTO products VALUES('" + nameText + "','" + typeText + "','" + sizeText + "'," + countText +");", event, stage, scene);
        }

    }


    public void addSaleToDb(String orderNumber, LocalDate date, String nameText,
                                            String typeText, String sizeText, int countText, ActionEvent event, Stage stage, Scene scene) {
        conn = connectToDb();
        Boolean existInStock = false;
        int newProductCount;
        ObservableList<Sale> list = FXCollections.observableArrayList();
        String productName = null;
        String productType = null;
        String productSize = null;
        int productCount = 0;
        try {
            preparedStatement = conn.prepareStatement("SELECT * FROM products");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                productName = resultSet.getString("productName");
                productType = resultSet.getString("productType");
                productSize = resultSet.getString("productSize");
                productCount = resultSet.getInt("productCount");
                if (productName.equals(nameText) && productType.equals(typeText) &&
                        productSize.equals(sizeText)){
                    existInStock = true;
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (existInStock == true) {
            newProductCount = productCount - countText;
            System.out.println(productCount+" "+countText);
            if(productCount == countText){
                addData("INSERT INTO sales VALUES('"+orderNumber+"','"+date+"','"
                        +productName+"','"+productType+"','"+productSize+"',"+productCount+","+true+");", event, stage, scene);
                removeData("DELETE FROM products WHERE productName = '"+productName+"' AND " +
                        "productType = '"+productType+"' AND productSize = '"+productSize+"'");
            }else if (productCount > countText) {
                addData("INSERT INTO sales VALUES('"+orderNumber+"','"+date+"','"
                        +productName+"','"+productType+"','"+productSize+"',"+countText+","+true+");", event, stage, scene);
                addData("UPDATE products SET productCount = "+newProductCount+" WHERE productName = '"+productName+"' AND " +
                        "productType = '"+productType+"' AND productSize = '"+productSize+"';", event, stage, scene);
            }else if (productCount < countText) {
                addData("INSERT INTO sales VALUES('"+orderNumber+"','"+date+"','"
                        +productName+"','"+productType+"','"+productSize+"',"+productCount+","+true+");", event, stage, scene);
                addData("INSERT INTO sales VALUES('"+orderNumber+"','"+date+"','"
                        +productName+"','"+productType+"','"+productSize+"',"+(newProductCount*-1)+","+false+");", event, stage, scene);
                removeData("DELETE FROM products WHERE productName = '"+productName+"' AND " +
                        "productType = '"+productType+"' AND productSize = '"+productSize+"'");
            }
        }else {
            addData("INSERT INTO sales VALUES('"+orderNumber+"','"+date+"','"
                    +nameText+"','"+typeText+"','"+sizeText+"',"+countText+","+false+");", event, stage, scene);
        }
    }
}



