package org.example.trackit;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SingupPageController {

    @FXML private TextField signupUsernameField;
    @FXML private PasswordField signupPasswordField;
    @FXML private TextField signupEmailField;
    @FXML private TextField signupAddressField;
    @FXML private TextField signupTownField;
    @FXML private ComboBox<String> signupStateComboBox;
    @FXML private TextField signupZipField;
    @FXML private TextField signupPhoneField;

    // Called when the "Create User" button is clicked
    @FXML
    protected void onCreateUserClicked(javafx.event.ActionEvent event) {
        String username = signupUsernameField.getText();
        String password = signupPasswordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Username and password are required!");
            return;
        }

        // Save user credentials (for prototype; replace with Firebase later)
        UserData.saveUser(username, password);

        showAlert("Success", "Account created successfully!");

        // Navigate back to login page
        goToLoginPage(event);
    }

    // Navigation method to go to the login page
    @FXML
    protected void goToLoginPage(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-page.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1200, 800));
            stage.setMaximized(true);
            stage.setTitle("Login - Track-It");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the login page.");
        }
    }

    // Utility method to show alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
