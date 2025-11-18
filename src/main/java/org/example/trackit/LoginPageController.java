package org.example.trackit;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginPageController {
    @FXML
    public TextField loginUserIdField;
    @FXML
    public Button loginButton;
    @FXML
    private TextField loginUsernameField;
    @FXML
    private PasswordField loginPasswordField;


    @FXML
    public void initialize() {
        loginButton.setDisable(true);

        ChangeListener<String> listener = (obs, oldText, newText) -> {
            boolean allFilled = !loginUserIdField.getText().isEmpty() && !loginPasswordField.getText().isEmpty();
            loginButton.setDisable(!allFilled);
        };

        loginUserIdField.textProperty().addListener(listener);
        loginPasswordField.textProperty().addListener(listener);
    }




    @FXML
    private void handleLogin(ActionEvent event) {
        // Get reference to stage and master controller
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Parent masterRoot = stage.getScene().getRoot();
        if (masterRoot != null) {
            MasterPageController masterController = (MasterPageController) masterRoot.getUserData();
            masterController.setUserLoggedIn("Place holder name");
            masterController.loadAndSetContent("main-page.fxml");
        }
    }


    @FXML
    private void handleCreateUserLogin(ActionEvent event) {
        // Get reference to stage and master controller
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Parent masterRoot = stage.getScene().getRoot();
        if (masterRoot != null) {
            MasterPageController masterController = (MasterPageController) masterRoot.getUserData();
            masterController.loadAndSetContent("signup-page.fxml");
        }
    }


}
