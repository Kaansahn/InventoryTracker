package com.example.stock;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class AddSaleWindowController {
    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField orderNumberText;

    @FXML
    private TextField productCountText;

    @FXML
    private TextField productNameText;

    @FXML
    private TextField productSizeText1;

    @FXML
    private TextField productSizeText2;

    @FXML
    private TextField productTypeText;

    @FXML
    private TableView<Sale> table;

    @FXML
    private TableColumn<Sale,String> nameColumn;
    @FXML
    private TableColumn<Sale,String> typeColumn;
    @FXML
    private TableColumn<Sale,String> sizeColumn;
    @FXML
    private TableColumn<Sale,Integer> countColumn;


    String orderNumber;
    LocalDate date;
    String productName;
    String productType;
    String productSize1;
    String productSize2;
    String productSize;
    int productCount;

    AlertWindows alertWindows = new AlertWindows();

    Stage stage;
    Scene scene;

    DatabaseFunctions databaseFunctions = new DatabaseFunctions();

    public void initialize(){
        nameColumn.setCellValueFactory(new PropertyValueFactory<Sale,String>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Sale,String>("type"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<Sale,String>("size"));
        countColumn.setCellValueFactory(new PropertyValueFactory<Sale,Integer>("count"));
    }

    @FXML
    void addButtonAction(ActionEvent event) {
        productName = productNameText.getText().toUpperCase().trim();
        productType = productTypeText.getText().toUpperCase().trim();
        productSize1 = productSizeText1.getText().trim();
        productSize2 = productSizeText2.getText().trim();
        productSize = productSize1+"X"+productSize2;
        try {
            productCount = Integer.parseInt(productCountText.getText().trim());
        }catch (NumberFormatException e){
            alertWindows.errorAlertWindow("Invalid integer input");
        }

        if (table.getItems().isEmpty()){
            addItemToTable(productName,productType,productSize,productCount);
        }else {
            Boolean productExist = false;
            for(Sale sale : table.getItems()) {
                if (sale.getName().equals(productName) && sale.getType().equals(productType) &&
                        sale.getSize().equals(productSize)) {
                    sale.setCount(sale.getCount() + productCount);
                    table.refresh();
                    productExist = true;
                }
            }
            if (productExist == false) {
                addItemToTable(productName,productType,productSize,productCount);
            }
        }
        clearTextFields();
    }

    public void addItemToTable(String productName, String productType, String productSize, int productCount){
        if (productName.equals("") || productType.equals("")){
            alertWindows.errorAlertWindow("Please fill all of the required blanks");
        }else {
            table.getItems().add(new Sale(orderNumber, productName,productType,productSize,productCount,date,false));
        }
    }

    public void clearTextFields(){
        productNameText.clear();
        productTypeText.clear();
        productSizeText1.clear();
        productSizeText2.clear();
        productCountText.clear();
    }

    @FXML
    void addSaleButtonAction(ActionEvent event) {
        date = datePicker.getValue();
        orderNumber = orderNumberText.getText().toUpperCase().trim();
        ObservableList<Sale> allPorducts = table.getItems();
        try{
            for (Sale sale : allPorducts) {
                databaseFunctions.addSaleToDb(orderNumber, date, sale.getName(), sale.getType(),
                        sale.getSize(), sale.getCount(), event, stage, scene);
            }
            System.out.println("Sales: "+allPorducts);
            alertWindows.confirmationAlertWindow(event, stage,scene,"Sale added. Do you want to add more sale?","add-sale-window.fxml","main-window.fxml");
        }catch (Exception e){
            alertWindows.errorAlertWindow("Error occured at addSaleButtonAction");
        }

     }

    @FXML
    void backButtonAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-window.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void removeButtonAction(ActionEvent event) {
        Sale selectedRow = table.getSelectionModel().getSelectedItem();
        table.getItems().remove(selectedRow);
    }
}
