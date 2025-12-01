package org.example.trackit;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class SingupPageController {
    @FXML
    private TextField signupEmailField;
    @FXML
    private PasswordField signupPasswordField;
    @FXML
    private TextField signupUsernameField;
    @FXML
    private TextField signupAddressField;
    @FXML
    private TextField signupTownField;
    @FXML
    private ComboBox signupStateComboBox;
    @FXML
    private TextField signupZipField;
    @FXML
    private TextField signupPhoneField;
    @FXML
    private Button createUserButton;


    @FXML
    private void initialize() {
        // List of US states (example)
        List<String> states = Arrays.asList(
                "Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut",
                "Delaware", "District of Columbia", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana",
                "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota",
                "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York",
                "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania",
                "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont",
                "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"
                // Add more states as needed
        );
        signupStateComboBox.setItems(FXCollections.observableArrayList(states));

        createUserButton.setDisable(true);

        ChangeListener<String> listener = (obs, oldText, newText) -> {
            boolean allFilled = !signupEmailField.getText().isEmpty() && !signupPasswordField.getText().isEmpty()
                    && !signupUsernameField.getText().isEmpty() && !signupAddressField.getText().isEmpty()
                    && !signupTownField.getText().isEmpty() && !signupStateComboBox.getSelectionModel().isEmpty()
                    && !signupZipField.getText().isEmpty() && !signupPhoneField.getText().isEmpty();
            createUserButton.setDisable(!allFilled);
        };

        signupEmailField.textProperty().addListener(listener);
        signupPasswordField.textProperty().addListener(listener);
        signupUsernameField.textProperty().addListener(listener);
        signupAddressField.textProperty().addListener(listener);
        signupTownField.textProperty().addListener(listener);
        signupStateComboBox.getSelectionModel().selectedItemProperty().addListener(listener);
        signupZipField.textProperty().addListener(listener);
        signupPhoneField.textProperty().addListener(listener);
    }



    @FXML
    public void handleCreateUserSignup(ActionEvent event) {
        // Get reference to stage and master controller
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Parent masterRoot = stage.getScene().getRoot();
        if (masterRoot != null) {
            MasterPageController masterController = (MasterPageController) masterRoot.getUserData();
            masterController.setUserLoggedIn("Place holder name");
            masterController.loadAndSetContent("guest-main-page.fxml");
        }
    }
}
