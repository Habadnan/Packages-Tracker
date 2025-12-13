package org.example.trackit;

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


import static org.example.trackit.GuestMainPageController.verify;

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

    public static boolean clickedOngoing;

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
                // assuming you changed this method as discussed earlier
                masterController.loadAndSetContent("detail-tracking-page.fxml", input);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Warning");
                alert.setHeaderText("Invalid tracking number");
                alert.setContentText("Make sure that inputted tracking number matches actual number");
                alert.showAndWait();
            }
        }
        else if (source == trackButtonOngoing) {
            clickedOngoing = true;
            fxmlFile = "tracking-page.fxml";
        }
        else if (source == trackButtonPast) {
            clickedOngoing = false;
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


}
