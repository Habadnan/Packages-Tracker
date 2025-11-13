package org.example.trackit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginPageController {

    @FXML private TextField loginUserIdField;
    @FXML private PasswordField loginPasswordField;

    // LOGIN BUTTON
    @FXML
    protected void handleLoginButton(ActionEvent event) {
        String username = loginUserIdField.getText();
        String password = loginPasswordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "User ID and password are required!");
            return;
        }

        if (UserData.checkCredentials(username, password)) {
            showAlert("Success", "Successfully logged in!");
            // Optional: navigate to main dashboard or home page here
        } else {
            showAlert("Error", "Invalid username or password!");
        }
    }

    // NAVIGATE TO SIGNUP PAGE
    @FXML
    protected void handleCreateUserId(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("signup-page.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1200, 800));
            stage.setMaximized(true);
            stage.setTitle("Sign Up - Track-It");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ALERT UTILITY
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
