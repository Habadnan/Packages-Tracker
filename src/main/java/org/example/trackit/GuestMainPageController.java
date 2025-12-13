package org.example.trackit;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GuestMainPageController {


    @FXML
    private Button trackButtonDirect;
    @FXML
    private TextField trackingID;

    @FXML
    private void initialize() {
        trackButtonDirect.setDisable(true);

        ChangeListener<String> listener = (obs, oldText, newText) -> {
            boolean allFilled = !trackingID.getText().isEmpty();
            trackButtonDirect.setDisable(!allFilled);
        };

        trackingID.textProperty().addListener(listener);
    }


    public void handleTrackButton(ActionEvent event) throws IOException {
        // clear old message
        //invalidTrackingID.setText("");

        String input = trackingID.getText().trim();

        // basic format check: exactly 9 digits
        if (!input.matches("\\d{9}")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("Invalid tracking number");
            alert.setContentText("Make sure that tracking number is 9 digits");
            alert.showAndWait();
            return;
        }

        // Get reference to stage and master controller
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent masterRoot = stage.getScene().getRoot();

        if (validateTrackingNumber(input)) {
            MasterPageController masterController = (MasterPageController) masterRoot.getUserData();
            masterController.loadAndSetContent("detail-tracking-page.fxml", input);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("Invalid tracking number");
            alert.setContentText("Make sure that inputted tracking number matches actual number");
            alert.showAndWait();
        }
    }


    private boolean validateTrackingNumber(String trackingNumber){
        return verify(trackingNumber);
    }

    static boolean verify(String trackingNumber) {
        ApiFuture<QuerySnapshot> future = HelloApplication.fstore.collection("shipments").get();
        List<QueryDocumentSnapshot> documents;
        try {
            documents = future.get().getDocuments();
            if (!documents.isEmpty()) {
                for (QueryDocumentSnapshot document : documents) {
                    if (document.getData().get("TrackingNumber").equals(trackingNumber)) {
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