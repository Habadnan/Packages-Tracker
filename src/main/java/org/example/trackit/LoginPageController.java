package org.example.trackit;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
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
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LoginPageController {
    public TextField loginUserIdField;
    @FXML
    private TextField loginUsernameField;

    @FXML
    private PasswordField loginPasswordField;

    @FXML
    private void handleLogin() {
        System.out.println("Logging in: " + loginUsernameField.getText());
    }

    @FXML
    private void handleCreateUserId(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("signup-page.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.setTitle("Sign Up - Track-It");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean verifyUser() throws IOException {
        UserRecord userRecord;
            try{
                //check to see if there even is an associated email address inside the firebase auth service
                userRecord = HelloApplication.fauth.getUserByEmail(loginUsernameField.getText());
                //if there is then find the users collection inside userinfo that has the email address
                if(readFirebase(userRecord)){
                    //implement logic to go to a different pane after successfully logging in
                }
            } catch(FirebaseAuthException e){
                System.out.println("E-mail is not associated with any user");
                return false;
            }
        return true;
    }

    public boolean readFirebase(UserRecord userRecord) {
        boolean key = false;
        ApiFuture<QuerySnapshot> future =  HelloApplication.fstore.collection("UserInfo").get();
        List<QueryDocumentSnapshot> documents;
        try{
            documents = future.get().getDocuments();
            if(!documents.isEmpty()){
                System.out.println("Getting (reading) data from firebase database....");
                for (QueryDocumentSnapshot document : documents){
                    //check if the documents have the email address in userinfo collection that also has the associated password
                    if(document.getData().get("ID").equals(userRecord.getUid())){
                        if(document.getData().get("Password").equals(loginPasswordField.getText())){
                            System.out.println("Successfully logged in");
                            key = true;
                            break;
                        }
                        else{
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Error");
                            alert.setHeaderText("Wrong password");
                            alert.setContentText("Please try again");
                            alert.showAndWait();
                        }
                    }
                }
            }
            else
            {
                System.out.println("No data");
            }
        }
        catch (InterruptedException | ExecutionException ex)
        {
            ex.printStackTrace();
        }
        return key;
    }

}
