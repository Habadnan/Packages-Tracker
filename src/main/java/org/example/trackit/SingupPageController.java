package org.example.trackit;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class SingupPageController {
    @FXML
    private TextField signupEmailField;
    @FXML
    private PasswordField signupPasswordField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
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
                    && !firstNameField.getText().isEmpty() && !lastNameField.getText().isEmpty()
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
        if(isRepeatUsername(signupUsernameField.getText())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("Invalid name");
            alert.setContentText("Username is already taken");
            alert.showAndWait();
        }
        else{
            register();
            // Get reference to stage and master controller
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Parent masterRoot = stage.getScene().getRoot();
            if (masterRoot != null) {
                MasterPageController masterController = (MasterPageController) masterRoot.getUserData();
                masterController.setUserLoggedIn(signupUsernameField.getText());
                masterController.loadAndSetContent("user-main-page.fxml");
            }
        }
    }

    private boolean register(){
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(signupEmailField.getText())
                .setEmailVerified(false)
                .setPassword(signupPasswordField.getText())
                .setDisplayName(signupUsernameField.getText())
                .setDisabled(false);

        UserRecord userRecord;
        try {
            userRecord = HelloApplication.fauth.createUser(request);
            System.out.println("Created new user with Firebase ID: " + userRecord.getUid());

            DocumentReference docRef = HelloApplication.fstore.collection("users").document(UUID.randomUUID().toString());
            Map<String, String> data = addUserData(userRecord);

            ApiFuture<WriteResult> result = docRef.set(data);
            return true;

        } catch (FirebaseAuthException ex) {
            System.out.println("There was an error creating a new user in the database");
            return false;
        }
    }

    private Map<String, String> addUserData(UserRecord userRecord) {
        Map<String, String> data = new HashMap<>();
        data.put("Email", signupEmailField.getText());
        data.put("Password", signupPasswordField.getText());
        data.put("ID", userRecord.getUid());
        data.put("FirstName", firstNameField.getText());
        data.put("LastName", lastNameField.getText());
        data.put("Username", signupUsernameField.getText());
        data.put("Address", signupAddressField.getText());
        data.put("Town", signupTownField.getText());
        data.put("State", signupStateComboBox.getSelectionModel().getSelectedItem().toString());
        data.put("ZipCode", signupZipField.getText());
        data.put("PhoneNumber", signupPhoneField.getText());
        return data;
    }

    private boolean isRepeatUsername(String username) {
        ApiFuture<QuerySnapshot> future = HelloApplication.fstore.collection("users").get();
        List<QueryDocumentSnapshot> documents;
        try {
            documents = future.get().getDocuments();
            if (!documents.isEmpty()) {
                for (QueryDocumentSnapshot document : documents) {
                    if (document.getData().get("Username").equals(username)) {
                        System.out.println("Username already exists in the database");
                        return true;
                    }
                }
            }
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
