package com.example.stock;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class AddStockWindowController {
    @FXML
    private TextField nameText;
    @FXML
    private TextField typeText;
    @FXML
    private TextField sizeText1;
    @FXML
    private TextField sizeText2;
    @FXML
    private TextField countText;

    private Stage stage;
    private Scene scene;

    DatabaseFunctions dbFunctions = new DatabaseFunctions();

    AlertWindows alertWindows = new AlertWindows();


    public void backButtonAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-window.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    public void addButtonAction(ActionEvent event){
        String productName = nameText.getText().toUpperCase().trim();
        String productType = typeText.getText().toUpperCase().trim();
        String productSize = sizeText1.getText().trim()+"X"+sizeText2.getText().trim();
        if (productName.equals("") || productType.equals("")){
            alertWindows.informationAlertWindow(event,stage,scene,"Product name and Product type can not be empty!");
        }else {
            try{
                int productCount = Integer.parseInt(countText.getText().trim());
                dbFunctions.addStockToDb(productName, productType, productSize, productCount, event, stage, scene);
                //event, stage, scene added for the confirmationAlarmWindow in the DatabaseFunctions class
            }catch (Exception e){
                System.out.println(e);
                alertWindows.errorAlertWindow("Invalid product count input");
            }
        }

    }
}
