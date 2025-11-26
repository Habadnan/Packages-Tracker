package org.example.trackit;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LoginPageController {
    @FXML
    private TextField loginUserIdField;
    @FXML
    private Button loginButton;
    @FXML
    private PasswordField loginPasswordField;

    UserRecord userRecord;
    @FXML
    private void initialize() {
        loginButton.setDisable(true);

        ChangeListener<String> listener = (obs, oldText, newText) -> {
            boolean allFilled = !loginUserIdField.getText().isEmpty() && !loginPasswordField.getText().isEmpty();
            loginButton.setDisable(!allFilled);
        };

        loginUserIdField.textProperty().addListener(listener);
        loginPasswordField.textProperty().addListener(listener);
    }




    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        if(verifyUser()){
            // Get reference to stage and master controller
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Parent masterRoot = stage.getScene().getRoot();
            if (masterRoot != null) {
                MasterPageController masterController = (MasterPageController) masterRoot.getUserData();
                masterController.setUserLoggedIn(userRecord.getDisplayName());
                masterController.loadAndSetContent("main-page.fxml");
            }
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

    private boolean verifyUser() throws IOException {
        return readFirebase(loginUserIdField.getText());
    }

    private boolean readFirebase(String username) {
        boolean key = false;
        ApiFuture<QuerySnapshot> future =  HelloApplication.fstore.collection("users").get();
        List<QueryDocumentSnapshot> documents;
        try{
            documents = future.get().getDocuments();
            if(!documents.isEmpty()){
                System.out.println("Getting (reading) data from firebase database....");
                for (QueryDocumentSnapshot document : documents){
                    if(document.getData().get("Username").equals(username) || document.getData().get("Email").equals(username)){
                        if(document.getData().get("Password").equals(loginPasswordField.getText())){
                            System.out.println("Successfully logged in");
                            userRecord = HelloApplication.fauth.getUser(document.getData().get("ID").toString());
                            key = true;
                            break;
                        }
                        else{
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Error");
                            alert.setHeaderText("Wrong password");
                            alert.setContentText("Please try again");
                            alert.showAndWait();
                            break;
                        }
                    }
                }
            } else{
                System.out.println("No account associated with the provided username/email");
            }
        }
        catch (InterruptedException | ExecutionException | FirebaseAuthException ex)
        {
            ex.printStackTrace();
        }
        return key;
    }
}
