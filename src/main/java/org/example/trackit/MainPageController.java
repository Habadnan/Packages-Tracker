package org.example.trackit;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class MainPageController {

    @FXML
    public Button trackButton;
    @FXML
    public TextField trackingID;

    @FXML
    public void initialize() {
        trackButton.setDisable(true);

        ChangeListener<String> listener = (obs, oldText, newText) -> {
            boolean allFilled = !trackingID.getText().isEmpty();
            trackButton.setDisable(!allFilled);
        };

        trackingID.textProperty().addListener(listener);
    }

}
