package org.example.trackit;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SingupPageController {

    @FXML
    public TextField signupEmailField;
    @FXML
    public PasswordField signupPasswordField;
    @FXML
    public TextField signupUsernameField;
    @FXML
    public TextField signupAddressField;
    @FXML
    public TextField signupTownField;
    @FXML
    public ComboBox<String> signupStateComboBox;
    @FXML
    public TextField signupZipField;
    @FXML
    public TextField signupPhoneField;

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

            //need to add userinfo collection into firebase database
            DocumentReference docRef = HelloApplication.fstore.collection("UserInfo").document(UUID.randomUUID().toString());
            Map<String, String> data = addUserData(userRecord);

            ApiFuture<WriteResult> result = docRef.set(data);
            return true;

        } catch (FirebaseAuthException ex) {
            System.out.println("Error creating a new user in the firebase");
            return false;
        }
    }

    private Map<String, String> addUserData(UserRecord userRecord) {
        Map<String, String> data = new HashMap<>();
        data.put("Email", signupEmailField.getText());
        data.put("Password", signupPasswordField.getText());
        data.put("ID", userRecord.getUid());
        data.put("Username", signupUsernameField.getText());
        data.put("Address", signupAddressField.getText());
        data.put("Town", signupTownField.getText());
        data.put("State", signupStateComboBox.getSelectionModel().getSelectedItem());
        data.put("ZipCode", signupZipField.getText());
        data.put("PhoneNumber", signupPhoneField.getText());
        return data;
    }
}

