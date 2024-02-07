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
import java.time.LocalDate;
import java.util.List;

public class CheckSaleWindowController {
    @FXML
    private TableColumn<Sale, Integer> countColumn;

    @FXML
    private TableColumn<Sale, String> nameColumn;

    @FXML
    private TableColumn<Sale, String> sizeColumn;

    @FXML
    private TableView<Sale> table;

    @FXML
    private TextField textField;

    @FXML
    private TableColumn<Sale, String> typeColumn;

    @FXML
    private TableColumn<Sale, String> orderNumberColumn;

    @FXML
    private TableColumn<Sale, LocalDate> dateColumn;

    @FXML
    private TableColumn<Sale, Boolean> stockColumn;

    List<Sale> sales;
    DatabaseFunctions databaseFunctions = new DatabaseFunctions();
    Stage stage;
    Scene scene;

    public void initialize(){
        orderNumberColumn.setCellValueFactory(new PropertyValueFactory<Sale, String>("orderNumber"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Sale,String>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Sale,String>("type"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<Sale,String>("size"));
        countColumn.setCellValueFactory(new PropertyValueFactory<Sale,Integer>("count"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Sale, LocalDate>("date"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<Sale, Boolean>("inStock"));
        fetchDataAndPopulateTable();
    }

    public void fetchDataAndPopulateTable(){
        sales = databaseFunctions.displayAllSale();
        table.getItems().addAll(sales);
    }

    @FXML
    void backButtonAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-window.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void findButtonAction(ActionEvent event) {
        String text = textField.getText().toUpperCase();
        sales.clear();
        table.getItems().clear();
        sales = databaseFunctions.displayFilteredSales(text);
        table.getItems().addAll(sales);
    }
}
