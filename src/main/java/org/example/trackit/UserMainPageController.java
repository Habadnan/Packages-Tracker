package org.example.trackit;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserMainPageController {
    @FXML
    public Button trackButtonDirect;
    @FXML
    public Button trackButtonOngoing;
    @FXML
    public Button trackButtonPast;
    @FXML
    public Button shipButton;
    @FXML
    public TextField trackingID;

    @FXML
    private Label invalidTrackingID;

    @FXML
    private void initialize() {
        trackButtonDirect.setDisable(true);

        ChangeListener<String> listener = (obs, oldText, newText) -> {
            boolean allFilled = !trackingID.getText().isEmpty();
            trackButtonDirect.setDisable(!allFilled);
        };

        trackingID.textProperty().addListener(listener);
    }


    @FXML
    public void handleButton(ActionEvent event) {
        Object source = event.getSource();
        String fxmlFile = null;

        if (source == trackButtonDirect) {
            // clear old message
            invalidTrackingID.setText("");

            String input = trackingID.getText().trim();

            // basic format check: exactly 9 digits
            if (!input.matches("\\d{9}")) {
                invalidTrackingID.setText("INVALID TRACKING ID NUMBER");
                return;
            }

            // Get reference to stage and master controller
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent masterRoot = stage.getScene().getRoot();

            if (validateTrackingNumber(input)) {
                MasterPageController masterController = (MasterPageController) masterRoot.getUserData();
                // assuming you changed this method as discussed earlier
                masterController.loadAndSetContent("detail-tracking-page.fxml", input);
            } else {
                invalidTrackingID.setText("INVALID TRACKING ID NUMBER");
            }
        }
        else if (source == trackButtonOngoing) {
            fxmlFile = "tracking-page.fxml";
        }
        else if (source == trackButtonPast) {
            fxmlFile = "tracking-page.fxml";
        }
        else if (source == shipButton) {
            fxmlFile = "shipping-page.fxml";
        }
        if (fxmlFile != null) {
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Parent masterRoot = stage.getScene().getRoot();
            if (masterRoot != null) {
                MasterPageController masterController = (MasterPageController) masterRoot.getUserData();
                masterController.loadAndSetContent(fxmlFile);
            }
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
