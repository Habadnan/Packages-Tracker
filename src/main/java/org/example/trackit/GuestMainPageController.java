package org.example.trackit;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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


    public void handleTrackButton(ActionEvent event) {
        // Get reference to stage and master controller
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Parent masterRoot = stage.getScene().getRoot();
        if (masterRoot != null) {
            MasterPageController masterController = (MasterPageController) masterRoot.getUserData();
            masterController.loadAndSetContent("detail-tracking-page.fxml");
        }

    }
}
