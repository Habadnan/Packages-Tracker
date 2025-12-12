package org.example.trackit;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.auth.UserRecord;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AccountDetailsPageController {

    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label stateLabel;
    @FXML
    private Label townLabel;
    @FXML
    private Label zipLabel;
    @FXML
    private Button logoutButton;

    private UserRecord user = MasterPageController.userLoggedIn;

    @FXML
    public void initialize(){
        ApiFuture<QuerySnapshot> future = HelloApplication.fstore.collection("users").get();
        List<QueryDocumentSnapshot> documents;
        try {
            documents = future.get().getDocuments();
            if (!documents.isEmpty()) {
                for (QueryDocumentSnapshot document : documents) {
                    if (document.getData().get("ID").equals(user.getUid())) {
                        firstNameLabel.setText(document.get("FirstName").toString());
                        lastNameLabel.setText(document.get("LastName").toString());
                        usernameLabel.setText(user.getDisplayName());
                        emailLabel.setText(user.getEmail());
                        passwordLabel.setText(document.get("Password").toString());
                        phoneLabel.setText(document.get("PhoneNumber").toString());
                        addressLabel.setText(document.get("Address").toString());
                        stateLabel.setText(document.get("State").toString());
                        townLabel.setText(document.get("Town").toString());
                        zipLabel.setText(document.get("ZipCode").toString());
                        break;
                    }
                }
            }
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
    }


    public void logOut(ActionEvent actionEvent) {
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Parent masterRoot = stage.getScene().getRoot();
        if (masterRoot != null) {
            MasterPageController masterController = (MasterPageController) masterRoot.getUserData();
            masterController.setLoggedOut();
            masterController.loadAndSetContent("guest-main-page.fxml");
        }
    }
}
