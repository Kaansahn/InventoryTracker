package com.example.stock;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class AlertWindows {
    public void errorAlertWindow(String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void informationAlertWindow(ActionEvent event, Stage stage, Scene scene, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void confirmationAlertWindow(ActionEvent event, Stage stage, Scene scene, String content, String yesResult, String noResult) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(content);
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        alert.getButtonTypes().setAll(yesButton, noButton);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()){
            if (result.get() == yesButton) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(yesResult));
                stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(fxmlLoader.load());
                stage.setScene(scene);
                stage.show();
            } else if (result.get() == noButton) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(noResult));
                stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(fxmlLoader.load());
                stage.setScene(scene);
                stage.show();
            }
        }
    }
}
