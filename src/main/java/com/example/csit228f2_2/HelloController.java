package com.example.csit228f2_2;

import SQLStuff.CRUDHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.text.Text;

import java.io.IOException;

public class HelloController {
    public ToggleButton tbNight;
    @FXML
    private Label welcomeText;

    @FXML
    private Text tfStatus;

    @FXML
    private TextField tfNewUsername;

    @FXML
    private TextField tfNewPassword;

    @FXML private void onDeleteAccount(){
        CRUDHandler crudHandler = new CRUDHandler();
        crudHandler.deleteData(HelloApplication.loggedInUserID);

        HelloApplication.tfUsername.clear();
        HelloApplication.pfPassword.clear();
        HelloApplication.actionTarget.setText("");
        HelloApplication.actionTarget.setOpacity(0);

        HelloApplication.stage.setScene(HelloApplication.scene);
        HelloApplication.stage.show();
    }

    @FXML
    private void onLogout(){
        HelloApplication.tfUsername.clear();
        HelloApplication.pfPassword.clear();
        HelloApplication.actionTarget.setText("");
        HelloApplication.actionTarget.setOpacity(0);

        HelloApplication.stage.setScene(HelloApplication.scene);
        HelloApplication.stage.show();
    }

    @FXML
    private void setNewUsername(){
        CRUDHandler crudHandler = new CRUDHandler();

        String newUsername = tfNewUsername.getText();

        crudHandler.updateUsername(HelloApplication.loggedInUserID,newUsername);

        tfStatus.setText("Successfully changed username!");
        tfStatus.setOpacity(1);
    }

    @FXML
    private void setNewPassword(){
        CRUDHandler crudHandler = new CRUDHandler();

        String newPassword = tfNewPassword.getText();

        crudHandler.updatePassword(HelloApplication.loggedInUserID,newPassword);

        tfStatus.setText("Successfully changed password!");
        tfStatus.setOpacity(1);
    }

    @FXML
    private void onHelloButtonClick() {
//        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    private void onNightModeClick() {
        if (tbNight.isSelected()) {
            // night mode
            tbNight.getScene().getStylesheets().add(
                    getClass().getResource("styles.css").toExternalForm());
        } else {
            tbNight.getScene().getStylesheets().clear();
        }
    }


}