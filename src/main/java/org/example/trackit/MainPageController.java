package org.example.trackit;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class MainPageController {

    @FXML
    private Button trackButton;
    @FXML
    private TextField trackingID;

    @FXML
    private void initialize() {
        trackButton.setDisable(true);

        ChangeListener<String> listener = (obs, oldText, newText) -> {
            boolean allFilled = !trackingID.getText().isEmpty();
            trackButton.setDisable(!allFilled);
        };

        trackingID.textProperty().addListener(listener);
    }

}
