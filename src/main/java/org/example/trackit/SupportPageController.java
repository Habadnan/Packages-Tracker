package org.example.trackit;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static org.example.trackit.MasterPageController.userLoggedIn;

public class SupportPageController {
    @FXML
    private TextField supportNameField;
    @FXML
    private TextField supportEmailField;
    @FXML
    private Button sendSupportButton;


    @FXML
    private void initialize() {
        sendSupportButton.setDisable(true);

        ChangeListener<String> listener = (obs, oldText, newText) -> {
            boolean allFilled = !supportNameField.getText().isEmpty() && !supportEmailField.getText().isEmpty();
            sendSupportButton.setDisable(!allFilled);
        };

        supportNameField.textProperty().addListener(listener);
        supportEmailField.textProperty().addListener(listener);
    }


    public void handleSupportButton(ActionEvent actionEvent) {
    }

    @FXML
    public void handleBackToHomeButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent masterRoot = stage.getScene().getRoot();
        if (masterRoot != null) {
            MasterPageController masterController = (MasterPageController) masterRoot.getUserData();

            // decide target based on whether a user is logged in
            String targetFxml;
            if (userLoggedIn != null) {
                targetFxml = "user-main-page.fxml";
            } else {
                targetFxml = "guest-main-page.fxml";
            }

            masterController.loadAndSetContent(targetFxml);
        }
    }

}
