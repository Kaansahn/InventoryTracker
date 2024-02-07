package com.example.stock;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class CheckStockWindowController{
    @FXML
    private TableView<Product> table;
    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, String> typeColumn;

    @FXML
    private TableColumn<Product,String> sizeColumn;

    @FXML
    private TableColumn<Product, Integer> countColumn;

    @FXML
    private TextField textField;

    private Stage stage;
    private Scene scene;

    DatabaseFunctions dbFunctions = new DatabaseFunctions();
    List<Product> products;


    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<Product,String>("productName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Product,String>("productType"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<Product,String>("productSize"));
        countColumn.setCellValueFactory(new PropertyValueFactory<Product,Integer>("productCount"));
        fetchDataAndPopulateTable();
    }

    private void fetchDataAndPopulateTable(){
        products = dbFunctions.displayAllData();
        table.getItems().addAll(products);
    }



    public void findButtonAction(ActionEvent event){
        String text = textField.getText().toUpperCase();
        products.clear();
        table.getItems().clear();
        products = dbFunctions.displayFilteredData(text);
        table.getItems().addAll(products);
    }

    public void backButtonAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-window.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }
}
